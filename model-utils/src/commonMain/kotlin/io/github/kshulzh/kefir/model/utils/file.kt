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

package io.github.kshulzh.kefir.model.utils

import io.github.kshulzh.kefir.model.api.io.KtFileElement
import io.github.kshulzh.kefir.model.api.io.KtPackageElement
import io.github.kshulzh.kefir.model.api.io.KtPackageScope

/**
 * Retrieves all package scopes within the current package scope, including nested packages.
 *
 * This method collects and returns a list of all package scopes by recursively
 * traversing the nested elements within the current scope. If the current scope
 * does not contain any package elements, it returns a list containing only the
 * current package scope itself.
 *
 * @return A list of [KtPackageScope] instances representing the current package scope
 *         and all nested package scopes.
 */
fun KtPackageScope.getPackages(): List<KtPackageScope> {
    return if (packageElements.isEmpty()) listOf(this) else (packageElements.filterIsInstance<KtPackageElement>()
        .flatMap { it.getPackages() } + this)
}

/**
 * Retrieves all file elements from the current package scope, including those
 * within nested package elements.
 *
 * This method iterates over the package elements within the current package scope,
 * identifies any instances of `KtPackageElement`, collects their respective file
 * elements recursively, and combines them with file elements directly defined
 * in the current package scope.
 *
 * @return A list of `KtFileElement` instances representing all files within
 *         the current package scope and any nested packages.
 */
fun KtPackageScope.getFiles(): List<KtFileElement> {
    return packageElements.filterIsInstance<KtPackageElement>().flatMap { it.getFiles() } +
            packageElements.filterIsInstance<KtFileElement>()
}