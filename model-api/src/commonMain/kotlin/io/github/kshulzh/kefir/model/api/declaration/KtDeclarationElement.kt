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

package io.github.kshulzh.kefir.model.api.declaration

import io.github.kshulzh.kefir.model.api.KtElement
import io.github.kshulzh.kefir.model.api.KtName
import io.github.kshulzh.kefir.model.api.KtPath
import io.github.kshulzh.kefir.model.api.io.KtFileElement
import io.github.kshulzh.kefir.model.api.utils.KtVisitor

/**
 * Represents a declaration element in the Kotlin model structure.
 *
 * A `KtDeclarationElement` serves as a base element for various kinds of
 * Kotlin declarations, such as classes, properties, fields, and functions.
 * It provides shared features like naming and scoping information,
 * and functionality to retrieve the path of the declaration within a file.
 */
interface KtDeclarationElement : KtElement {
    /**
     * Represents the scope of declarations for a declaration element.
     *
     * This property defines the hierarchical structure in which a declaration
     * element resides, allowing traversal or management of nested declarations.
     * It represents the direct containing scope for the declaration element
     * or null if no such scope exists. This is primarily used to establish
     * relationships between declaration elements within the Kotlin model structure.
     */
    var declarationsScope: KtDeclarationsScope?

    /**
     * Represents the name of the declaration element within the Kotlin model structure.
     *
     * This variable holds the identifier used to refer to the declaration, and is part
     * of the structural definition of all elements implementing the [KtDeclarationElement] interface.
     * The name helps uniquely identify and navigate between declaration elements in the model hierarchy.
     */
    var name: KtName

    /**
     * Retrieves the path to the file element and its associated hierarchical path within the scope of declarations.
     *
     * This method traverses up through the declaration scopes, starting from the current declaration,
     * until a `KtFileElement` is located. At each step, it collects the names of the encountered declaration elements,
     * forming a hierarchical path. The traversal stops when a `KtFileElement` is found or no further scope is available.
     *
     * @return A pair where the first element is the located `KtFileElement` (or `null` if none is found),
     * and the second element is the `KtPath` representing the reversed hierarchical path of names leading to the file.
     */
    fun getPathToFile(): Pair<KtFileElement?, KtPath> {
        val path = mutableListOf(name)
        var current: KtDeclarationsScope? = this.declarationsScope
        while (current != null && current !is KtFileElement) {
            if (current is KtDeclarationElement) {
                path.add(current.name)
                current = current.declarationsScope
            } else {
                current = null
            }
        }

        return current to KtPath(path.reversed().toMutableList())
    }

    override fun <R, D> accept(visitor: KtVisitor<R, D>, data: D): R = visitor.visitDeclaration(this, data)
}