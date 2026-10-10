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

package io.github.kshulzh.kefir.model.fir.expression

import io.github.kshulzh.kefir.model.api.KtElement
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationElement
import io.github.kshulzh.kefir.model.api.expression.KtDoWhileElement
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.expression.KtLoopElement
import io.github.kshulzh.kefir.model.api.expression.KtWhileElement
import io.github.kshulzh.kefir.model.api.type.KtBaseTypes
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.transform.FirWrapper
import io.github.kshulzh.kefir.transform.context.KtFirTransformContext
import org.jetbrains.kotlin.fir.expressions.FirDoWhileLoop
import org.jetbrains.kotlin.fir.expressions.FirLoop
import org.jetbrains.kotlin.fir.expressions.FirWhileLoop

/**
 * Shared FIR-backed implementation of [KtLoopElement]. Read-only: [condition] and [body] are
 * wrapped through [wrapExpression], which does not support many FIR expressions yet.
 */
abstract class KtFirLoopElement<L : FirLoop>(
    final override val firElement: L,
    var transformFirContext: KtFirTransformContext,
    override var parent: KtElement? = null,
) : KtLoopElement, FirWrapper<L> {
    override var condition: KtExpressionElement
        get() = wrapExpression(firElement.condition, transformFirContext, this)
            ?: throw UnsupportedOperationException("Unsupported FIR condition ${firElement.condition}")
        set(_) = throw UnsupportedOperationException("Changing an existing FIR loop is not supported")

    override var body: KtExpressionElement?
        get() = wrapExpression(firElement.block, transformFirContext, this)
        set(_) = throw UnsupportedOperationException("Changing an existing FIR loop is not supported")

    override var label: String?
        get() = firElement.label?.name
        set(_) = throw UnsupportedOperationException("Changing an existing FIR loop is not supported")

    /** FIR loops are statements without a type, so they are modelled as `Unit`. */
    override var type: KtTypeElement?
        get() = KtBaseTypes.UNIT
        set(_) = throw UnsupportedOperationException("FIR loops have no type")

    override val annotations: MutableList<KtAnnotationElement> = mutableListOf()
}

/** FIR-backed `while` loop. */
class KtFirWhileElement(
    firElement: FirWhileLoop,
    transformFirContext: KtFirTransformContext,
    parent: KtElement? = null,
) : KtFirLoopElement<FirWhileLoop>(firElement, transformFirContext, parent), KtWhileElement

/** FIR-backed `do ... while` loop. */
class KtFirDoWhileElement(
    firElement: FirDoWhileLoop,
    transformFirContext: KtFirTransformContext,
    parent: KtElement? = null,
) : KtFirLoopElement<FirDoWhileLoop>(firElement, transformFirContext, parent), KtDoWhileElement
