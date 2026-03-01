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

package io.github.kshulzh.kefir.builder.declaration

import io.github.kshulzh.kefir.builder.KefirDslMarker
import io.github.kshulzh.kefir.model.api.KtExternalElement
import io.github.kshulzh.kefir.model.api.declatation.KtClassElement
import io.github.kshulzh.kefir.model.api.declatation.KtDeclarationsScope
import io.github.kshulzh.kefir.model.api.declatation.findDeclaration
import io.github.kshulzh.kefir.model.declatation.KtClassElementImpl

/**
 * Creates or retrieves a class declaration within the current scope.
 *
 * @param name The name of the class to find or define.
 * @param init A lambda function to initialize the class if it is newly created.
 * @return An instance of [KtClassElement], either an existing or newly created class, or null if found in an external element.
 */
inline fun KtDeclarationsScope.Class(
    name: String,
    init: @KefirDslMarker KtClassElement.() -> Unit = {}
): KtClassElement? {
    return findDeclaration<KtClassElement>(name).firstOrNull().let { klass ->
        if (this is KtExternalElement) return klass
        klass?.also(init) ?: KtClassElementImpl(name, declarationsScope = this).also {
            it.init()
            declarations.add(it)
        }
    }
}