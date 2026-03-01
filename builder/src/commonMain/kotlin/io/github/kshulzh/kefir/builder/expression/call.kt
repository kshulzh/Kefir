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
@file:Suppress("FunctionName")

package io.github.kshulzh.kefir.builder.expression

import io.github.kshulzh.kefir.model.api.declatation.KtConstructorElement
import io.github.kshulzh.kefir.model.api.expression.KtDelegatingConstructorCallElement
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.expression.KtDelegatingConstructorCallElementImpl

/**
 * Creates a delegating constructor call element.
 *
 * @param constructorElement the constructor element that is being delegated to
 * @param args the list of arguments for the constructor call, defaults to an empty mutable list
 * @return a delegating constructor call element for the specified constructor and arguments
 */
fun CallDelegate(
    constructorElement: KtConstructorElement,
    args: MutableList<KtExpressionElement?> = mutableListOf()
): KtDelegatingConstructorCallElement {
    return KtDelegatingConstructorCallElementImpl(
        constructorElement,
        args
    )
}