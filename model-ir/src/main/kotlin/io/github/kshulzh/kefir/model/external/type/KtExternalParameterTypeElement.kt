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

package io.github.kshulzh.kefir.model.external.type

import io.github.kshulzh.kefir.model.api.type.KtParameterTypeElement
import io.github.kshulzh.kefir.model.api.type.KtTypeParameterElement
import io.github.kshulzh.kefir.model.external.KtExternalRootPackageElement
import io.github.kshulzh.kefir.transform.FirWrapper
import io.github.kshulzh.kefir.transform.IrWrapper
import org.jetbrains.kotlin.fir.declarations.FirTypeParameterRef
import org.jetbrains.kotlin.fir.scopes.impl.toConeType
import org.jetbrains.kotlin.fir.symbols.SymbolInternals
import org.jetbrains.kotlin.fir.types.ConeTypeParameterType
import org.jetbrains.kotlin.ir.symbols.IrTypeParameterSymbol
import org.jetbrains.kotlin.ir.types.IrSimpleType

/**
 * Represents an external parameter type element within the Kotlin FIR-based model.
 *
 * This class acts as a wrapper around the underlying FIR representation for type parameters
 * and provides an abstraction for interacting with type parameter elements in external Kotlin packages.
 *
 * @constructor Creates an instance of `KtExternalParameterTypeElement`.
 * @param firElement The underlying FIR type parameter element that this object wraps.
 * @param root The root package element associated with the external type element.
 *
 * This class implements:
 * - `KtParameterTypeElement`: Defines the structure and behavior of a parameterized type element.
 * - `FirWrapper`: Provides access to the FIR representation of the type parameter.
 */
class KtExternalParameterTypeElement(
    override val firElement: ConeTypeParameterType,
    val root: KtExternalRootPackageElement
) : KtParameterTypeElement, FirWrapper<ConeTypeParameterType> {
    /**
     * Represents the type parameter element corresponding to the `firElement` symbol in the Kotlin FIR model.
     * This property provides the `KtTypeParameterElement` associated with the `firElement`
     * by resolving it from the `typeParams` map.
     *
     * The resolution uses the `firElement.lookupTag.typeParameterSymbol.fir` key to retrieve the proper element.
     * Returns `null` if no corresponding type parameter is found.
     */
    @OptIn(SymbolInternals::class)
    override val parameterType: KtTypeParameterElement? by lazy {
        typeParams[firElement.lookupTag.typeParameterSymbol.fir]
    }
    /**
     * Indicates whether the represented type is nullable.
     *
     * This property determines if the type may represent a null value at runtime.
     * The value `true` means the type is nullable, while `false` means the type is non-nullable.
     */
    override val isNullable: Boolean
        get() = TODO("Not yet implemented")

}