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

import io.github.kshulzh.kefir.model.api.KtName
import io.github.kshulzh.kefir.model.api.io.KtPackageElement
import io.github.kshulzh.kefir.model.api.io.KtPackageScope
import io.github.kshulzh.kefir.model.api.io.KtPackageScopeElement
import io.github.kshulzh.kefir.model.utils.createSetDelegate

/**
 * Represents the implementation of a Kotlin package element within the package hierarchy.
 *
 * This class serves as a concrete implementation of the `KtPackageElement` interface,
 * providing support for hierarchical package navigation and management of subpackages
 * and file elements within a package scope.
 *
 * It allows:
 * - Representation of a package with a specific name.
 * - A nullable reference to a parent package to establish hierarchical relationships.
 * - Management of child elements within the package using a mutable set.
 *
 * @param name The name of the current package, represented as a `KtName`.
 * @param parent The parent scope of this package, represented as a `KtPackageScope`. It can be null.
 * @param packageElements A mutable set of elements contained within this package.
 */
class KtPackageElementImpl(
    override var name: KtName,
    override var parent: KtPackageScope? = null,
    packageElements: MutableSet<KtPackageScopeElement> = mutableSetOf()
) : KtPackageElement {
    /**
     * Represents the set of elements contained within the current package scope.
     *
     * This property holds a mutable set of `KtPackageScopeElement` instances, which can represent
     * either subpackages or file elements within the scope. The set encapsulates the hierarchical
     * structure of the package, supporting operations like adding, removing, and navigating through
     * the elements.
     *
     * Changes to the elements are delegated using the `createSetDelegate` mechanism, enabling dynamic
     * updates and consistent management of parent relationships among the elements.
     */
    override var packageElements: MutableSet<KtPackageScopeElement> by createSetDelegate(
        packageElements,
        KtPackageScopeElement::parent
    )

    /**
     * Creates a new package within the current package scope and adds it to the package elements.
     *
     * @param name The name of the package to create, represented as a `KtName` object.
     * @return The newly created package as a `KtPackageScope` instance.
     */
    override fun createPackage(name: KtName): KtPackageScope {
        return KtPackageElementImpl(name, this).also { packageElements.add(it) }
    }

    override fun toString() = name
}