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

import io.github.kshulzh.kefir.model.api.KtElement
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationElement
import io.github.kshulzh.kefir.model.api.expression.KtConstElement
import io.github.kshulzh.kefir.model.api.type.KtBaseTypes
import io.github.kshulzh.kefir.model.api.type.KtTypeElement

/**
 * This class provides an implementation for the [KtConstElement] interface, representing
 * a constant element within a Kotlin abstract syntax tree (AST). It encapsulates
 * a generic value that can be resolved or evaluated at compile time and provides
 * associated type information.
 *
 * @param T The type of the constant value held by this element.
 *
 * @constructor Creates an instance of [KtConstElementImpl] with an initial value, type, and parent.
 * @property value The constant value associated with this element, stored as a nullable generic type.
 * @property parent The parent element in the AST hierarchy if applicable.
 */
class KtConstElementImpl<T>(
    override var value: T?,
    override val annotations: MutableList<KtAnnotationElement> = mutableListOf(),
    type: KtTypeElement? = null,
    override var parent: KtElement? = null,
) : KtConstElement<T> {
    /**
     * Holds the type information for the constant element.
     *
     * This variable stores an instance of [KtTypeElement]. If a specific type is already defined,
     * it is returned. Otherwise, the type is inferred based on the value of the constant element.
     *
     * The inferred type is mapped to the corresponding [KtBaseTypes] representation for common
     * data types, such as `Int`, `Long`, `Double`, `Float`, and `String`. If the value does not
     * match any of these known types, the type will be set to `null`.
     *
     * This variable is overridden for implementation in the context of managing constant elements
     * as part of the Kotlin type system.
     */
    override var type: KtTypeElement? = type
        get() = if (field != null) {
            field
        } else if (value != null) {
            when (value) {
                is Int -> KtBaseTypes.INT
                is Long -> KtBaseTypes.LONG
                is Double -> KtBaseTypes.DOUBLE
                is Float -> KtBaseTypes.FLOAT
                is String -> KtBaseTypes.STRING

                else -> null
            }

        } else {
            null
        }
}