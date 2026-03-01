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

package io.github.kshulzh.kefir.builder.type

import io.github.kshulzh.kefir.model.api.KtPath
import io.github.kshulzh.kefir.model.api.declatation.KtClassElement
import io.github.kshulzh.kefir.model.api.io.path
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.type.KtClassTypeElementImpl


/**
 * Creates an instance of [KtClassTypeElementImpl].
 *
 * @param isNullable A boolean indicating whether the type is nullable. Default is `false`.
 * @param typeArguments A mutable list of type arguments for the type. Default is an empty list.
 * @return A new instance of [KtClassTypeElementImpl] with the specified properties.
 */
fun KtClassElement.Type(
    isNullable: Boolean = false,
    typeArguments: MutableList<KtTypeElement> = mutableListOf()
): KtTypeElement {
    val pair = this.getPathToFile()

    return KtClassTypeElementImpl(
        pair.first?.path ?: KtPath(),
        pair.second,
        isNullable,
        typeArguments
    )
}

/**
 * Constructs a `KtTypeElement` instance based on the provided package, class name, nullability, and type arguments.
 *
 * @param package_ The package name of the type, expressed as a string.
 * @param clazz The class name of the type, expressed as a string.
 * @param isNullable A boolean indicating whether the type is nullable. Defaults to false.
 * @param typeArguments A mutable list of `KtTypeElement` representing the type arguments. Defaults to an empty list.
 * @return An instance of `KtTypeElement` representing the specified type.
 */
fun Type(
    package_: String,
    clazz: String,
    isNullable: Boolean = false,
    typeArguments: MutableList<KtTypeElement> = mutableListOf()
): KtTypeElement {
    return KtClassTypeElementImpl(
        KtPath(package_.split(".").toMutableList()),
        KtPath(clazz.split(".").toMutableList()),
        isNullable,
        typeArguments
    )
}