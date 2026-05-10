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

package io.github.kshulzh.kefir.model.api.statement

import io.github.kshulzh.kefir.model.api.KtElement
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationsScope
import io.github.kshulzh.kefir.model.api.utils.KtVisitor

/**
 * Defines a base element representing a statement in the Kotlin abstract syntax tree (AST).
 *
 * This interface extends [KtElement] and serves as a foundation for all specific statement types
 * in Kotlin. It models the shared structure and behaviors of statements, making it possible to
 * handle diverse statement forms in a uniform manner.
 */
interface KtStatementElement : KtAnnotationsScope, KtElement {
    /**
     * Represents the parent element within the abstract syntax tree (AST) hierarchy.
     *
     * The `parent` property provides access to the hierarchical context of this element.
     * It refers to the enclosing [KtElement] that contains this element, enabling navigation
     * or traversal of the AST structure. This property is nullable, indicating that the
     * element may not always have a parent in cases such as root nodes or detached elements.
     */
    var parent: KtElement?

    override fun <R, D> accept(visitor: KtVisitor<R, D>, data: D): R = visitor.visitStatement(this, data)
}