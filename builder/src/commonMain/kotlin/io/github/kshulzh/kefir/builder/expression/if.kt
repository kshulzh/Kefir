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

package io.github.kshulzh.kefir.builder.expression

import io.github.kshulzh.kefir.builder.KefirDslMarker
import io.github.kshulzh.kefir.model.api.expression.KtBlockElement
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.expression.KtIfElement
import io.github.kshulzh.kefir.model.expression.KtBlockElementImpl
import io.github.kshulzh.kefir.model.expression.KtIfElementImpl

/**
 * Constructs an `if` expression element with a specified condition and body.
 *
 * @param condition the condition expression for the `if` statement
 * @param body a lambda defining the body of the `if` block
 */
inline fun If(condition: KtExpressionElement, body: @KefirDslMarker KtBlockElement.() -> Unit) =
    KtIfElementImpl(condition, KtBlockElementImpl().apply(body))

/**
 * Sets the `elseBody` of this `KtIfElementImpl` using the provided block of code.
 *
 * @param body a lambda function that defines the body of the `else` branch.
 *             The `KtBlockElement` receiver allows for the construction of the block content.
 * @return the modified `KtIfElement` with the `elseBody` set.
 */
inline infix fun KtIfElementImpl.Else(body: @KefirDslMarker KtBlockElement.() -> Unit): KtIfElement {
    this.elseBody = KtBlockElementImpl().apply(body)

    return this
}