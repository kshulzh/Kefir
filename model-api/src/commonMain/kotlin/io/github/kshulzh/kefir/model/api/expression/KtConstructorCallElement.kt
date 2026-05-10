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

package io.github.kshulzh.kefir.model.api.expression

import io.github.kshulzh.kefir.model.api.arg.KtArgumentsScope
import io.github.kshulzh.kefir.model.api.declaration.KtConstructorElement
import io.github.kshulzh.kefir.model.api.utils.KtVisitor

/**
 * Represents a constructor call expression in the Kotlin abstract syntax tree (AST).
 *
 * This interface extends [KtExpressionElement], signifying that it is an expression
 * with type information and hierarchical placement within the AST. A `KtConstructorCallElement`
 * models a specific invocation of a constructor and provides properties for accessing
 * the invoked constructor and its arguments.
 */
interface KtConstructorCallElement : KtExpressionElement, KtArgumentsScope {
    /**
     * Represents the constructor element associated with the current instance of [KtConstructorCallElement].
     *
     * The `constructor` property refers to the instance of [KtConstructorElement] that defines
     * the initialization logic and metadata for the class being constructed within the containing
     * context. It combines declaration, modifier, and expression-related traits to facilitate
     * structural and functional representation of the class's constructor in the Kotlin model.
     *
     * This property is typically used to access details about the constructor itself, such as
     * its body, modifiers, or any related metadata.
     */
    val constructor: KtConstructorElement

    override fun <R, D> accept(visitor: KtVisitor<R, D>, data: D): R = visitor.visitConstructorCall(this, data)
}