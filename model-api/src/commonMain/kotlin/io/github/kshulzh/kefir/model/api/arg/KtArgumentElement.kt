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
import io.github.kshulzh.kefir.model.api.KtName
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement

/**
 * Represents an argument element in a Kotlin model.
 * Typically used to describe arguments within a scope, such as function calls or annotations.
 */
interface KtArgumentElement : KtElement {
    /**
     * Represents the name of an argument, which is optional and can either be present
     * or absent. This property is typically used to identify an argument within an
     * argument list or when mapping named arguments to their corresponding values.
     */
    var name: KtName?

    /**
     * Represents the value of an argument, which is an expression element.
     * Can be null to indicate the absence of a value for the argument.
     */
    var value: KtExpressionElement?

    /**
     * Represents the scope of arguments associated with a parent element implementing `KtArgumentElement`.
     * This property holds a reference to a `KtArgumentsScope` instance, which encapsulates a collection
     * of `KtArgumentElement` instances, enabling structured access to argument elements within
     * the corresponding scope.
     *
     * The `argumentsScope` can be used to manage or traverse arguments for elements that support
     * argument functionality in the Kotlin abstract representation.
     */
    var argumentsScope: KtArgumentsScope?

    /**
     * Represents the positional index of an argument within its containing arguments scope.
     *
     * This property is used to determine the sequential order of the argument in the list
     * of arguments associated with a particular `KtArgumentsScope`.
     */
    var position: Int
}