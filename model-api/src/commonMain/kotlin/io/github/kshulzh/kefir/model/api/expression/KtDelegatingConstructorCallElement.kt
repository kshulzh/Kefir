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
 * Represents a delegating constructor call element within the Kotlin abstract syntax tree (AST).
 *
 * A delegating constructor call is a specific expression element that invokes another constructor
 * within the same class or a parent class. It is typically used for initializing an object by
 * delegating to another constructor.
 *
 * This interface extends [KtExpressionElement], enabling it to include type information and
 * hierarchical positioning within the AST.
 */
interface KtDelegatingConstructorCallElement : KtExpressionElement, KtArgumentsScope {
    /**
     * Represents the constructor element associated with a delegating constructor call.
     *
     * This property provides access to the underlying Kotlin constructor element
     * defined within the current model structure. The constructor element encapsulates
     * initialization logic for the class it is a part of and combines the characteristics of
     * declaration, modifier scope, and expression elements.
     */
    val constructor: KtConstructorElement

    override fun <R, D> accept(visitor: KtVisitor<R, D>, data: D): R = visitor.visitDelegatingConstructorCall(this, data)
}