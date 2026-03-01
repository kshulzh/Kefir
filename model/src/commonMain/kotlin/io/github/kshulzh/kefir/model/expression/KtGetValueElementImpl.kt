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
import io.github.kshulzh.kefir.model.api.arg.KtParameterElement
import io.github.kshulzh.kefir.model.api.expression.KtGetValueElement
import io.github.kshulzh.kefir.model.api.type.KtTypeElement

/**
 * Implementation of the [KtGetValueElement] interface, representing an element used to
 * retrieve the value of a parameter in the Kotlin model. This class also implements
 * [KtAttributes], allowing it to hold a collection of attributes as metadata.
 *
 * @property parameter Represents the parameter element associated with this value retrieval.
 * It may be null if the parameter is not set.
 *
 * @property type Represents the type information for the value element, if available.
 * This property holds a reference to a [KtTypeElement], which can define the type of the
 * associated value. It may be null if the type is not explicitly specified.
 *
 * @property parent Represents the parent element in the Kotlin model. This property
 * links the current element to its parent context, establishing hierarchical relationships
 * within the structure of a Kotlin model. It may be null if no parent is assigned.
 *
 * @property attributes A mutable map of attributes that can store metadata or additional
 * properties for this element. The keys are strings, representing attribute names, and
 * the values can be of any type. This property enables the dynamic association of custom
 * attributes with the element.
 */
class KtGetValueElementImpl(
    override var parameter: KtParameterElement?,
    override var type: KtTypeElement? = null,
    override var parent: KtElement? = null,
    override var attributes: MutableMap<String, Any> = mutableMapOf()
) : KtGetValueElement, KtAttributes