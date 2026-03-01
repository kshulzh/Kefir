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

import io.github.kshulzh.kefir.model.api.declatation.KtClassElement
import io.github.kshulzh.kefir.model.api.declatation.KtConstructorElement
import io.github.kshulzh.kefir.transform.context.KtFirLocalTransformContext
import io.github.kshulzh.kefir.transform.context.KtIrLocalTransformContext
import io.github.kshulzh.kefir.transform.utils.*
import org.jetbrains.kotlin.DeprecatedForRemovalCompilerApi
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.descriptors.Visibilities
import org.jetbrains.kotlin.fir.backend.FirMetadataSource
import org.jetbrains.kotlin.fir.declarations.*
import org.jetbrains.kotlin.fir.declarations.impl.FirDeclarationStatusImpl
import org.jetbrains.kotlin.fir.moduleData
import org.jetbrains.kotlin.fir.resolve.defaultType
import org.jetbrains.kotlin.fir.symbols.SymbolInternals
import org.jetbrains.kotlin.fir.symbols.impl.FirConstructorSymbol
import org.jetbrains.kotlin.fir.toFirResolvedTypeRef
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.builders.declarations.buildConstructor
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrConstructor
import org.jetbrains.kotlin.ir.expressions.IrBlock
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.types.typeWith

/**
 * Transforms a given Kotlin constructor element (`KtConstructorElement`)
 * into its Intermediate Representation (IR) as an `IrConstructor`.
 *
 * This method leverages the local transformation context to convert
 * the input Kotlin constructor element into an IR structure that can be
 * further utilized within the Kotlin IR pipeline.
 *
 * @param input The `KtConstructorElement` to be transformed into an `IrConstructor`.
 *              This element represents a constructor within the Kotlin model.
 * @return An `IrConstructor` if the transformation is successful, or `null` if
 *         the transformation cannot be performed.
 */
@OptIn(DeprecatedForRemovalCompilerApi::class, UnsafeDuringIrConstructionAPI::class)
fun KtIrLocalTransformContext.transformIrConstructor(input: KtConstructorElement): IrConstructor? {
    return transformContext.pluginContext.irFactory.buildConstructor {
        isInline = false
        isExternal = false

        Modality.FINAL
    }.linkIr(input).apply {
        this.parent = input.declarationsScope.getIrOrExternal() ?: throw RuntimeException("Parent is null")
        returnType = input.declarationsScope.getIrOrExternal<IrClass>()!!.typeWith()
        fork {
            metadata = FirMetadataSource.Function(with(this@transformIrConstructor.fir()) { firTransform(input)!! })
        }

        fork {
            this.body = input.body?.let { irTransform(it) }?.let { exp ->
                if (exp is IrBlock) {
                    pluginContext.irFactory.createBlockBody(
                        UNDEFINED_OFFSET,
                        UNDEFINED_OFFSET
                    ).also {
                        it.statements.addAll(exp.statements)
                    }
                } else {
                    pluginContext.irFactory.createExpressionBody(UNDEFINED_OFFSET, UNDEFINED_OFFSET, exp)
                }
            }
        }

    }
}

/**
 * Transforms a given Kotlin constructor element into a FIR (Frontend Intermediate Representation) constructor.
 *
 * This function facilitates the conversion of a `KtConstructorElement` to a `FirConstructor` by extracting
 * the necessary information from the input and constructing an FIR-level representation of the constructor.
 * It uses the current transformation context to access relevant data and utilities for the transformation.
 * Throws a runtime exception if the associated class is null.
 *
 * @param input The Kotlin constructor element (`KtConstructorElement`) to be transformed.
 * @return The transformed FIR constructor (`FirConstructor`), or null if the transformation fails.
 */
@OptIn(DirectDeclarationsAccess::class, SymbolInternals::class)
fun KtFirLocalTransformContext.transformFirConstructor(input: KtConstructorElement): FirConstructor? {
    val classId = (input.declarationsScope as? KtClassElement)?.classId() ?: throw RuntimeException("Class is null")
    val returnType = (input.declarationsScope.getFirOrExternal<FirDeclaration>()?.symbol?.fir as FirClass).defaultType()
        .toFirResolvedTypeRef()
    return org.jetbrains.kotlin.fir.declarations.builder.buildConstructor {
        source = createSource()
        resolvePhase = FirResolvePhase.BODY_RESOLVE
        moduleData = transformContext.firSession.moduleData
        origin = FirDeclarationOrigin.Source
        attributes = FirDeclarationAttributes()
        status = FirDeclarationStatusImpl(
            visibility = Visibilities.Public,
            modality = Modality.OPEN
        )
        attributes = FirDeclarationAttributes()
        //override val typeParameters: MutableList<FirTypeParameterRef> = mutableListOf()
        returnTypeRef = returnType
        //override var receiverParameter: FirReceiverParameter? = null
        deprecationsProvider = UnresolvedDeprecationProvider
        //override var containerSource: DeserializedContainerSource? = null
        //override var dispatchReceiverType: ConeSimpleKotlinType? = null
        //override val contextParameters: MutableList<FirValueParameter> = mutableListOf()
        //override val valueParameters: MutableList<FirValueParameter> = mutableListOf()
        //override var contractDescription: FirContractDescription? = null
        //override val annotations: MutableList<FirAnnotation> = mutableListOf()
        symbol = FirConstructorSymbol(classId)
        //override var delegatedConstructor: FirDelegatedConstructorCall? = null
        //override var body: FirBlock? = null
        deprecationsProvider = UnresolvedDeprecationProvider
    }
}