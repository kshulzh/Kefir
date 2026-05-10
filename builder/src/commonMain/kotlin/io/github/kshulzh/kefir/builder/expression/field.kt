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

package io.github.kshulzh.kefir.builder.expression

import io.github.kshulzh.kefir.builder.declaration.getField
import io.github.kshulzh.kefir.model.api.declaration.KtPropertyElement
import io.github.kshulzh.kefir.model.api.expression.KtBlockElement
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.expression.KtGetFieldElement
import io.github.kshulzh.kefir.model.api.expression.KtGetValueElement
import io.github.kshulzh.kefir.model.expression.KtGetFieldElementImpl
import io.github.kshulzh.kefir.model.expression.KtGetValueElementImpl
import io.github.kshulzh.kefir.model.expression.KtSetFieldElementImpl
import io.github.kshulzh.kefir.model.utils.resolveParam

/**
 * A type alias for `KtGetValueElement`.
 *
 * This alias simplifies the usage of `KtGetValueElement` by providing an alternative name.
 */
typealias KtThisValueElement = KtGetValueElement

/**
 * Converts a `KtGetFieldElement` to a `KtSetFieldElement` by associating it
 * with a specified field value expression element.
 *
 * @param expressionElement the value expression element to set in the field
 */
infix fun KtGetFieldElement.Set(expressionElement: KtExpressionElement) =
    KtSetFieldElementImpl(this.field, this.receiver, expressionElement)

/**
 * Represents a resolved `this` reference within a block element of the Kotlin abstract syntax tree (AST).
 *
 * This property attempts to resolve the `this` reference for the current block element using
 * the `resolveParam` method. If successful, it returns a `KtThisValueElement` wrapped in
 * a `KtGetValueElementImpl` instance, which provides type information and hierarchical
 * structure within the AST. If resolution fails, it returns `null`.
 */
val KtBlockElement.This: KtThisValueElement?
    get() = this.resolveParam("<this>")?.let {
        KtGetValueElementImpl(it, type, parent = this)
    }

/**
 * Provides access to the backing field of a property within a block element context.
 *
 * This property represents a `KtGetFieldElement` that retrieves the backing field
 * of the associated property, enabling field-specific operations or transformations
 * within the block element. It facilitates interaction with the property's field,
 * seamlessly connecting the element's context to the accessed field.
 *
 * The field returned is created or retrieved based on the property element's state.
 * This ensures consistent access to the field tied to the property, integrating
 * functionalities across the block and property scopes.
 */
context(p: KtPropertyElement)
val KtBlockElement.Field: KtGetFieldElement
    get() {
        return KtGetFieldElementImpl(p.getField(), This, p.type)
    }