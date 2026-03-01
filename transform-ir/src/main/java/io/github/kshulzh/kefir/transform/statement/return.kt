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

package io.github.kshulzh.kefir.transform.statement

import io.github.kshulzh.kefir.model.api.statement.KtReturnStatementElement
import io.github.kshulzh.kefir.transform.context.KtFirLocalTransformContext
import io.github.kshulzh.kefir.transform.context.KtIrLocalTransformContext
import io.github.kshulzh.kefir.transform.utils.getIr
import io.github.kshulzh.kefir.transform.utils.linkIr
import io.github.kshulzh.kefir.transform.utils.target.resolveTarget
import org.jetbrains.kotlin.fir.expressions.FirReturnExpression
import org.jetbrains.kotlin.fir.expressions.builder.buildReturnExpression
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.IrReturn
import org.jetbrains.kotlin.ir.expressions.impl.IrReturnImpl

/**
 * Transforms a `KtReturnStatementElement` into an `IrReturn` object in the context
 * of the current local IR transformation.
 *
 * @param input The `KtReturnStatementElement` representing the return statement
 *        in the Kotlin abstract syntax tree (AST) to be transformed into its
 *        corresponding intermediate representation (IR).
 * @return An `IrReturn` object corresponding to the transformed return statement,
 *         or `null` if the transformation fails or the input is invalid.
 */
fun KtIrLocalTransformContext.transformIrReturn(input: KtReturnStatementElement): IrReturn? {
    val target = (input.resolveTarget()?.getIr<IrSimpleFunction>())!!.symbol
    val expression = irTransform(input.expression!!)!!
    return IrReturnImpl(
        startOffset = UNDEFINED_OFFSET,
        endOffset = UNDEFINED_OFFSET,
        type = pluginContext.irBuiltIns.nothingType,
        returnTargetSymbol = target,
        value = expression,
    ).linkIr(input)
}

/**
 * Transforms a `KtReturnStatementElement` into a `FirReturnExpression`.
 *
 * @param input The `KtReturnStatementElement` representing the return statement to be transformed.
 *              The `expression` property of the input is transformed into a FIR expression.
 * @return A `FirReturnExpression` resulting from the transformation, or `null` if the transformation fails.
 */
fun KtFirLocalTransformContext.transformFirReturn(input: KtReturnStatementElement): FirReturnExpression? {
    val expression = firTransform(input.expression!!)!!
    return buildReturnExpression {
        //this.target = target
        result = expression
    }
}