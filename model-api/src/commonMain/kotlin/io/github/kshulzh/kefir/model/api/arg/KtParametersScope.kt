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

package io.github.kshulzh.kefir.model.api.arg

import io.github.kshulzh.kefir.model.api.KtElement

/**
 * Represents a scope containing parameter elements in the Kotlin model.
 *
 * This interface is used to define and manage a collection of `KtParameterElement` instances
 * associated with a specific scope. It is commonly used in contexts such as functions, constructors,
 * or other callable entities where parameters are defined and referenced.
 */
interface KtParametersScope : KtElement {
    /**
     * Holds a mutable list of parameter elements associated with this scope.
     *
     * Each element in the list represents an individual parameter in the Kotlin model,
     * describing characteristics such as name, type, default value, and its role in
     * the corresponding callable entity (e.g., function, constructor, or lambda).
     *
     * This property is used to define, manage, or traverse parameters within the context
     * of the enclosing `KtParametersScope`. Adding a parameter to this list typically
     * sets the `parametersScope` property of the parameter to this scope, establishing
     * a bidirectional relationship between the parameter and its container.
     */
    var parameters: MutableList<KtParameterElement>

    /**
     * Adds a parameter to the parameters scope and associates it with the current scope.
     *
     * @param parameter the parameter to be added to this parameters scope
     */
    fun addParameter(parameter: KtParameterElement) {
        parameter.parametersScope = this
        parameters.add(parameter)
    }
}