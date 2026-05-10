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

@file:Suppress("FunctionName")

package io.github.kshulzh.kefir.builder.arg

import io.github.kshulzh.kefir.builder.type.Type
import io.github.kshulzh.kefir.model.api.arg.KtParameterElement
import io.github.kshulzh.kefir.model.api.arg.KtParametersScope
import io.github.kshulzh.kefir.model.api.declaration.KtClassElement
import io.github.kshulzh.kefir.model.api.declaration.KtFunctionElement
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.arg.KtParameterElementImpl

/**
 * Adds a parameter to the current scope if it does not already exist.
 *
 * @param name The name of the parameter to add or retrieve.
 * @param type The type of the parameter, represented by a [KtTypeElement]. Can be null if the type is not specified.
 * @param value The default value of the parameter, represented by a [KtExpressionElement]. Can be null if no default value is provided.
 * @param kind The kind of the parameter, defined by [KtParameterElement.Kind]. Defaults to [KtParameterElement.Kind.Regular].
 */
fun KtParametersScope.Param(
    name: String,
    type: KtTypeElement? = null,
    value: KtExpressionElement? = null,
    kind: KtParameterElement.Kind = KtParameterElement.Kind.Regular,
) = parameters.find {
    it.name == name
} ?: KtParameterElementImpl(name, type, value, kind = kind).also {
    addParameter(it)
}

/**
 * Adds a `DispatchReceiver` parameter with the name `<this>` to the current function's parameter scope.
 * The type of the parameter is determined by the enclosing class if it exists, using its resolved type.
 *
 * The `DispatchReceiver` parameter is typically used to represent an implicit or explicit `this`
 * reference within the enclosing context.
 *
 * The method resolves the parameter type by checking if the current scope is part of a
 * `KtFunctionElement` whose surrounding declarations belong to a `KtClassElement`. If a valid type
 * is found, it assigns it to the parameter; otherwise, the type remains null.
 *
 * The created parameter is appended to the parameter list of the current scope if a parameter with
 * the same name does not already exist.
 */
fun KtParametersScope.This() = Param(
    "<this>",
    ((this as? KtFunctionElement)?.declarationsScope as? KtClassElement)?.Type(),
    kind = KtParameterElement.Kind.DispatchReceiver
)

/**
 * Converts a string into a `KtParameterElement` with the specified type and a regular kind.
 *
 * @param type The type of the parameter as a `KtTypeElement`.
 * @return A new instance of `KtParameterElement` with the provided name and type, and kind set to `Regular`.
 */
infix fun String.As(type: KtTypeElement): KtParameterElement {
    return KtParameterElementImpl(
        this,
        type,
        kind = KtParameterElement.Kind.Regular,
    )
}

