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

package io.github.kshulzh.kefir.model.ir.expression

import io.github.kshulzh.kefir.model.api.KtElement
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.transform.context.KtTransformContext
import org.jetbrains.kotlin.ir.expressions.*

fun wrapExpression(
    irExpression: IrExpression,
    transformContext: KtTransformContext,
    parent: KtElement? = null
): KtExpressionElement? {
    return when (irExpression) {
        is IrBlock -> KtIrBlockElement(irExpression, transformContext, parent)
        is IrConst -> KtIrConstElement(irExpression, transformContext, parent)
        is IrGetField -> KtIrGetFieldElement(irExpression, transformContext, parent)
        is IrGetValue -> KtIrGetValueElement(irExpression, transformContext, parent)
        is IrSetField -> KtIrSetFieldElement(irExpression, transformContext, parent)
        //todo check else
        is IrWhen if irExpression.branches.size == 1 || irExpression.branches.size == 2 -> wrapExpression(
            irExpression.branches.first().result,
            transformContext,
            parent
        )


        else -> KtIrExpressionElement(irExpression, transformContext, parent)
    }
}