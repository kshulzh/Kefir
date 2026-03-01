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

package io.github.kshulzh.kefir.transform.utils

import org.jetbrains.kotlin.fir.backend.Fir2IrClassifierStorage
import org.jetbrains.kotlin.fir.backend.Fir2IrComponentsStorage
import org.jetbrains.kotlin.fir.backend.FirMetadataSource
import org.jetbrains.kotlin.fir.declarations.FirRegularClass
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol

/**
 * A structure for managing Intermediate Representation (IR) elements and their mappings for classes
 * during the transformation process.
 *
 * This class is designed to provide utilities for linking IR classes with their FIR (Frontend Intermediate Representation)
 * counterparts. It interacts directly with the internal caches of the transformation components to maintain the necessary
 * mappings between FIR and IR representations.
 *
 * @property fir2IrComponents A storage object holding the components required for FIR-to-IR transformation,
 * used to access and manipulate the classifier storage internally.
 */
class KtIrStructure(
    val fir2IrComponents: Fir2IrComponentsStorage
) {
    /**
     * Adds an `IrClass` to a cache within the `Fir2IrComponentsStorage` associated with this instance,
     * linking it to the corresponding FIR representation.
     *
     * This method retrieves metadata from the provided `IrClass` and updates a private cache,
     * mapping a `FirRegularClass` to its associated `IrClassSymbol`.
     * The cache is accessed reflectively within the `Fir2IrClassifierStorage`.
     *
     * @param irClass The `IrClass` to be added to the cache. This class contains metadata
     *                that links it to its corresponding FIR representation.
     */
    fun addClass(irClass: IrClass) {
        //reflection is bad, but why not?
        val fir = irClass.metadata as? FirMetadataSource.Class
        if (fir != null) {
            Fir2IrClassifierStorage::class.java.getDeclaredField("classCache").also {
                it.isAccessible = true
                @Suppress("UNCHECKED_CAST")
                val cache = it.get(fir2IrComponents.classifierStorage) as MutableMap<FirRegularClass, IrClassSymbol>
                cache[fir.fir as FirRegularClass] = irClass.symbol
            }
        }
    }
}