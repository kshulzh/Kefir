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
import io.github.kshulzh.kefir.model.api.io.KtFileElement
import io.github.kshulzh.kefir.model.api.io.KtPackageScope
import io.github.kshulzh.kefir.model.declatation.KtFileElementImpl

/**
 * Creates or retrieves an existing `KtFileElement` with the given name in the current `KtPackageScope`.
 * If the file already exists, it initializes it using the provided lambda.
 * If the file doesn't exist, it is created, added to the package scope, and then initialized.
 *
 * @param name The name of the file to create or retrieve.
 * @param init A DSL initialization block to configure the file element.
 * @return The `KtFileElement` that was created or retrieved.
 */
inline fun KtPackageScope.File(name: String, init: @KefirDslMarker KtFileElement. () -> Unit = {}): KtFileElement {
    return getFile(name)?.also(init) ?: KtFileElementImpl(name, parent = this).also {
        it.init()
        packageElements.add(it)
    }
}