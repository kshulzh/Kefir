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

package io.github.kshulzh.kefir.model.api.expression

import io.github.kshulzh.kefir.model.api.utils.KtVisitor

/**
 * Represents an `if` expression element within the Kotlin abstract syntax tree (AST).
 *
 * A `KtIfElement` models a conditional branching structure where an execution path
 * is determined based on a specified condition. It extends the functionality of
 * [KtExpressionElement], inheriting its properties for type and hierarchical AST placement.
 *
 * The `if` element consists of:
 * - A condition that evaluates to a boolean result.
 * - An optional body to execute if the condition is true.
 * - An optional body to execute if the condition is false (else branch).
 */
interface KtIfElement : KtExpressionElement {
    /**
     * Represents the condition expression associated with the element.
     *
     * The `condition` is an instance of [KtExpressionElement] and typically determines the logical or
     * structural decision-making for the containing element. For example, in the context of an
     * `if` statement ([KtIfElement]), the `condition` serves as the expression evaluated to decide
     * the execution path (e.g., `if (condition) ...`).
     *
     * This property plays a critical role in flow control constructs and often guides the program
     * behavior or branching logic within a Kotlin-based abstract syntax tree (AST).
     */
    var condition: KtExpressionElement

    /**
     * Represents the expression executed when the condition of an `if` statement evaluates to `true`.
     *
     * This property holds a reference to a [KtExpressionElement] that forms the body of the `if` block.
     * The content may include a single expression, a block of statements, or be `null` if the `if` body
     * is absent or not explicitly defined.
     *
     * As part of the abstract syntax tree (AST), `ifBody` provides access to the structural and type
     * information of the expression or block associated with the `if` statement's true branch.
     */
    var ifBody: KtExpressionElement?

    /**
     * Represents the else branch of an `if` expression in the Kotlin abstract syntax tree (AST).
     *
     * This property holds a nullable [KtExpressionElement] that describes the block or expression
     * executed when the condition of the associated `if` statement evaluates to `false`.
     *
     * It may contain a standalone expression or a block of statements, depending on the structure
     * of the `if-else` construct. If no else branch is present, this property will be `null`.
     */
    var elseBody: KtExpressionElement?

    override fun <R, D> accept(visitor: KtVisitor<R, D>, data: D): R = visitor.visitIf(this, data)
}