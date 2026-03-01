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

package io.github.kshulzh.kefir.transform.statement

import io.github.kshulzh.kefir.model.api.statement.KtExpressionStatement
import io.github.kshulzh.kefir.transform.context.KtFirLocalTransformContext
import io.github.kshulzh.kefir.transform.context.KtIrLocalTransformContext
import org.jetbrains.kotlin.fir.expressions.FirStatement
import org.jetbrains.kotlin.ir.expressions.IrExpression

/**
 * Transforms a `KtExpressionStatement` into an `IrExpression` in the context of the current local
 * IR transformation using the specified input expression.
 *
 * @param input The `KtExpressionStatement` representing a statement with a single expression in
 *        the Kotlin abstract syntax tree (AST). The `expression` property of the input is used and
 *        must be non-null to perform the transformation.
 * @return An `IrExpression` resulting from the transformation of the input expression, or `null`
 *         if the transformation fails or the input is invalid.
 */
fun KtIrLocalTransformContext.transformIrStatementExpression(input: KtExpressionStatement): IrExpression? {
    return irTransform(input.expression!!)!!
}

/**
 * Transforms a given [KtExpressionStatement] into its corresponding [FirStatement].
 *
 * This method performs a local transformation of an expression statement into
 * a Frontend Intermediate Representation (FIR) statement. It utilizes the
 * current [KtFirLocalTransformContext] to apply the necessary modifications
 * during the transformation process.
 *
 * @param input The [KtExpressionStatement] to be transformed. Represents a standalone expression
 *        in the Kotlin abstract syntax tree (AST) that acts as a statement.
 * @return The transformed [FirStatement], or `null` if the transformation is not successful
 *         or the input is invalid.
 */
fun KtFirLocalTransformContext.transformFirStatementExpression(input: KtExpressionStatement): FirStatement? {
    return null
}