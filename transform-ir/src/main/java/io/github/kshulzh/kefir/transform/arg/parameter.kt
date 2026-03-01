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

package io.github.kshulzh.kefir.transform.arg

import io.github.kshulzh.kefir.model.api.arg.KtParameterElement
import io.github.kshulzh.kefir.transform.context.KtFirLocalTransformContext
import io.github.kshulzh.kefir.transform.context.KtIrLocalTransformContext
import io.github.kshulzh.kefir.transform.utils.*
import io.github.kshulzh.kefir.transform.utils.type.resolveType
import org.jetbrains.kotlin.fir.declarations.*
import org.jetbrains.kotlin.fir.declarations.builder.buildValueParameter
import org.jetbrains.kotlin.fir.moduleData
import org.jetbrains.kotlin.fir.symbols.FirBasedSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirValueParameterSymbol
import org.jetbrains.kotlin.fir.toFirResolvedTypeRef
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrParameterKind
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.symbols.impl.IrValueParameterSymbolImpl


/**
 * Transforms a Kotlin parameter element into an IR (Intermediate Representation) value parameter.
 *
 * @param input The Kotlin parameter element to be transformed. It represents a parameter
 * in the original Kotlin code and contains metadata such as its name, type, and kind.
 * @return The transformed IR value parameter corresponding to the input Kotlin parameter
 * element, or null if the transformation could not be completed.
 */
fun KtIrLocalTransformContext.transformIrParameter(input: KtParameterElement): IrValueParameter? {
    val type = irTransform(input.resolveType()!!)!!
    return pluginContext.irFactory.createValueParameter(
        startOffset = UNDEFINED_OFFSET,
        endOffset = UNDEFINED_OFFSET,
        origin = IrDeclarationOrigin.DEFINED,
        name = input.name.transform(),
        type = type,
        isAssignable = false,
        varargElementType = null,
        symbol = IrValueParameterSymbolImpl(),
        kind = input.kind?.ir ?: IrParameterKind.Regular,
        isCrossinline = false,
        isNoinline = false,
        isHidden = false,
    ).linkIr(input)
}

/**
 * Transforms a Kotlin parameter element (`KtParameterElement`) into a FIR value parameter (`FirValueParameter`).
 *
 * @param input The Kotlin parameter element to be transformed.
 * @return The resulting FIR value parameter or null if it cannot be transformed.
 */
fun KtFirLocalTransformContext.transformFirParameter(input: KtParameterElement): FirValueParameter? {
    val containingDeclaration = input.parametersScope.getFirOrExternal<FirFunction>()?.symbol as FirBasedSymbol<*>
    return buildValueParameter {
        name = input.name.transform()
        source = createSource()
        resolvePhase = FirResolvePhase.BODY_RESOLVE
        moduleData = transformContext.firSession.moduleData
        origin = FirDeclarationOrigin.Source
        attributes = FirDeclarationAttributes()
        name = input.name.transform()
        symbol = FirValueParameterSymbol()
        containingDeclarationSymbol = containingDeclaration
        returnTypeRef = firTransform(input.type!!)!!.toFirResolvedTypeRef()
        //deprecationsProvider: DeprecationsProvider = UnresolvedDeprecationProvider
        //override val annotations: MutableList<FirAnnotation> = mutableListOf()
        //open var defaultValue: FirExpression? = null
        //open lateinit var containingDeclarationSymbol: FirBasedSymbol<*>
//        open var isCrossinline: Boolean = false
//        open var isNoinline: Boolean = false
//        open var isVararg: Boolean = false
        //open var valueParameterKind: FirValueParameterKind = FirValueParameterKind.Regular
    }.linkFir(input)
}


/**
 * Provides the IR representation of the kind of the parameter.
 *
 * The `ir` property maps the kind of a `KtParameterElement` to its corresponding
 * representation in the IR model (`IrParameterKind`).
 *
 * If the parameter kind is `Regular`, it maps to `IrParameterKind.Regular`.
 * If the parameter kind is `DispatchReceiver`, it maps to `IrParameterKind.DispatchReceiver`.
 *
 * This property is used in IR transformations to associate the appropriate representation
 * with a Kotlin parameter element during compilation or analysis.
 */
val KtParameterElement.Kind.ir
    get() = when (this) {
        KtParameterElement.Kind.Regular -> IrParameterKind.Regular
        KtParameterElement.Kind.DispatchReceiver -> IrParameterKind.DispatchReceiver
    }