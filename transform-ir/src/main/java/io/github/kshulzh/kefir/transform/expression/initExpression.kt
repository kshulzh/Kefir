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

package io.github.kshulzh.kefir.transform.expression

import io.github.kshulzh.kefir.ir.helper.KtIrInitExpressionElement
import io.github.kshulzh.kefir.transform.context.KtIrLocalTransformContext
import io.github.kshulzh.kefir.transform.utils.initialize
import io.github.kshulzh.kefir.transform.utils.linkIr
import org.jetbrains.kotlin.ir.expressions.IrExpression

/**
 * Transforms a `KtIrInitExpressionElement` into its corresponding IR (`Intermediate Representation`) expression.
 * The transformation process involves initializing the IR element and linking it to the provided Kotlin element.
 *
 * @param input The `KtIrInitExpressionElement` to be transformed. This represents an IR-backed expression element
 *              in the Kotlin tree, including initialization logic and associated metadata.
 * @return The transformed `IrExpression`, or `null` if the transformation fails.
 */
fun KtIrLocalTransformContext.transformIrInitExpression(input: KtIrInitExpressionElement): IrExpression? {
    return input.initialize().linkIr(input)
}