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

package io.github.kshulzh.kefir.model.statement

import io.github.kshulzh.kefir.model.api.KtAttributes
import io.github.kshulzh.kefir.model.api.KtElement
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.statement.KtReturnStatementElement
import io.github.kshulzh.kefir.model.api.statement.KtStatementsScope
import io.github.kshulzh.kefir.model.utils.createNullableDelegate

/**
 * Implementation of a Kotlin return statement element.
 *
 * This class represents a return statement in the Kotlin abstract syntax tree (AST),
 * which includes an optional expression, a target, and additional properties. It implements
 * both the [KtReturnStatementElement] and [KtAttributes] interfaces to manage the returned
 * expression, target, scope, and associated attributes.
 *
 * @param expression The initial expression associated with the return statement. Can be null.
 * @param target The target element of the return statement, representing the destination for the control transfer. Defaults to null.
 * @param statementsScope The [KtStatementsScope] containing this return statement. Defaults to null.
 * @param attributes A mutable map of attributes associated with this element. Defaults to an empty map.
 */
class KtReturnElementImpl(
    expression: KtExpressionElement?,
    override var target: KtElement? = null,
    override var statementsScope: KtStatementsScope? = null,
    override var attributes: MutableMap<String, Any> = mutableMapOf(),
) : KtReturnStatementElement, KtAttributes {
    /**
     * Represents the expression associated with the `return` statement element in the Kotlin abstract syntax tree (AST).
     *
     * This property holds a [KtExpressionElement], representing the expression to be returned by the enclosing `return`
     * statement. It links the `return` statement to the specific expression being returned, enabling analysis and manipulation
     * within the AST.
     *
     * The property is backed by a delegated implementation that facilitates dynamic behavior for access and modification.
     * Using `createDelegate`, it ensures consistency between the expression and its parent element in the AST hierarchy
     * by delegating behavior to the [KtExpressionElement.parent] property. This delegation helps maintain the structural
     * and relational integrity of the AST.
     *
     * The expression is nullable, signifying cases where the `return` statement does not include an expression. This occurs
     * in scenarios such as `return` statements in functions with a `Unit` return type or incomplete syntax during construction.
     */
    override var expression: KtExpressionElement? by createNullableDelegate(expression, KtExpressionElement::parent)
}