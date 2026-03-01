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

package io.github.kshulzh.kefir.model.api.expression

/**
 * Represents a constant expression element in the Kotlin abstract syntax tree (AST).
 * A constant expression refers to a value that is evaluated or resolved at compile time.
 *
 * This interface extends the functionality of [KtExpressionElement], allowing it to have
 * type information and hierarchical positioning within the AST.
 *
 * @param T The type of the constant value held by this element.
 */
interface KtConstElement<T> : KtExpressionElement {
    /**
     * Represents the value associated with a constant element.
     *
     * This can hold any value of a generic type (T) and is designed to be nullable.
     * It allows the storage of constant values such as strings, booleans, or null.
     */
    var value: T?
}