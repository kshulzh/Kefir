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
 * Represents a scope containing argument elements in the Kotlin model.
 *
 * This interface defines a collection of `KtArgumentElement` instances that are associated
 * with a specific scope. It is used to model arguments in contexts such as function calls,
 * annotations, or any other elements that can define and pass arguments.
 */
interface KtArgumentsScope : KtElement {
    /**
     * Holds a mutable list of argument elements associated with a specific scope.
     * Each element in the list represents a single argument, which may include
     * its name, value, position, and its relationship to the arguments scope.
     *
     * This property is used to manage or traverse arguments within a context such as
     * a function call, annotation, or any other construct supporting arguments in the
     * Kotlin model.
     */
    var arguments: MutableList<KtArgumentElement>
}