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

@file:Suppress("FunctionName")

package io.github.kshulzh.kefir.builder.declaration

import io.github.kshulzh.kefir.builder.KefirDslMarker
import io.github.kshulzh.kefir.builder.type.Type
import io.github.kshulzh.kefir.model.api.arg.KtParameterElement
import io.github.kshulzh.kefir.model.api.declatation.KtClassElement
import io.github.kshulzh.kefir.model.api.declatation.KtConstructorElement
import io.github.kshulzh.kefir.model.api.declatation.KtDeclarationsScope
import io.github.kshulzh.kefir.model.api.expression.KtBlockElement
import io.github.kshulzh.kefir.model.declatation.KtConstructorElementImpl
import io.github.kshulzh.kefir.model.expression.KtBlockElementImpl

/**
 * Creates a new constructor declaration within the current scope.
 * In future will match by params
 *
 * @param parameters A mutable list of parameters to initialize the constructor with. Default is an empty mutable list.
 * @param init A lambda function to initialize the constructor element.
 * @return An instance of [KtConstructorElement], representing the newly created constructor.
 */
inline fun KtDeclarationsScope.Constructor(
    parameters: MutableList<KtParameterElement> = mutableListOf(),
    init: @KefirDslMarker KtConstructorElement.() -> Unit = {}
): KtConstructorElement {
    //todo create params matching
    return KtConstructorElementImpl(null, this).also {
        init(it)
        this.declarations.add(it)
    }
}

/**
 * Initializes the body of a [KtConstructorElement] using the provided configuration block.
 *
 * @param body A lambda function annotated with [KefirDslMarker] used to define the body of the constructor.
 *             The lambda receives a [KtBlockElement] as its receiver for configuring the body statements.
 * @return The updated [KtConstructorElement] with the newly configured body.
 */
inline infix fun KtConstructorElement.Body(body: @KefirDslMarker (KtBlockElement.() -> Unit) = {}): KtConstructorElement {
    return apply {
        this.body = KtBlockElementImpl(
            type = (this@Body.declarationsScope as? KtClassElement)?.Type(),
            parent = this,
        ).apply {
            this.body()
        }
    }
}