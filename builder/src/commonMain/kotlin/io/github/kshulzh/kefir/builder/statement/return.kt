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

@file:Suppress("FunctionName")

package io.github.kshulzh.kefir.builder.statement

import io.github.kshulzh.kefir.model.api.KtElement
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.statement.KtReturnStatementElement
import io.github.kshulzh.kefir.model.api.statement.KtStatementsScope
import io.github.kshulzh.kefir.model.statement.KtReturnElementImpl


/**
 * Creates a return statement with the specified expression and optional target element.
 * The created return statement is added to the statements list of the current scope.
 *
 * @param expression The [KtExpressionElement] that represents the return value of the return statement.
 * @param target The optional [KtElement] that represents the target of the return statement. Defaults to null.
 * @return An instance of [KtReturnStatementElement] representing the return statement.
 */
fun KtStatementsScope.Return(expression: KtExpressionElement, target: KtElement? = null): KtReturnStatementElement =
    Return1(expression, target).also {
        statements.add(it)
    }

/**
 * Creates a `KtReturnStatementElement` initialized with the given expression and optional target.
 *
 * @param expression the expression to be returned as part of the return statement
 * @param target an optional target element associated with the return statement, default is null
 * @return a `KtReturnStatementElement` instance initialized with the given parameters
 */
fun KtStatementsScope.Return1(expression: KtExpressionElement, target: KtElement? = null): KtReturnStatementElement =
    KtReturnElementImpl(expression, target, this)