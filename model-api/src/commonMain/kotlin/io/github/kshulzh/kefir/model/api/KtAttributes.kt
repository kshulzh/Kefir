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

package io.github.kshulzh.kefir.model.api

/**
 * Represents an element that supports a collection of attributes represented as a mutable map.
 * Extends the core [KtElement] interface to include attribute-related functionality.
 */
interface KtAttributes : KtElement {
    /**
     * A mutable map used to store attributes associated with the implementing element.
     * The keys are strings representing attribute names, while the values can be of any type.
     * This property provides a mechanism to associate and manage custom metadata or properties dynamically.
     */
    var attributes: MutableMap<String, Any>
}

/**
 * A mutable map that stores external attributes for elements implementing [KtElement].
 * The keys represent elements, while the values are mutable maps containing attribute key-value pairs.
 * This map provides a mechanism to associate attributes with elements that do not directly support attributes.
 */
private val externalAttributes: MutableMap<Any, MutableMap<String, Any>> = mutableMapOf()

/**
 * A property to manage attributes associated with a `KtElement`.
 *
 * - If the element implements `KtAttributes`, it accesses or modifies the internal `attributes` map.
 * - Otherwise, it fetches or initializes an external attributes map specifically for the element.
 *
 * This provides a unified way to handle attributes for both `KtAttributes` and other `KtElement` implementations.
 */
val KtElement.attributes: MutableMap<String, Any>
    get() = if (this is KtAttributes) attributes else externalAttributes.getOrPut(
        this
    ) { mutableMapOf() }