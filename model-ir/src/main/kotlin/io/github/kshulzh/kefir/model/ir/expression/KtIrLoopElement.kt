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

package io.github.kshulzh.kefir.model.ir.expression

import io.github.kshulzh.kefir.model.api.KtElement
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationElement
import io.github.kshulzh.kefir.model.api.expression.KtDoWhileElement
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.expression.KtLoopElement
import io.github.kshulzh.kefir.model.api.expression.KtWhileElement
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.ir.type.wrapType
import io.github.kshulzh.kefir.model.utils.createLazyIr2
import io.github.kshulzh.kefir.transform.IrWrapper
import io.github.kshulzh.kefir.transform.context.KtTransformContext
import org.jetbrains.kotlin.ir.expressions.IrDoWhileLoop
import org.jetbrains.kotlin.ir.expressions.IrLoop
import org.jetbrains.kotlin.ir.expressions.IrWhileLoop

/**
 * Shared IR-backed implementation of [KtLoopElement]; writes to [condition], [body] and [label]
 * are applied to the wrapped [IrLoop].
 */
abstract class KtIrLoopElement<L : IrLoop>(
    final override val irElement: L,
    var transformContext: KtTransformContext,
    override var parent: KtElement? = null,
) : KtLoopElement, IrWrapper<L> {
    private var conditionDelegate: KtExpressionElement? by createLazyIr2(
        transformContext,
        { irElement.condition },
        { wrapIrExpression(it, transformContext, this) },
        property = KtExpressionElement::parent,
        onSet = { e -> e?.let { irTransform(it) }.also { irElement.condition = it!! } }
    )

    override var condition: KtExpressionElement
        get() = conditionDelegate!!
        set(value) {
            conditionDelegate = value
        }

    override var body: KtExpressionElement? by createLazyIr2(
        transformContext,
        { irElement.body },
        { it?.let { exp -> wrapIrExpression(exp, transformContext, this) } },
        property = KtExpressionElement::parent,
        onSet = { e -> e?.let { irTransform(it) }.also { irElement.body = it } }
    )

    override var label: String?
        get() = irElement.label
        set(value) {
            irElement.label = value
        }

    override var type: KtTypeElement?
        get() = wrapType(irElement.type, transformContext)
        set(_) = throw UnsupportedOperationException("Changing the type of an existing IR loop is not supported")

    override val annotations: MutableList<KtAnnotationElement> = mutableListOf()
}

/** IR-backed `while` loop. */
class KtIrWhileElement(
    irElement: IrWhileLoop,
    transformContext: KtTransformContext,
    parent: KtElement? = null,
) : KtIrLoopElement<IrWhileLoop>(irElement, transformContext, parent), KtWhileElement

/** IR-backed `do ... while` loop. */
class KtIrDoWhileElement(
    irElement: IrDoWhileLoop,
    transformContext: KtTransformContext,
    parent: KtElement? = null,
) : KtIrLoopElement<IrDoWhileLoop>(irElement, transformContext, parent), KtDoWhileElement
