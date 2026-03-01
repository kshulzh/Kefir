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
import io.github.kshulzh.kefir.model.api.declatation.KtFieldElement
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.expression.KtGetFieldElement
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.utils.createNullableDelegate

/**
 * An implementation of the [KtGetFieldElement] interface representing an expression that retrieves
 * the value of a specified field within the Kotlin Abstract Syntax Tree (AST).
 *
 * This class models field access operations, including the relationship between the accessed field and
 * its potential receiver expression. It integrates type and parent information, as well as a flexible
 * attribute system for managing metadata dynamically.
 *
 * @property field Represents the field element ([KtFieldElement]) that is being accessed.
 * This property provides direct access to the associated field in the current scope of the syntax tree.
 *
 * @property receiver Describes the optional receiver expression ([KtExpressionElement]) that provides the
 * context for the field access. It can be null if no explicit receiver is required.
 *
 * @property type Specifies type information ([KtTypeElement]) associated with the field access expression,
 * or null in cases where type inference or absence of explicit typing applies.
 *
 * @property parent Represents the parent [KtElement] within the AST hierarchy. This relationship
 * contextualizes the current field access expression within the broader structure of the syntax tree.
 *
 * @property attributes A mutable map containing custom metadata or properties associated with
 * the element. This enables the integration of dynamic attributes into the syntax element as part
 * of the [KtAttributes] implementation.
 */
class KtGetFieldElementImpl(
    override var field: KtFieldElement,
    receiver: KtExpressionElement?,
    override var type: KtTypeElement?,
    override var parent: KtElement? = null,
    override var attributes: MutableMap<String, Any> = mutableMapOf()
) : KtGetFieldElement, KtAttributes {
    /**
     * Represents the receiver expression associated with this field access element.
     *
     * The `receiver` property references a [KtExpressionElement] that provides the context
     * or scope required for accessing the field. It may be `null` if the field is accessed
     * without an explicit receiver (e.g., within the same object or scope).
     *
     * This property leverages a delegated mechanism for managing its value and maintaining
     * a reference to the parent element in the abstract syntax tree (AST). Any updates to
     * the property will ensure consistency with the hierarchical structure of the AST.
     */
    override var receiver: KtExpressionElement? by createNullableDelegate(receiver, KtExpressionElement::parent)
}