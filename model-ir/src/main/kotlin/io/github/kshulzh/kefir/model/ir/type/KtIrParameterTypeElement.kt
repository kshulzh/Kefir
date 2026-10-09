/*
 * Copyright (c) 2026. Kirill Shulzhenko
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

package io.github.kshulzh.kefir.model.ir.type

import io.github.kshulzh.kefir.model.api.type.KtParameterTypeElement
import io.github.kshulzh.kefir.model.api.type.KtTypeParameterElement
import io.github.kshulzh.kefir.transform.IrWrapper
import org.jetbrains.kotlin.ir.symbols.IrTypeParameterSymbol
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.types.IrSimpleType

/**
 * Represents a Kotlin parameter type element that wraps an IR (Intermediate Representation) simple type
 * and provides access to its characteristics within the transformation or analysis context.
 *
 * This class bridges the IR representation with the Kotlin type system, allowing interactions and analysis
 * of type parameters at a higher abstraction level.
 *
 * @constructor Creates an instance of [KtIrParameterTypeElement] with the specified IR simple type.
 * @param irElement The underlying [IrSimpleType] instance that this class wraps.
 */
class KtIrParameterTypeElement(
    override val irElement: IrSimpleType
) : KtParameterTypeElement,IrWrapper<IrSimpleType> {
    /**
     * Represents the type parameter element associated with the current `IrSimpleType` instance.
     *
     * The value is lazily initialized and resolved from the `IrTypeParameterSymbol` classifier
     * associated with the `irElement`. This property provides a reference to the wrapped
     * `KtTypeParameterElement` object for the underlying `IrTypeParameter`.
     *
     * @return The corresponding `KtTypeParameterElement`, or `null` if not available.
     */
    @OptIn(UnsafeDuringIrConstructionAPI::class)
    override val parameterType: KtTypeParameterElement? by lazy {
        val symbol = irElement.classifier as IrTypeParameterSymbol
        symbol.owner.kefir
    }
    /**
     * Indicates whether the type represented by this element is nullable.
     *
     * A type is considered nullable if it can hold the value `null`.
     * This property is determined based on the type information
     * found in the associated IR (Intermediate Representation) element.
     */
    override val isNullable: Boolean
        get() = TODO("Not yet implemented")
}