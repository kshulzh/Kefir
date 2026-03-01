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

package io.github.kshulzh.kefir.transform.model

import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.transform.context.KtIrLocalTransformContext
import io.github.kshulzh.kefir.transform.utils.getIr
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.IrBlock
import org.jetbrains.kotlin.ir.expressions.IrBody

/**
 * Replaces the body of the given `IrFunction` with the provided `KtExpressionElement`.
 *
 * @param expressionBody The new expression to set as the body of the function. If null,
 *                       the body will be cleared.
 * @return The updated `IrBody` of the function, or null if the body was cleared.
 */
context(c: KtIrLocalTransformContext)
fun IrFunction.replaceBody(expressionBody: KtExpressionElement?): IrBody? {
    if (expressionBody == null) {
        body = null
        return null
    }
    body = expressionBody.getIr() as? IrBody ?: c.irTransform(expressionBody)?.let { exp ->
        if (exp is IrBlock) {
            c.pluginContext.irFactory.createBlockBody(
                UNDEFINED_OFFSET,
                UNDEFINED_OFFSET
            ).also {
                it.statements.addAll(exp.statements)
            }
        } else {
            c.pluginContext.irFactory.createExpressionBody(UNDEFINED_OFFSET, UNDEFINED_OFFSET, exp)
        }
    }
    return body
}
