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
import io.github.kshulzh.kefir.model.api.KtPath
import io.github.kshulzh.kefir.model.api.declatation.KtClassElement
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
): KtPackageElement {
    return this.getOrCreatePackage(KtPath(path.split(".").toMutableList())).also(init) as KtPackageElement
}

/**
 * Creates or retrieves a class declaration within a specific file in the current package scope.
 * If the file exists, it delegates the class creation or retrieval to the file scope.
 * If the file does not exist, it creates a new file with the specified name, adds it to the package scope,
 * and attempts to create or retrieve the class within the new file.
 *
 * @param name The name of the class to create or retrieve.
 * @param init A DSL initialization block to configure the class if it is newly created.
 * @return An instance of [KtClassElement] representing the class, or null if it cannot be created or retrieved.
 */
inline fun KtPackageScope.Class(name: String, init: @KefirDslMarker KtClassElement.() -> Unit = {}): KtClassElement? {
    File("$name.kt") {
        return this.Class(name, init)
    }
    return null
}
