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

package io.github.kshulzh.kefir.model.api.statement

import io.github.kshulzh.kefir.model.api.KtElement
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.utils.KtVisitor

/**
 * Represents a `return` statement element in the Kotlin abstract syntax tree (AST).
 *
 * This interface extends [KtStatementElement] and models the structure and properties
 * of a `return` statement in Kotlin source code. It provides the ability to reference
 * and manipulate both the returned expression and the target symbol within the `return` statement.
 */
interface KtReturnStatementElement : KtStatementElement {
    /**
     * Represents an optional expression associated with a return statement element in the Kotlin abstract syntax tree (AST).
     *
     * This property holds a [KtExpressionElement], which defines the expression being returned by the containing
     * return statement. The expression can represent any valid Kotlin expression, such as a literal, function call,
     * or more complex constructs.
     *
     * It is nullable, indicating that the return statement may not always include an expression. For example, in
     * certain contexts, a return statement might be used without returning a specific value, such as in a function
     * with a `Unit` return type.
     */
    var expression: KtExpressionElement?

    /**
     * Represents the target element associated with this return statement.
     *
     * This property refers to a [KtElement] that acts as the target of the return operation,
     * typically representing the destination or context in the abstract syntax tree (AST)
     * where control is returned.
     *
     * The property is nullable, indicating that the target might not always be explicitly
     * defined or resolved in the context of this statement.
     */
    var target: KtElement?

    override fun <R, D> accept(visitor: KtVisitor<R, D>, data: D): R = visitor.visitReturnStatement(this, data)
}