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
import io.github.kshulzh.kefir.model.api.utils.KtVisitor

/**
 * Represents the scope of a Kotlin package, providing operations for managing
 * and navigating through the elements contained within the package.
 *
 * The scope serves as a container for subpackages and file elements, enabling
 * hierarchical organization and retrieval of these elements by name.
 * It allows the creation of new subpackages and the navigation through an
 * existing package structure.
 */
interface KtPackageScope : KtElement {
    /**
     * A mutable set that contains all elements within the current package scope.
     *
     * This set includes instances of `KtPackageScopeElement`, allowing for
     * the representation and management of hierarchical packages and files
     * contained within the scope. Its elements may consist of:
     * - `KtPackageElement` for managing subpackages.
     * - `KtFileElement` for managing files within the package.
     *
     * The `packageElements` property is used to retrieve, filter, or modify
     * the elements defining the structure and content of the package scope.
     */
    val packageElements: MutableSet<KtPackageScopeElement>

    /**
     * Creates a new package within the current package scope and returns it.
     *
     * @param name The name of the package to create. It must be a valid `KtName` object representing the package.
     * @return The newly created package as a `KtPackageScope` object.
     */
    fun createPackage(name: KtName): KtPackageScope

    /**
     * Retrieves the package associated with the specified name within the current package scope.
     *
     * @param name the name of the package to be retrieved.
     * @return the package corresponding to the given name, or null if no such package exists.
     */
    fun getPackage(name: KtName): KtPackageScope? {
        return packageElements.filterIsInstance<KtPackageElement>().firstOrNull { it.name == name }
    }

    /**
     * Retrieves a `KtFileElement` from the current package scope, matching the provided name.
     *
     * @param name The name of the file to search for in the package scope.
     * @return The `KtFileElement` matching the provided name, or `null` if no such file exists.
     */
    fun getFile(name: KtName): KtFileElement? {
        return packageElements.filterIsInstance<KtFileElement>().firstOrNull { it.name == name }
    }

    override fun <R, D> accept(visitor: KtVisitor<R, D>, data: D): R = visitor.visitPackageScope(this, data)
}

/**
 * Retrieves an existing package with the specified name from the current scope, or creates
 * a new package if none exists. Ensures that the package is part of the current scope
 * after the operation.
 *
 * @param name The name of the package to retrieve or create.
 * @return The `KtPackageScope` representing the retrieved or newly created package.
 */
fun KtPackageScope.getOrCreatePackage(name: KtName): KtPackageScope {
    return getPackage(name)
        ?: createPackage(name)
}

/**
 * Retrieves or creates a series of nested `KtPackageScope` instances based on the given `KtPath`.
 * For each part of the `KtPath`, the corresponding package is either fetched
 * if it exists or created otherwise.
 *
 * @param path The hierarchical path representing the series of packages to retrieve or create.
 * @return The deepest `KtPackageScope` instance corresponding to the final part of the given path.
 */
fun KtPackageScope.getOrCreatePackage(path: KtPath): KtPackageScope {
    var current = this
    path.parts.forEach {
        if (it.isEmpty()) return@forEach
        current = current.getOrCreatePackage(it)
    }
    return current
}

