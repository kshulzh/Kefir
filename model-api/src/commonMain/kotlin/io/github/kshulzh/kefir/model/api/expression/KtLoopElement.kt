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

/**
 * Common contract of loop expressions ([KtWhileElement], [KtDoWhileElement]).
 *
 * A loop evaluates [condition] and repeats [body]. The optional [label] names the loop
 * so that `break`/`continue` can target it. The loop [type] is `Unit` by default.
 */
interface KtLoopElement : KtExpressionElement {
    /** Boolean expression that decides whether the loop continues. */
    var condition: KtExpressionElement

    /** Loop body, `null` for an empty loop. */
    var body: KtExpressionElement?

    /** Optional loop label. */
    var label: String?
}
