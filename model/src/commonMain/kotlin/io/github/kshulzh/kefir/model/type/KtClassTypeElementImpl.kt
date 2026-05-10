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

package io.github.kshulzh.kefir.model.type

import io.github.kshulzh.kefir.model.api.KtPath
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationElement
import io.github.kshulzh.kefir.model.api.declaration.KtClassElement
import io.github.kshulzh.kefir.model.api.type.KtClassTypeElement
import io.github.kshulzh.kefir.model.api.type.KtTypeElement

/**
 * Implementation of the [KtClassTypeElement] interface representing a Kotlin class type element
 * within the type system. This class provides a concrete model for a Kotlin class type, including
 * its package path, class path, nullability, and type arguments.
 *
 * @property ktPackage The fully qualified package path of the class type.
 * @property ktClass The class path hierarchy of the target type.
 * @property isNullable Indicates whether the class type allows nullability.
 * @property typeArguments A mutable list of type arguments associated with the class type.
 */
class KtClassTypeElementImpl(
    override val ktPackage: KtPath,
    override val ktClass: KtPath,
    override val isNullable: Boolean = false,
    override var typeArguments: MutableList<KtTypeElement> = mutableListOf(),
    override val klass: KtClassElement? = null,
    override val annotations: MutableList<KtAnnotationElement> = mutableListOf(),
) : KtClassTypeElement {
    override fun toString() = buildString {
        append(ktPackage.parts.joinToString(".") { it })
        append(":")
        append(ktClass.parts.joinToString(".") { it })
        if (isNullable) append("?")
    }
}