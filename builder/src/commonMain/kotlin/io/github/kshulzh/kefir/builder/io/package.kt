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

@file:Suppress("FunctionName")

package io.github.kshulzh.kefir.builder.io

import io.github.kshulzh.kefir.builder.KefirDslMarker
import io.github.kshulzh.kefir.builder.declaration.Class
import io.github.kshulzh.kefir.builder.declaration.FindClass
import io.github.kshulzh.kefir.builder.declaration.NewClass
import io.github.kshulzh.kefir.model.api.KtPath
import io.github.kshulzh.kefir.model.api.declaration.KtClassElement
import io.github.kshulzh.kefir.model.api.io.KtFileElement
import io.github.kshulzh.kefir.model.api.io.KtPackageElement
import io.github.kshulzh.kefir.model.api.io.KtPackageScope
import io.github.kshulzh.kefir.model.api.io.getOrCreatePackage

/**
 * Creates or retrieves an existing `KtPackageElement` with the specified path in the current `KtPackageScope`.
 * If the package already exists, it initializes it using the provided lambda.
 * If the package does not exist, it is created and then initialized.
 *
 * @param path The dot-separated path of the package to create or retrieve.
 * @param init A DSL initialization block to configure the package element.
 * @return The `KtPackageElement` that was created or retrieved.
 */
inline fun KtPackageScope.Package(
    path: String,
    init: @KefirDslMarker KtPackageScope.() -> Unit = {}
): KtPackageScope {
    return Package(KtPath(path.split(".").toMutableList()), init)
}

/**
 * Creates or retrieves a nested package structure within the current `KtPackageScope`
 * based on the specified `KtPath`. If the path is empty, the current scope is returned.
 * Otherwise, it navigates through the path and initializes subpackages as needed.
 * Optionally, applies the given initialization block to the resulting package scope.
 *
 * @param path The `KtPath` representing the hierarchical structure of the package to create or retrieve.
 * @param init An optional DSL initialization block to configure the resulting `KtPackageScope`.
 * @return The `KtPackageScope` corresponding to the deepest package in the given `KtPath`.
 */
inline fun KtPackageScope.Package(
    path: KtPath,
    init: @KefirDslMarker KtPackageScope.() -> Unit = {}
): KtPackageScope{
    if (path.parts.isEmpty()) return this
    return this.getOrCreatePackage(path).also(init)
}

/**
 * Retrieves an existing class by name within the current package scope or creates a new one if it does not exist.
 *
 * If the named class is found, it is returned after applying the DSL initialization block.
 * If the class is not found, a new class is created using*/
inline fun KtPackageScope.Class(name: String, init: @KefirDslMarker KtClassElement.() -> Unit = {}): KtClassElement {
    return FindClass(name, init) ?: File(name).NewClass(name, init)
}

/**
 * Searches for a Kotlin class by its name within the current package scope and optionally
 * applies an initialization block to it if the class is found.
 *
 * The method first attempts to locate a corresponding file with the name `{name}.kt` in
 **/
inline fun KtPackageScope.FindClass(name: String, init: @KefirDslMarker KtClassElement.() -> Unit = {}): KtClassElement? {
    FindFile("$name.kt")?.FindClass(name, init)?.let { return it }

    if (packageElements.isEmpty()) return null
    return packageElements.filterIsInstance<KtFileElement>().firstNotNullOf { it.FindClass(name, init) }
}

/**
 * Creates or retrieves a `KtClassElement` within the current package scope based on the provided hierarchical path.
 * If the path contains multiple parts, this method recursively creates or accesses nested class elements.
 *
 * @param path The hierarchical path to the class, represented as a [KtPath].*/
inline fun KtPackageScope.Class(path: KtPath, init: @KefirDslMarker KtClassElement.() -> Unit = {}): KtClassElement {
    if (path.parts.isEmpty()) throw IllegalArgumentException("Path must contain at least one part")
    if (path.parts.size == 1) return Class(path.parts.first(), init)
    return Class(path.parts.first()).Class(path.dropFirst(), init)
}

/**
 * Finds a class within the current package scope based on a hierarchical [path].
 *
 * This function recursively resolves the path to locate the target class element.
 * If the path consists of multiple components, it navigates through nested packages
 * or classes*/
inline fun KtPackageScope.FindClass(path: KtPath, init: @KefirDslMarker KtClassElement.() -> Unit = {}): KtClassElement? {
    if (path.parts.isEmpty()) return null
    if (path.parts.size == 1) return FindClass(path.parts.first(), init)
    return FindClass(path.parts.first())?.FindClass(path.dropFirst(), init)
}

/**
 * Creates a new class element within a newly created Kotlin file in the current package scope.
 *
 * This function creates a new file with the specified name in the current package scope
 * and then initializes a new instance of [Kt*/
inline fun KtPackageScope.NewClass(name: String, init: @KefirDslMarker KtClassElement.() -> Unit = {}): KtClassElement? {
    NewFile("$name.kt") {
        return this.NewClass(name, init)
    }
    return null
}
