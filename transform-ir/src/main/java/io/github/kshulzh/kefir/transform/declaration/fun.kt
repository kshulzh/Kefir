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

import io.github.kshulzh.kefir.model.api.declatation.KtFunctionElement
import io.github.kshulzh.kefir.model.api.modifiers.KtModifiers
import io.github.kshulzh.kefir.transform.context.KtFirLocalTransformContext
import io.github.kshulzh.kefir.transform.context.KtIrLocalTransformContext
import io.github.kshulzh.kefir.transform.model.replaceBody
import io.github.kshulzh.kefir.transform.utils.*
import io.github.kshulzh.kefir.transform.utils.type.resolveType
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.descriptors.Visibilities
import org.jetbrains.kotlin.fir.backend.FirMetadataSource
import org.jetbrains.kotlin.fir.declarations.*
import org.jetbrains.kotlin.fir.declarations.builder.buildSimpleFunction
import org.jetbrains.kotlin.fir.declarations.impl.FirDeclarationStatusImpl
import org.jetbrains.kotlin.fir.declarations.utils.classId
import org.jetbrains.kotlin.fir.moduleData
import org.jetbrains.kotlin.fir.symbols.impl.ConeClassLikeLookupTagImpl
import org.jetbrains.kotlin.fir.symbols.impl.FirNamedFunctionSymbol
import org.jetbrains.kotlin.fir.toFirResolvedTypeRef
import org.jetbrains.kotlin.fir.types.impl.ConeClassLikeTypeImpl
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.symbols.impl.IrSimpleFunctionSymbolImpl

/**
 * Transforms a given Kotlin function element into an IR (Intermediate Representation) simple function.
 *
 * This method processes a `KtFunctionElement`, utilizing its properties and resolving its type,
 * to generate a corresponding `IrSimpleFunction`. The transformation incorporates the function's
 * modifiers, parameters, body, and other structural aspects, ensuring that the resulting IR function
 * conforms to the specified attributes of the input function element.
 *
 * @param input The `KtFunctionElement` representing the Kotlin function to be transformed.
 *              This element contains all the necessary details such as the function's name,
 *              parameters, modifiers, and body, which are utilized during the transformation process.
 * @return The transformed `IrSimpleFunction` representing the IR equivalent of the input Kotlin function element.
 */
fun KtIrLocalTransformContext.transformIrFunction(input: KtFunctionElement): IrSimpleFunction {
    val type = irTransform(input.resolveType()!!)

    return pluginContext.irFactory.createSimpleFunction(
        startOffset = UNDEFINED_OFFSET,
        endOffset = UNDEFINED_OFFSET,
        //todo
        origin = IrDeclarationOrigin.DEFINED,
        name = input.name.transform(),
        visibility = input.visibilityDescriptor ?: DescriptorVisibilities.PUBLIC,
        isInline = input.modifiers.contains(KtModifiers.INLINE),
        isExpect = input.modifiers.contains(KtModifiers.EXPECT),
        returnType = type,
        modality = input.modalityModifier ?: Modality.FINAL,
        symbol = IrSimpleFunctionSymbolImpl(),
        isTailrec = input.modifiers.contains(KtModifiers.TAILREC),
        isSuspend = input.modifiers.contains(KtModifiers.SUSPEND),
        isOperator = input.modifiers.contains(KtModifiers.OPERATOR),
        isInfix = input.modifiers.contains(KtModifiers.INFIX),
        isExternal = input.modifiers.contains(KtModifiers.EXTERNAL),
    ).linkIr(input).apply {
        fork {
            metadata = FirMetadataSource.Function(with(this@transformIrFunction.fir()) { firTransform(input)!! })
        }
        fork {
            parameters = input.parameters.map { irTransform(it)!! }
            parameters.forEach {
                it.parent = this
            }
        }


        fork {
            //todo add external
            parent = input.declarationsScope?.getIr() ?: throw RuntimeException("No parent found")
        }

        fork {
            input.body?.also { replaceBody(it) }
        }
    }
}


/**
 * Transforms a Kotlin function element (`KtFunctionElement`) into a FIR simple function (`FirSimpleFunction`).
 * The transformation is performed within the FIR-based local transformation context, which provides access
 * to necessary structural, attribute, and session-related data.
 *
 * @param input The function element (`KtFunctionElement`) to transform. This element represents a function
 *              declaration, including its parameters, body, return type, and other associated attributes.
 * @return A `FirSimpleFunction` representation of the input function element, fully constructed and linked
 *         within the FIR transformation context.
 */
fun KtFirLocalTransformContext.transformFirFunction(input: KtFunctionElement): FirSimpleFunction {
    val returnType = firTransform(input.resolveType()!!)?.toFirResolvedTypeRef()
    val parent = input.declarationsScope.getFirOrExternal<FirDeclaration>()!!
    return buildSimpleFunction {
        source = createSource()
        resolvePhase = FirResolvePhase.BODY_RESOLVE
        moduleData = firSession.moduleData
        origin = FirDeclarationOrigin.Source
        attributes = FirDeclarationAttributes()
        status = FirDeclarationStatusImpl(
            visibility = Visibilities.Public,
            modality = Modality.OPEN
        )
        returnTypeRef = returnType ?: firSession.builtinTypes.unitType
        receiverParameter = null
        deprecationsProvider = UnresolvedDeprecationProvider
        containerSource = null
        dispatchReceiverType = if (parent is FirClass) {
            ConeClassLikeTypeImpl(
                ConeClassLikeLookupTagImpl(parent.classId),
                emptyArray(),
                false
            )
        } else {
            null
        }
        //contextParameters = mutableListOf()
        //valueParameters = mutableListOf()
        //body: FirBlock? = null
        //contractDescription = null
        name = input.name.transform()
        symbol = FirNamedFunctionSymbol(input.callableId())
        //annotations= mutableListOf()
        //typeParameters = mutableListOf()
    }.linkFir(input).apply {
        firStructure.addCallable(this)
        //todo if context is fir enable it
//        input.body?.let {
//            this.replaceBody(it, expressionTransformer)
//        }
        fork {
            replaceValueParameters(input.parameters.filter { it.name != "<this>" }.map { firTransform(it)!! })
        }
    }
}