/*
 * Copyright (c) 2025-2026. Kirill Shulzhenko
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.kshulzh.kefir.transform.declaration

import io.github.kshulzh.kefir.model.api.arg.KtParameterElement
import io.github.kshulzh.kefir.model.api.declaration.KtFieldElement
import io.github.kshulzh.kefir.model.api.declaration.KtPropertyElement
import io.github.kshulzh.kefir.transform.context.KtFirLocalTransformContext
import io.github.kshulzh.kefir.transform.context.KtIrLocalTransformContext
import io.github.kshulzh.kefir.transform.utils.*
import io.github.kshulzh.kefir.transform.utils.type.resolveType
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.descriptors.Visibilities
import org.jetbrains.kotlin.fir.backend.FirMetadataSource
import org.jetbrains.kotlin.fir.declarations.*
import org.jetbrains.kotlin.fir.declarations.builder.buildBackingField
import org.jetbrains.kotlin.fir.declarations.builder.buildProperty
import org.jetbrains.kotlin.fir.declarations.builder.buildPropertyAccessor
import org.jetbrains.kotlin.fir.declarations.builder.buildValueParameter
import org.jetbrains.kotlin.fir.declarations.impl.FirDeclarationStatusImpl
import org.jetbrains.kotlin.fir.declarations.utils.classId
import org.jetbrains.kotlin.fir.moduleData
import org.jetbrains.kotlin.fir.symbols.impl.*
import org.jetbrains.kotlin.fir.toFirResolvedTypeRef
import org.jetbrains.kotlin.fir.types.impl.ConeClassLikeTypeImpl
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.symbols.impl.IrPropertySymbolImpl
import org.jetbrains.kotlin.ir.util.patchDeclarationParents

/**
 * Transforms a [KtPropertyElement] into an [IrProperty] representation within the context
 * of intermediate representation (IR) transformation. The method populates and links
 * various property components such as the backing field, getter, and setter, if they exist,
 * and associates the metadata and parent context accordingly.
 *
 * @param input The [KtPropertyElement] representing the property in the Kotlin model structure to be transformed.
 * @return A transformed [IrProperty] corresponding to the input element, or null if the transformation cannot be completed.
 */
fun KtIrLocalTransformContext.transformIrProperty(input: KtPropertyElement): IrProperty {
    return transformContext.pluginContext.irFactory.createProperty(
        startOffset = UNDEFINED_OFFSET,
        endOffset = UNDEFINED_OFFSET,
        origin = IrDeclarationOrigin.DEFINED,
        name = input.name.transform(),
        visibility = input.visibilityDescriptor ?: DescriptorVisibilities.PUBLIC,
        modality = input.modalityModifier ?: Modality.FINAL,
        symbol = IrPropertySymbolImpl(),
        isVar = false,
        isConst = false,
        isLateinit = false,
        isDelegated = false,
    ).linkIr(input).apply {
        input.field?.also {
            fork {
                backingField = (irTransform(it)!!).also { field1 ->
                    field1.correspondingPropertySymbol = symbol
                }
            }
        }

        input.getter?.also {
            fork {
                getter = (irTransform(it) as? IrSimpleFunction)?.also { get1 ->
                    get1.correspondingPropertySymbol = symbol
                }
            }
        }


        input.setter?.also {
            fork {
                setter = (irTransform(it) as? IrSimpleFunction)?.also { set1 ->
                    set1.correspondingPropertySymbol = symbol
                }
            }
        }

        fork {
            metadata = FirMetadataSource.Property(with(this@transformIrProperty.fir()) { firTransform(input)!! })
        }

        fork {
            parent = input.declarationsScope?.getIr() ?: throw RuntimeException("No parent found")
        }
        patchDeclarationParents()
    }
}

/**
 * Transforms a [KtPropertyElement] into a corresponding [FirProperty] representation within the FIR pipeline.
 * This method handles creating FIR attributes, structure, declarations, and related components for a property.
 *
 * @param input The [KtPropertyElement] representing the Kotlin property to be transformed into FIR.
 * @return A [FirProperty] representation of the provided input or null if the transformation fails.
 */
fun KtFirLocalTransformContext.transformFirProperty(input: KtPropertyElement): FirProperty {
    val symbol = FirRegularPropertySymbol(input.callableId())
    val returnType = firTransform(input.resolveType())?.toFirResolvedTypeRef()
    val parent = input.declarationsScope.getFirOrExternal<FirDeclaration>()!!
    val field1 = input.field?.let { transformFirBackingField(symbol, it) }

    return buildProperty {
        source = createSource()
        resolvePhase = FirResolvePhase.BODY_RESOLVE
        moduleData = transformContext.firSession.moduleData
        origin = FirDeclarationOrigin.Source
        attributes = FirDeclarationAttributes()
        status = FirDeclarationStatusImpl(
            visibility = Visibilities.Public,
            modality = Modality.OPEN
        )
        returnTypeRef = returnType ?: transformContext.firSession.builtinTypes.unitType
        receiverParameter = null
        deprecationsProvider = UnresolvedDeprecationProvider
        containerSource = null
        dispatchReceiverType = if (parent is FirClass) {
            ConeClassLikeTypeImpl(
                ConeClassLikeLookupTagImpl(parent.classId),
                emptyArray(),
                false
            )
        } else null

        //contextParameters = mutableListOf()
        name = input.name.transform()
        //initializer = input.value?.let { expressionTransformer.transform(it) }
        //delegate: FirExpression? = null
        isVar = true
        getter = null
        setter = null
        backingField = field1
        //annotations = mutableListOf()
        this.symbol = symbol
        //delegateFieldSymbol = null
        bodyResolveState = FirPropertyBodyResolveState.NOTHING_RESOLVED
        //typeParameters= mutableListOf()
    }.linkFir(input).apply {
        transformContext.firStructure.addCallable(this, true)

        input.setter?.also {
            fork {
                replaceSetter(transformFirFieldAccessor(input, false))
            }
        }

        input.getter?.also {
            fork {
                replaceGetter(transformFirFieldAccessor(input, true))
            }
        }
    }
}

/**
 * Transforms a given Kotlin property element into a FIR (Frontend Intermediate Representation) property accessor.
 *
 * This method is designed to handle both getter and setter transformations for a property element.
 * The transformation process utilizes the current transformation context and associates the resulting FIR property accessor
 * with the appropriate parent FIR declaration. It also constructs the necessary attributes, modifiers,
 * and type references for the property accessor.
 *
 * @param input The Kotlin property element to transform, including its getter, setter, and associated type details.
 * @param isGetter1 A boolean flag to indicate whether the transformation is for a getter (`true`) or setter (`false`).
 * @return The transformed FIR property accessor for the provided input element, or `null` if the transformation cannot be performed.
 */
private fun KtFirLocalTransformContext.transformFirFieldAccessor(
    input: KtPropertyElement,
    isGetter1: Boolean
): FirPropertyAccessor {
    val returnType = firTransform(input.resolveType())?.toFirResolvedTypeRef()
    val function = if (isGetter1) input.getter else input.setter
    val parent = input.declarationsScope.getFirOrExternal<FirDeclaration>()!!
    return buildPropertyAccessor {
        source = createSource()
        resolvePhase = FirResolvePhase.RAW_FIR
        moduleData = transformContext.firSession.moduleData
        origin = FirDeclarationOrigin.Source
        attributes = FirDeclarationAttributes()
        status = FirDeclarationStatusImpl(
            visibility = Visibilities.Public,
            modality = Modality.OPEN
        )
        returnTypeRef = returnType ?: transformContext.firSession.builtinTypes.unitType
        deprecationsProvider = UnresolvedDeprecationProvider
        dispatchReceiverType = if (parent is FirClass) {
            ConeClassLikeTypeImpl(
                ConeClassLikeLookupTagImpl(parent.classId),
                emptyArray(),
                false
            )
        } else {
            null
        }
        //override val valueParameters: MutableList<FirValueParameter> = mutableListOf()
        //override var body: FirBlock? = null
        contractDescription = null
        symbol = FirPropertyAccessorSymbol()
        propertySymbol = input.getFir<FirProperty>()?.symbol!!
        isGetter = isGetter1
        fork {
            val params =
                function!!.parameters.filter { it.name != "<this>" }.map { transformFirValueParameter(symbol, it) }
            valueParameters.addAll(params)
        }
        //override val annotations: MutableList<FirAnnotation> = mutableListOf()
    }
}


/**
 * Transforms a FIR backing field using information from the given property symbol and field element.
 *
 * @param firProperty The FIR property symbol representing the property for which the backing field is being created or transformed.
 * @param input The field element that serves as the input for the transformation, providing necessary type and value information.
 * @return The transformed `FirBackingField`, or null if the transformation could not be completed.
 */
private fun KtFirLocalTransformContext.transformFirBackingField(
    firProperty: FirPropertySymbol,
    input: KtFieldElement
): FirBackingField {
    val returnType = firTransform(input.resolveType()!!)?.toFirResolvedTypeRef()
    input.declarationsScope.getFirOrExternal<FirDeclaration>()!!
    return buildBackingField {
        source = createSource()
        resolvePhase = FirResolvePhase.RAW_FIR
        moduleData = firSession.moduleData
        origin = FirDeclarationOrigin.Source
        attributes = FirDeclarationAttributes()
        returnTypeRef = returnType ?: firSession.builtinTypes.unitType
        deprecationsProvider = UnresolvedDeprecationProvider
        name = input.name.transform()
        isVar = true
        isVal = false
        symbol = FirBackingFieldSymbol()
        propertySymbol = firProperty
        initializer = null
        //annotations: MutableList<FirAnnotation> = mutableListOf()
        status = FirDeclarationStatusImpl(
            visibility = Visibilities.Public,
            modality = Modality.OPEN
        )

    }.apply {
        fork {
            replaceInitializer(input.value?.let { firTransform(it) })
        }
    }
}

/**
 * Transforms a given `KtParameterElement` into a `FirValueParameter` using the specified `FirPropertyAccessorSymbol`
 * as its containing declaration. Constructs a `FirValueParameter` with attributes, type references, and other
 * properties resolved within the transformation context.
 *
 * @param firProperty The `FirPropertyAccessorSymbol` representing the property or accessor associated with the
 * containing declaration for the created `FirValueParameter`.
 * @param input The `KtParameterElement` to be transformed into a `FirValueParameter`. This represents the
 * Kotlin language model's parameter definition used as input for the transformation.
 * @return The resulting `FirValueParameter` constructed from the given `KtParameterElement`, or `null` if
 * the transformation cannot be completed (e.g., if the type resolution fails).
 */
private fun KtFirLocalTransformContext.transformFirValueParameter(
    firProperty: FirPropertyAccessorSymbol,
    input: KtParameterElement
): FirValueParameter {
    val returnType = firTransform(input.resolveType()!!)?.toFirResolvedTypeRef()
    return buildValueParameter {
        source = createSource()
        resolvePhase = FirResolvePhase.RAW_FIR
        moduleData = firSession.moduleData
        origin = FirDeclarationOrigin.Source
        attributes = FirDeclarationAttributes()
        returnTypeRef = returnType ?: firSession.builtinTypes.unitType
        deprecationsProvider = UnresolvedDeprecationProvider
        name = input.name.transform()
        //override val annotations: MutableList<FirAnnotation> = mutableListOf()
        symbol = FirValueParameterSymbol()
        //defaultValue: FirExpression? = null
        containingDeclarationSymbol = firProperty
        isCrossinline = false
        isNoinline = false
        isVararg = false
        valueParameterKind = FirValueParameterKind.Regular

    }
}

