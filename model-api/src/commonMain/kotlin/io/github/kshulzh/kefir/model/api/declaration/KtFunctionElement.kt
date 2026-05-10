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

import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationsScope
import io.github.kshulzh.kefir.model.api.arg.KtParametersScope
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.modifiers.KtModifierScope
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.api.type.KtTypeParameterScope
import io.github.kshulzh.kefir.model.api.utils.KtVisitor

/**
 * Represents a Kotlin function element within the Kotlin model structure.
 *
 * A `KtFunctionElement` is a specific type of declaration element that defines
 * the structure of a function. It supports features such as specifying a body,
 * defining parameters, assigning a return type, and applying annotations or modifiers.
 * This interface provides a comprehensive view of a function's definition within
 * the model and enables systematic processing of functions in a Kotlin-based system.
 */
interface KtFunctionElement : KtDeclarationElement,
    KtParametersScope,
    KtModifierScope,
    KtAnnotationsScope,
    KtTypeParameterScope {
    /**
     * Represents the body of a function or declaration.
     *
     * This property holds an instance of [KtExpressionElement], representing the code block
     * or expression body associated with a function or declaration element. The body
     * can be null, indicating that there is no implementation or the body is omitted.
     *
     * The body is commonly used to define the behavior or logic of the containing
     * [KtFunctionElement]. It may include statements, expressions, or other constructs
     * that form the functional implementation.
     */
    var body: KtExpressionElement?

    /**
     * Represents the type information associated with a function element in the Kotlin model structure.
     *
     * This property is nullable and specifies the return type of the associated `KtFunctionElement` as a [KtTypeElement].
     * It can be used to define or retrieve the type declaration of the function, indicating what value the function
     * is expected to return. If null, it means that the function does not have an explicitly defined return type.
     */
    var type: KtTypeElement?

    override fun <R, D> accept(visitor: KtVisitor<R, D>, data: D): R = visitor.visitFunction(this, data)
}