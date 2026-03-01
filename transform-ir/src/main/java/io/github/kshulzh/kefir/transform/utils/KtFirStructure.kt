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

package io.github.kshulzh.kefir.transform.utils

import org.jetbrains.kotlin.fir.declarations.FirCallableDeclaration
import org.jetbrains.kotlin.fir.declarations.FirClass
import org.jetbrains.kotlin.fir.declarations.FirFile

/**
 * Represents a structure in the context of the Kotlin FIR (Frontend Intermediate Representation) framework.
 * The `KtFirStructure` interface defines methods for adding various FIR elements such as files, classes,
 * and callable declarations into a structural representation.
 */
interface KtFirStructure {
    /**
     * Adds the specified FIR (Frontend Intermediate Representation) file to the current structure.
     *
     * @param firFile The FIR file to be added to the structure.
     */
    fun addFile(firFile: FirFile)

    /**
     * Adds a class represented by a [FirClass] instance to the current structure.
     *
     * @param firClass The [FirClass] instance representing the class to be added.
     */
    fun addClass(firClass: FirClass)

    /**
     * Adds a callable declaration to the current structure.
     *
     * @param firCallable The callable declaration to be added.
     * @param isProperty A boolean indicating whether the callable is a property. Defaults to `false`.
     */
    fun addCallable(firCallable: FirCallableDeclaration, isProperty: Boolean = false)
}