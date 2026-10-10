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

package io.github.kshulzh.kefir.model.fir.expression

import io.github.kshulzh.kefir.model.api.KtElement
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.expression.KtLoopElement
import io.github.kshulzh.kefir.transform.context.KtFirTransformContext
import org.jetbrains.kotlin.fir.expressions.FirExpression
import org.jetbrains.kotlin.fir.expressions.FirDoWhileLoop
import org.jetbrains.kotlin.fir.expressions.FirLoop
import org.jetbrains.kotlin.fir.expressions.FirWhileLoop

fun wrapExpression(
    expression: FirExpression,
    transformContext: KtFirTransformContext,
    parent: KtElement? = null
): KtExpressionElement? {
    return when (expression) {

        else -> null
    }
}

/** FIR loops are statements, not [FirExpression]s, so they are wrapped separately. */
fun wrapLoop(
    loop: FirLoop,
    transformContext: KtFirTransformContext,
    parent: KtElement? = null
): KtLoopElement? {
    return when (loop) {
        is FirWhileLoop -> KtFirWhileElement(loop, transformContext, parent)
        is FirDoWhileLoop -> KtFirDoWhileElement(loop, transformContext, parent)
        else -> null
    }
}
