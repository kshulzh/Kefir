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
import io.github.kshulzh.kefir.model.api.type.KtBaseTypes
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.expression.KtBlockElementImpl
import io.github.kshulzh.kefir.model.expression.KtDoWhileElementImpl
import io.github.kshulzh.kefir.model.expression.KtWhileElementImpl

/**
 * Constructs `while (condition) { body }`.
 *
 * @param condition boolean expression checked before every iteration
 * @param label optional loop label
 * @param type loop type, `Unit` by default
 * @param body statements of the loop body
 */
inline fun While(
    condition: KtExpressionElement,
    label: String? = null,
    type: KtTypeElement? = KtBaseTypes.UNIT,
    body: @KefirDslMarker KtBlockElement.() -> Unit
) = KtWhileElementImpl(condition, KtBlockElementImpl(type = KtBaseTypes.UNIT).apply(body), label, type)

/**
 * Constructs `do { body } while (condition)`; the body runs at least once.
 *
 * @param condition boolean expression checked after every iteration
 * @param label optional loop label
 * @param type loop type, `Unit` by default
 * @param body statements of the loop body
 */
inline fun DoWhile(
    condition: KtExpressionElement,
    label: String? = null,
    type: KtTypeElement? = KtBaseTypes.UNIT,
    body: @KefirDslMarker KtBlockElement.() -> Unit
) = KtDoWhileElementImpl(condition, KtBlockElementImpl(type = KtBaseTypes.UNIT).apply(body), label, type)
