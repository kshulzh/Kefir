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

package io.github.kshulzh.kefir.model.api.expression

import io.github.kshulzh.kefir.model.api.utils.KtVisitor

/**
 * Represents a `do body while (condition)` loop: the body always runs at least once,
 * the condition is checked after every iteration.
 */
interface KtDoWhileElement : KtLoopElement {
    override fun <R, D> accept(visitor: KtVisitor<R, D>, data: D): R = visitor.visitDoWhile(this, data)
}
