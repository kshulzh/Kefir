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

package io.github.kshulzh.kefir.builder.expression

import io.github.kshulzh.kefir.model.api.expression.KtConstElement
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.expression.KtConstElementImpl

/**
 * Creates a constant element of a specified type.
 *
 * @param T the reified type of the constant
 * @param value the value of the constant
 * @return a constant element of type KtConstElement containing the given value
 */
//todo set also type here
inline fun <reified T> Const(value: T): KtConstElement<T> = KtConstElementImpl(value)

/**
 * Creates a constant element representing the boolean value `true`.
 *
 * @return a constant element with the value `true`
 */
fun True(): KtConstElement<Boolean> = KtConstElementImpl(true)

/**
 * Constructs a constant element representing the boolean value `false`.
 *
 * @return an instance of `KtConstElement<Boolean>` with the value set to `false`
 */
fun False(): KtConstElement<Boolean> = KtConstElementImpl(false)

/**
 * Creates a constant element with a null value and the specified type.
 *
 * @param type the type of the constant element
 * @return a constant element of the specified type with a null value
 */
fun Null(type: KtTypeElement): KtConstElement<*> = KtConstElementImpl(null, type = type)