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

package io.github.kshulzh.kefir.model.api.io

import io.github.kshulzh.kefir.model.api.KtElement
import io.github.kshulzh.kefir.model.api.KtName
import io.github.kshulzh.kefir.model.api.KtPath

/**
 * Represents an element within a Kotlin package scope.
 *
 * A `KtPackageScopeElement` serves as a constituent part of a package structure,
 * which could be either another package or a file. This interface facilitates
 * hierarchical organization and navigation within a package scope.
 *
 * A package scope element may have:
 * - A name that uniquely identifies it within its parent package.
 * - A parent package scope that provides the context for its classification
 *   and location within the hierarchy.
 */
interface KtPackageScopeElement : KtElement {
    /**
     * The name associated with this element in the package scope.
     *
     * Represents a unique identifier for the element within the package hierarchy,
     * such as a file name or subpackage name. This property facilitates
     * operations like retrieval, navigation, and management of elements
     * within the package scope.
     */
    var name: KtName

    /**
     * Represents the parent package scope for the current `KtPackageScopeElement`.
     *
     * This variable defines the hierarchical relationship between the current element
     * and its enclosing package scope. It allows navigation or reference to the outer,
     * containing package scope. The `parent` may be `null` if the current element
     * exists at the top level of the package hierarchy or is not associated with a
     * parent scope.
     */
    var parent: KtPackageScope?
}

/**
 * Returns the hierarchical path of the current `KtPackageScopeElement` as a `KtPath` object.
 *
 * The path is constructed by traversing through the parent elements of the
 * current `KtPackageScopeElement`, collecting their names in hierarchical
 * order starting from the root element.
 *
 * This property is computed lazily and represents the chain of package names
 * leading to the current element in reverse order, suitable for hierarchical
 * resolution and representation.
 */
val KtPackageScopeElement.path: KtPath
    get() {
        val result = mutableListOf<KtName>()
        var current = parent as? KtPackageScopeElement
        while (current != null) {
            result.add(current.name)
            current = current.parent as? KtPackageScopeElement
        }
        result.reverse()
        return KtPath(result)
    }