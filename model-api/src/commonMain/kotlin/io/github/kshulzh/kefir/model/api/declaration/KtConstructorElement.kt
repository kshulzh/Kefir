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

package io.github.kshulzh.kefir.model.api.declaration

import io.github.kshulzh.kefir.model.api.KtElement
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationsScope
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.modifiers.KtModifierScope
import io.github.kshulzh.kefir.model.api.type.KtTypeParameterScope
import io.github.kshulzh.kefir.model.api.utils.KtVisitor

/**
 * Represents a Kotlin constructor element within a Kotlin model structure.
 *
 * A `KtConstructorElement` is a specialized declaration element that combines characteristics
 * of a declaration, a modifier scope, and an expression element.
 * It provides a mechanism for defining the body of a constructor, which contains the initialization
 * logic for the class it belongs to.
 */
interface KtConstructorElement : KtElement, KtModifierScope, KtDeclarationElement, KtTypeParameterScope, KtAnnotationsScope {
    /**
     * Represents the body of a constructor or similar declaration, encapsulating
     * an expression or block of code associated with the element.
     *
     * This property holds an instance of [KtExpressionElement], which can include
     * additional information like its type or parent element. The body provides
     * the primary implementation or functionality of the enclosing element.
     */
    var body: KtExpressionElement?

    override fun <R, D> accept(visitor: KtVisitor<R, D>, data: D): R = visitor.visitConstructor(this, data)
}