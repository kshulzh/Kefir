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
import io.github.kshulzh.kefir.model.api.KtPath
import io.github.kshulzh.kefir.model.api.declaration.KtClassElement
import io.github.kshulzh.kefir.model.api.declaration.KtDeclarationsScope
import io.github.kshulzh.kefir.model.api.declaration.findDeclaration
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
): KtClassElement {
    return FindClass(name, init) ?: NewClass(name, init)
}

inline fun KtDeclarationsScope.FindClass(
    name: String,
    init: @KefirDslMarker KtClassElement.() -> Unit = {}
): KtClassElement? {
    return findDeclaration<KtClassElement>(name).firstOrNull()?.also { klass ->
        if (this !is KtExternalElement) klass.also(init)
    }
}

/**
 * Creates a new class element within the current declarations scope.
 *
 * This function initializes a new instance of [KtClassElement] with the specified name
 * and applies the given DSL initialization block to configure the class element. Once
 * created, the class element is*/
inline fun KtDeclarationsScope.NewClass(
    name: String,
    init: @KefirDslMarker KtClassElement.() -> Unit = {}
): KtClassElement {
    return KtClassElementImpl(name, declarationsScope = this).also {
        it.init()
        declarations.add(it)
    }
}


/**
 * Defines a class element in the current scope using a path and initialization function.
 * Creates or navigates through nested class elements based on the provided hierarchical path.
 *
 * @param path The hierarchical path represented as a [*/
inline fun KtDeclarationsScope.Class(
    path: KtPath,
    init: @KefirDslMarker KtClassElement.() -> Unit = {}
): KtClassElement {
    if (path.parts.isEmpty()) throw IllegalArgumentException("Path must contain at least one part")
    var klass: KtClassElement = Class(path.parts.first(), init)
    var temp = path.dropFirst()
    while (temp.parts.isNotEmpty()) {
        klass = klass.Class(temp.parts.first(), init)
        temp = temp.dropFirst()
    }

    return klass.also(init)
}

/**
 * Finds a class within the current declaration scope based on a hierarchical path.
 *
 * This function searches for a class element by traversing the provided [path], resolving each successive
 * part of the path within the nested class structure. If a valid class element corresponding to the
 */
inline fun KtDeclarationsScope.FindClass(
    path: KtPath,
    init: @KefirDslMarker KtClassElement.() -> Unit = {}
): KtClassElement? {
    if (path.parts.isEmpty()) return null
    var klass: KtClassElement = FindClass(path.parts.first(), init) ?: return null
    var temp = path.dropFirst()
    while (temp.parts.isNotEmpty()) {
        klass = klass.FindClass(temp.parts.first(), init) ?: return null
        temp = temp.dropFirst()

    }
    return klass.also(init)
}

/**
 * Creates a new `KtClassElement` within the current scope using the specified hierarchical path.
 *
 * For each segment*/
inline fun KtDeclarationsScope.NewClass(
    name: KtPath,
    init: @KefirDslMarker KtClassElement.() -> Unit = {}
): KtClassElement {
    if (name.parts.isEmpty()) throw IllegalArgumentException("Path must contain at least one part")
    var klass: KtClassElement = NewClass(name.parts.first(), init)
    var temp = name.dropFirst()
    while (temp.parts.isNotEmpty()) {
        klass = klass.NewClass(temp.parts.first(), init)
        temp = temp.dropFirst()
    }
    return klass
}