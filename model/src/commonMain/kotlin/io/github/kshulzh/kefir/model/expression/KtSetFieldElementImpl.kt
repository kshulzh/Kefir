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
import io.github.kshulzh.kefir.model.api.declaration.KtFieldElement
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.expression.KtSetFieldElement
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.utils.createNullableDelegate

/**
 * Implementation of the [KtSetFieldElement] interface, representing a field-setting expression
 * in the Kotlin abstract syntax tree (AST).
 *
 * This class models the assignment of a value to a field, potentially including a receiver
 * expression or type information. It combines the functionality of [KtSetFieldElement] with
 * the ability to manage attributes as defined by the [KtAttributes] interface.
 *
 * @property field The field being set in the assignment, represented by a [KtFieldElement]. This
 * element specifies the field's type, value, and associated metadata.
 * @property type The type of the field-setting expression. This is an optional [KtTypeElement]
 * that describes the type information of the assignment expression itself.
 * @property parent The parent element within the abstract syntax tree (AST) hierarchy, which
 * provides context for this element.
 * @property attributes A mutable map of attributes associated with this element, allowing
 * dynamic storage and management of metadata or additional properties.
 */
class KtSetFieldElementImpl(
    override var field: KtFieldElement,
    receiver: KtExpressionElement?,
    value: KtExpressionElement?,
    override var type: KtTypeElement? = null,
    override val annotations: MutableList<KtAnnotationElement> = mutableListOf(),
    override var parent: KtElement? = null,
    override var attributes: MutableMap<String, Any> = mutableMapOf()
) : KtSetFieldElement, KtAttributes {
    /**
     * Represents the receiver element in the context of a `set field` expression within the Kotlin abstract syntax tree (AST).
     *
     * This property is an optional instance of [KtExpressionElement], which corresponds to the expression or construct
     * that provides context for the field being accessed or modified. It can represent elements such as qualifiers,
     * references, or other expressions acting as the receiver of the operation.
     *
     * Delegation is implemented using the `createDelegate` utility, which synchronizes the property with its associated
     * parent in the AST hierarchy. This ensures consistency and maintains the structural integrity of the AST during
     * modifications or updates to the receiver element.
     */
    override var receiver: KtExpressionElement? by createNullableDelegate(receiver, KtExpressionElement::parent)

    /**
     * Represents the value component of a `KtSetFieldElementImpl`.
     *
     * This property holds an optional instance of [KtExpressionElement],
     * representing the expression associated with the field's assignment.
     *
     * Delegation is used via the `createDelegate` function to enable dynamic
     * synchronization between this property and its parent element within
     * the Kotlin abstract syntax tree (AST). The delegation ensures that
     * structural changes to the AST surrounding this value are consistently
     * reflected in the hierarchy, maintaining integrity and proper linkage.
     *
     * If no explicit value is specified for the `KtSetFieldElementImpl`, this
     * property will contain `null`.
     */
    override var value: KtExpressionElement? by createNullableDelegate(value, KtExpressionElement::parent)
}