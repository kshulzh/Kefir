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

import io.github.kshulzh.kefir.ir.helper.KtIrInitStatementElement
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.statement.KtReturnStatementElement
import io.github.kshulzh.kefir.model.api.statement.KtStatementElement
import io.github.kshulzh.kefir.transform.context.KtFirLocalTransformContext
import io.github.kshulzh.kefir.transform.context.KtIrLocalTransformContext
import org.jetbrains.kotlin.fir.expressions.FirStatement
import org.jetbrains.kotlin.ir.IrStatement

/**
 * Transforms the given Kotlin statement element into its corresponding intermediate representation (IR) statement.
 *
 * Depending on the specific type of the input statement, this method delegates the transformation
 * to the appropriate `irTransform` function.
 *
 * @param input The Kotlin statement element to be transformed. This can be one of the following types:
 *              - [KtReturnStatementElement]: Represents a `return` statement in the Kotlin AST.
 *              - [KtExpressionStatement]: Represents a Kotlin expression as a standalone statement.
 *              - [KtIrInitStatementElement]: Represents an IR-backed statement in the Kotlin AST used
 *                for initialization.
 *
 * @return The corresponding [IrStatement] instance if the input type is supported and transformed successfully,
 *         otherwise `null` if the input type is not recognized or cannot be transformed.
 */
fun KtIrLocalTransformContext.transformIrStatement(input: KtStatementElement): IrStatement? {
    return when (input) {
        is KtReturnStatementElement -> irTransform(input)
        is KtIrInitStatementElement -> irTransform(input)
        is KtExpressionElement -> irTransform(input)
        else -> null
    }
}

fun KtFirLocalTransformContext.transformFirStatement(input: KtStatementElement): FirStatement? {
    return when (input) {
        is KtReturnStatementElement -> firTransform(input)
        else -> null
    }
}