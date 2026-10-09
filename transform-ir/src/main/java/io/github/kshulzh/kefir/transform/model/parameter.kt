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
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.expressions.IrBody
import org.jetbrains.kotlin.ir.expressions.IrExpressionBody

/**
 * Replaces the default value of an [IrValueParameter] with the provided expression body after transforming it into
 * an [IrBody]. If the given expression body is `null`, the default value will be set to `null` as well.
 *
 * @param expressionBody The Kotlin expression element to be transformed and set as the default value. If `null`, the
 *                       existing default value will be cleared.
 * @return The transformed [IrBody] representing the new default value, or `null` if the expression body is `null`.
 */
context(c: KtIrLocalTransformContext)
fun IrValueParameter.replaceDefaultValue(expressionBody: KtExpressionElement?): IrBody? {
    if (expressionBody == null) {
        defaultValue = null
        return null
    }
    defaultValue = expressionBody.getIr() as? IrExpressionBody ?: c.irTransform(expressionBody)?.let { exp ->
        c.pluginContext.irFactory.createExpressionBody(UNDEFINED_OFFSET, UNDEFINED_OFFSET, exp)
    }
    return defaultValue
}