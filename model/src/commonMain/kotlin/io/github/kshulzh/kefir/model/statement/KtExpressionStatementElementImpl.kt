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
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.statement.KtExpressionStatement
import io.github.kshulzh.kefir.model.api.statement.KtStatementsScope
import io.github.kshulzh.kefir.model.utils.createNullableDelegate

/**
 * Implementation of a Kotlin expression statement element.
 *
 * This class represents an expression statement in the Kotlin abstract syntax tree (AST),
 * which consists of a single expression that acts as a standalone statement. It implements
 * both the [KtExpressionStatement] and [KtAttributes] interfaces to provide properties for
 * managing the encapsulated expression and associated metadata.
 *
 * @param expression The initial expression to be wrapped in this statement element. Can be null.
 * @param statementsScope The [KtStatementsScope] that encompasses this statement. Defaults to null.
 * @param attributes A mutable map of attributes associated with this element. Defaults to an empty map.
 */
class KtExpressionStatementElementImpl(
    expression: KtExpressionElement?,
    override var statementsScope: KtStatementsScope? = null,
    override var attributes: MutableMap<String, Any> = mutableMapOf()
) : KtExpressionStatement, KtAttributes {
    /**
     * Represents the expression associated with the enclosing expression statement element.
     *
     * This property refers to a [KtExpressionElement], which models a foundational component
     * of Kotlin's abstract syntax tree (AST) for expressions. It allows association of an
     * optional expression with the encompassing statement, supporting analysis and transformations
     * within the scope of the statement's context.
     *
     * The property is backed by a delegated implementation, enabling dynamic behavior for access
     * and modification. Through the delegation to the [KtExpressionElement.parent] property,
     * it maintains hierarchical relationships within the AST structure. The usage of `createDelegate`
     * ensures that updates to this property are observed and propagated correctly, preserving
     * consistency in derived states.
     *
     * Being nullable, it signifies that the expression may not always be present or initialized
     * in the statement, reflecting cases where the statement is under construction or in an
     * intermediate state.
     */
    override val expression: KtExpressionElement? by createNullableDelegate(expression, KtExpressionElement::parent)
}