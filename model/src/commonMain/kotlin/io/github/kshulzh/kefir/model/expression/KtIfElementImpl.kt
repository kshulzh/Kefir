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

package io.github.kshulzh.kefir.model.expression

import io.github.kshulzh.kefir.model.api.KtAttributes
import io.github.kshulzh.kefir.model.api.KtElement
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationElement
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.expression.KtIfElement
import io.github.kshulzh.kefir.model.api.type.KtBaseTypes
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.utils.createDelegate
import io.github.kshulzh.kefir.model.utils.createNullableDelegate

/**
 * Implementation of the [KtIfElement] interface, which represents a conditional `if` expression
 * within the Kotlin abstract syntax tree (AST).
 *
 * This class provides properties and functionality specific to an `if` statement, including:
 * - A [condition] that determines the execution path.
 * - An optional [ifBody] containing the expression(s) executed when the condition evaluates to `true`.
 * - An optional [elseBody] for the expressions executed when the condition evaluates to `false`.
 *
 * Inherits from [KtAttributes], allowing custom attributes to be associated with the element.
 * The [type] property denotes the type of the `if` expression, and it defaults to [KtBaseTypes.UNIT].
 *
 * This implementation supports delegation for its core properties ([condition], [ifBody], and [elseBody]),
 * enabling their association with parent elements in the AST hierarchy.
 */
class KtIfElementImpl(
    condition: KtExpressionElement,
    ifBody: KtExpressionElement? = null,
    elseBody: KtExpressionElement? = null,
    override var type: KtTypeElement? = KtBaseTypes.UNIT,
    override val annotations: MutableList<KtAnnotationElement> = mutableListOf(),
    override var parent: KtElement? = null,
    override var attributes: MutableMap<String, Any> = mutableMapOf()
) : KtIfElement, KtAttributes {
    /**
     * Represents the conditional expression for this element.
     *
     * The `condition` variable is a delegated property that holds a reference to a `KtExpressionElement`,
     * which serves as the conditional expression evaluating the logic for this element. This property
     * is assigned and managed using the `createDelegate` utility, ensuring synchronization with changes
     * in its parent context within the Kotlin abstract syntax tree (AST).
     *
     * Through its delegation, this property dynamically links with the `parent` of the associated
     * `KtExpressionElement`, maintaining the hierarchical positioning and structural integrity within
     * the AST. This behavior allows for seamless updates and traversal of the syntax tree when the
     * condition is modified or replaced.
     */
    override var condition: KtExpressionElement by createDelegate(condition, KtExpressionElement::parent)

    /**
     * Represents the body of the "if" branch in a conditional expression.
     *
     * This property holds an optional instance of [KtExpressionElement], representing
     * the primary block of code or expression executed when the condition in the "if"
     * statement evaluates to true.
     *
     * The body can be any valid Kotlin expression or block enclosed within the "if" branch.
     * When no explicit body is defined, this property is `null`.
     *
     * Delegation is used via the `createDelegate` method to manage the internal state
     * and ensure synchronization with the parent element hierarchy.
     */
    override var ifBody: KtExpressionElement? by createNullableDelegate(ifBody, KtExpressionElement::parent)

    /**
     * Represents the `else` branch body of an `if` statement in the Kotlin abstract syntax tree (AST).
     *
     * This property is a delegated variable that holds an optional instance of [KtExpressionElement],
     * representing the expression or block of expressions specific to the `else` branch. If the `if`
     * statement does not include an `else` block, this property will be `null`.
     *
     * The delegation mechanism ensures that updates to the `elseBody` property are reflected in the
     * surrounding AST structure by maintaining the parent-child relationship between elements.
     */
    override var elseBody: KtExpressionElement? by createNullableDelegate(elseBody, KtExpressionElement::parent)
}