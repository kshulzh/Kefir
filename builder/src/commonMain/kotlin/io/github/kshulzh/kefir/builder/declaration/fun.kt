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
import io.github.kshulzh.kefir.builder.type.Type
import io.github.kshulzh.kefir.builder.type.match
import io.github.kshulzh.kefir.model.api.arg.KtParameterElement
import io.github.kshulzh.kefir.model.api.declaration.KtClassElement
import io.github.kshulzh.kefir.model.api.declaration.KtDeclarationsScope
import io.github.kshulzh.kefir.model.api.declaration.KtFunctionElement
import io.github.kshulzh.kefir.model.api.declaration.findDeclaration
import io.github.kshulzh.kefir.model.api.expression.KtBlockElement
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.arg.KtParameterElementImpl
import io.github.kshulzh.kefir.model.declatation.KtFunctionElementImpl
import io.github.kshulzh.kefir.model.expression.KtBlockElementImpl

/**
 * Creates or retrieves a function declaration within the current scope.
 * If the scope is a `KtClassElement` and the parameters list is empty or does not begin with a dispatch receiver parameter,
 * a dispatch receiver parameter is automatically added.
 *
 * @param name The name of the function to find or define.
 * @param parameters A mutable list of parameters for the function. Defaults to an empty list.
 * @param init A lambda function to initialize the function if it is newly created. Defaults to an empty initializer.
 * @return An instance of [KtFunctionElement], either an existing or a newly created function.
 */
inline fun KtDeclarationsScope.Fun(
    name: String,
    parameters: MutableList<KtParameterElement> = mutableListOf(),
    init: @KefirDslMarker KtFunctionElement.() -> Unit = {}
): KtFunctionElement {
    if (this is KtClassElement) {
        if (parameters.isEmpty() || parameters[0].name != "<this>") {
            parameters.add(
                0,
                KtParameterElementImpl(
                    "<this>",
                    this.Type(),
                    kind = KtParameterElement.Kind.DispatchReceiver,
                )
            )
        }
    }
    return findDeclaration<KtFunctionElement>(name).firstOrNull()?.also(init) ?: NewFun(name, parameters, init)
}

inline fun KtDeclarationsScope.MatchFun(
    name: String,
    args: MutableList<Any?> = mutableListOf(),
    init: @KefirDslMarker KtFunctionElement.() -> Unit = {}
): KtFunctionElement? {
//    if (this is KtClassElement) {
//        if (args.isEmpty()) {
//            args.add(0,this.Type())
//        } else {
//            val arg0 = args[0]
//            if (arg0 is KtTypeElement && arg0 != this.Type()) {
//                args.add(0,this.Type())
//            } else if (arg0 is Pair<*, *> && arg0.second is KtTypeElement && arg0.first != this.Type()) {
//                args.add(0,this.Type())
//            }
//        }
//    }
    return findDeclaration<KtFunctionElement>(name).match(args)?.also(init)
}

/**
 * Creates a new function declaration within the current scope.
 *
 * @param name The name of the function to create.
 * @param init A lambda function to initialize the function's properties and content.
 * @return An instance of [KtFunctionElement] representing the newly created function.
 */
inline fun KtDeclarationsScope.NewFun(
    name: String,
    parameters: MutableList<KtParameterElement> = mutableListOf(),
    init: @KefirDslMarker KtFunctionElement.() -> Unit = {}
): KtFunctionElement {
    if (this is KtClassElement) {
        parameters.add(
            0,
            KtParameterElementImpl(
                "<this>",
                this.Type(),
                kind = KtParameterElement.Kind.DispatchReceiver,
            )
        )
    }
    return KtFunctionElementImpl(
        name,
        declarationsScope = this,
        parameters = parameters
    ).apply {
        init()
        declarations.add(this)
    }
}

/**
 * Assigns a body to the function element using a provided DSL block. If no block is provided, an empty body is applied.
 *
 * @param body A lambda function marked with the `KefirDslMarker` that defines the content of the body.
 * @return The current `KtFunctionElement` instance with the updated body.
 */
inline infix fun KtFunctionElement.Body(body: @KefirDslMarker (KtBlockElement.() -> Unit) = {}): KtFunctionElement {
    return apply {
        this.body = KtBlockElementImpl(
            type = type,
            parent = this,
        ).apply {
            this.body()
        }
    }
}