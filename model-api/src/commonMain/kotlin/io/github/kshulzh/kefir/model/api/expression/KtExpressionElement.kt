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

import io.github.kshulzh.kefir.model.api.KtElement
import io.github.kshulzh.kefir.model.api.type.KtTypeElement

/**
 * Represents a general expression element in the Kotlin abstract syntax tree (AST).
 * This interface acts as a foundational type for all complex and simple expressions
 * in the Kotlin model.
 *
 * As an extension of [KtElement], it provides essential structure and behavior for
 * expression elements, allowing them to have hierarchical placement within the AST.
 *
 * It is typically a base type for more specialized expression elements, enabling them
 * to support type information and reference their parent elements in the syntax tree.
 */
interface KtExpressionElement : KtElement {
    /**
     * Represents the type of the current expression element.
     *
     * This property refers to a [KtTypeElement] that provides type-related information
     * associated with the expression. It may be `null` if the expression does not have
     * an explicitly defined or inferred type.
     */
    var type: KtTypeElement?

    /**
     * Represents the parent element within the abstract syntax tree (AST) hierarchy.
     *
     * The `parent` property provides access to the hierarchical context of this element.
     * It refers to the enclosing [KtElement] that contains this element, enabling navigation
     * or traversal of the AST structure. This property is nullable, indicating that the
     * element may not always have a parent in cases such as root nodes or detached elements.
     */
    var parent: KtElement?
}