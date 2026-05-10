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
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationElement
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.statement.KtReturnStatementElement
import io.github.kshulzh.kefir.model.utils.createNullableDelegate

/**
 * Implementation of a return statement element in the Kotlin abstract syntax tree (AST).
 *
 * This class represents a concrete model for a `return` statement, encapsulating its
 * associated attributes such as the optional expression being returned, the target
 * element, the parent element, and other metadata. It implements the [KtReturnStatementElement]
 * interface, allowing it to participate in the abstract representations of Kotlin source code.
 *
 * @constructor Initializes a new instance of [KtReturnElementImpl] with the given
 * properties for the return expression, target, parent, and attributes.
 *
 * @param expression The expression associated with the return statement. It may represent
 * a variety of Kotlin expressions or be null if the return statement has no expression.
 * @param target The element that the return statement targets, typically a function
 * or lambda expression. Can be null if there is no explicit target.
 * @param parent The parent element in the abstract syntax tree (AST) hierarchy that encloses
 * this return element. Can be null if no parent is defined.
 * @param attributes A mutable map storing arbitrary attributes associated with this return
 * element, allowing additional metadata or properties to be dynamically attached.
 */
class KtReturnElementImpl(
    expression: KtExpressionElement?,
    override var target: KtElement? = null,
    override val annotations: MutableList<KtAnnotationElement> = mutableListOf(),
    override var parent: KtElement? = null,
    override var attributes: MutableMap<String, Any> = mutableMapOf(),
) : KtReturnStatementElement, KtAttributes {
    /**
     * Represents an optional expression within a return statement element.
     *
     * This property is a delegate-managed nullable [KtExpressionElement], which defines
     * the expression being returned by the containing return statement. The delegate
     * observes and manages changes to this property, delegating additional behavior
     * such as tracking or copying as specified.
     *
     * The expression can represent any valid Kotlin expression, such as a literal,
     * function call, or compound expression. It is nullable, indicating that the
     * return statement might not include an expression in cases like returning from
     * a `Unit` function or in other contexts where a value is omitted.
     */
    override var expression: KtExpressionElement? by createNullableDelegate(expression, KtExpressionElement::parent)
}