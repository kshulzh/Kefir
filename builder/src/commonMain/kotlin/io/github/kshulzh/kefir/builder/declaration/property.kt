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

package io.github.kshulzh.kefir.builder.declaration

import io.github.kshulzh.kefir.builder.KefirDslMarker
import io.github.kshulzh.kefir.builder.arg.Param
import io.github.kshulzh.kefir.builder.arg.This
import io.github.kshulzh.kefir.builder.expression.Field
import io.github.kshulzh.kefir.builder.expression.Set
import io.github.kshulzh.kefir.builder.expression.Variable
import io.github.kshulzh.kefir.builder.statement.Return
import io.github.kshulzh.kefir.model.api.declaration.KtDeclarationsScope
import io.github.kshulzh.kefir.model.api.declaration.KtFieldElement
import io.github.kshulzh.kefir.model.api.declaration.KtFunctionElement
import io.github.kshulzh.kefir.model.api.declaration.KtPropertyElement
import io.github.kshulzh.kefir.model.api.expression.KtBlockElement
import io.github.kshulzh.kefir.model.api.type.KtBaseTypes
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.declatation.KtFieldElementImpl
import io.github.kshulzh.kefir.model.declatation.KtFunctionElementImpl
import io.github.kshulzh.kefir.model.declatation.KtPropertyElementImpl

/**
 * Creates or retrieves a property declaration within the current scope.
 *
 * @param name The name of the property to find or define.
 * @param type The type of the property.
 * @param init A lambda function to initialize the variable if it is newly created. Defaults to an empty initializer.
 * @return An instance of [KtPropertyElement], either an existing or a newly created variable.
 */
//todo add also val option
fun KtDeclarationsScope.Var(
    name: String,
    type: KtTypeElement,
    init: KtPropertyElement.() -> Unit = {}
): KtPropertyElement {
    return declarations.filterIsInstance<KtPropertyElement>().find { it.name == name }?.also(init)
        ?: KtPropertyElementImpl(name, type, this).also(init)
            .also {
                declarations.add(it)
            }
}

/**
 * Sets or updates the type of the current `KtPropertyElement` instance with the specified `KtTypeElement`.
 *
 * @param type The `KtTypeElement` to be assigned to the property element.
 * @return The current instance of `KtPropertyElement` with the updated type.
 */
infix fun KtPropertyElement.Type(type: KtTypeElement): KtPropertyElement {
    return this.also {
        it.type = type
    }
}

/**
 * Retrieves the associated field of the current property element. If the field does not
 * already exist, it creates a new instance of [KtFieldElement] with the current
 * property's name and type, assigns it to the field, and then returns it.
 *
 * @return The associated [KtFieldElement] of the property element.
 */
fun KtPropertyElement.getField(): KtFieldElement {
    return field ?: KtFieldElementImpl(
        name = name,
        type = type,
        declarationsScope = declarationsScope,
    ).also {
        field = it
    }
}

/**
 * Creates or retrieves a getter function for the property element. If a getter already exists,
 * it updates its body with the provided DSL block. Otherwise, it creates and initializes a new getter.
 *
 * @param body A lambda function marked with the `KefirDslMarker` that defines the content of the getter's body.
 *             By default, it returns the `Field` of the property element.
 * @return An instance of [KtFunctionElement] representing the getter function, either existing or newly created.
 */
inline fun KtPropertyElement.Getter(
    body: @KefirDslMarker KtBlockElement.() -> Unit = { Return(Field) }
): KtFunctionElement {
    val getter1 = getter
    return getter1?.also {
        it.Body(body)
    }
        ?: KtFunctionElementImpl(
            declarationsScope = this.declarationsScope,
            name = "<get-$name>",
            type = this.type
        )
            .also {
                getter = it
                it.This()
                it.Body(body)
            }
}

/**
 * Creates or retrieves the setter for the current property and allows configuring it using the provided body.
 * If a setter already exists, its body is updated. Otherwise, a new setter is created and initialized.
 *
 * @param name The name of the setter parameter, defaults to "value".
 * @param body A lambda marked with `@KefirDslMarker` that defines the body of the setter using DSL.
 * @return The instance of the `KtFunctionElement` representing the property's setter.
 */
inline fun KtPropertyElement.Setter(
    name: String = "value",
    body: @KefirDslMarker KtBlockElement.() -> Unit = { Field Set Variable(name) }
): KtFunctionElement {
    val setter1 = setter
    return setter1?.also {
        it.Body(body)
    }
        ?: KtFunctionElementImpl(
            declarationsScope = this.declarationsScope,
            name = "<set-${this.name}>",
            type = KtBaseTypes.UNIT
        ).also {
            setter = it
        }.also {
            it.This()
            it.Param(name, this.type)
            it.Body(body)
        }
}