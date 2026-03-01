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

package io.github.kshulzh.kefir.model.io

import io.github.kshulzh.kefir.model.api.KtElement
import io.github.kshulzh.kefir.model.api.KtName
import io.github.kshulzh.kefir.model.api.io.KtPackageScope
import io.github.kshulzh.kefir.model.api.io.KtPackageScopeElement
import io.github.kshulzh.kefir.model.utils.createSetDelegate

/**
 * A concrete implementation of the `KtPackageScope` interface representing the root package element
 * within the package hierarchy.
 *
 * This class provides a mechanism to manage and navigate child package elements within the
 * root package scope. Hierarchical relationships between package elements are maintained,
 * and the class supports creation of new package scopes.
 *
 * Responsibilities:
 * - Containment and management of package elements through the `packageElements` property.
 * - Creation of new subpackages within the root package scope.
 *
 * @param packageElements An initial set of package elements contained within this root package scope.
 *                         Defaults to an empty mutable set if not provided.
 */
class KtRootPackageElementImpl(packageElements: MutableSet<KtPackageScopeElement> = mutableSetOf()) : KtPackageScope,
    KtElement {
    /**
     * Represents a mutable set of package scope elements contained within this package scope.
     *
     * This property holds a collection of `KtPackageScopeElement` elements, which may represent
     * either subpackages or files within the package's hierarchical structure. The set allows
     * modifying the structure by adding or removing elements while maintaining references
     * to the parent package.
     *
     * The delegation mechanism `createSetDelegate` ensures automatic updates to element-parent
     * relationships when the set is modified, facilitating consistent hierarchical organization
     * of the package elements.
     */
    override var packageElements: MutableSet<KtPackageScopeElement> by createSetDelegate(
        packageElements,
        KtPackageScopeElement::parent
    )

    /**
     * Creates a new package within the current package scope and adds it to the package elements.
     *
     * @param name The name of the package to create, represented as a `KtName` object.
     * @return The newly created package as an instance of `KtPackageScope`.
     */
    override fun createPackage(name: KtName): KtPackageScope {
        return KtPackageElementImpl(name, this).also { packageElements.add(it) }
    }

    override fun toString() = "<ROOT>"
}