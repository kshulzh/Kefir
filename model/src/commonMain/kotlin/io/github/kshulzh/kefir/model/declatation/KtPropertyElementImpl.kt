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

package io.github.kshulzh.kefir.model.declatation

import io.github.kshulzh.kefir.model.api.KtAttributes
import io.github.kshulzh.kefir.model.api.KtName
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationElement
import io.github.kshulzh.kefir.model.api.declatation.KtDeclarationsScope
import io.github.kshulzh.kefir.model.api.declatation.KtFieldElement
import io.github.kshulzh.kefir.model.api.declatation.KtFunctionElement
import io.github.kshulzh.kefir.model.api.declatation.KtPropertyElement
import io.github.kshulzh.kefir.model.api.modifiers.KtModifier
import io.github.kshulzh.kefir.model.api.type.KtTypeElement

/**
 * Represents an implementation of a Kotlin property element.
 *
 * This class provides a concrete representation of `KtPropertyElement` with
 * support for modeling properties in the Kotlin language. A property consists
 * of a name, type, associated modifiers, annotations, and optional field or
 * accessors (getter and setter).
 *
 * @property name The name of the Kotlin property.
 * @property type The type of the Kotlin property as a `KtTypeElement`.
 * @property declarationsScope The optional scope that defines related declarations for the property.
 * @property modifiers The set of modifiers applied to the property (e.g., `public`, `private`).
 * @property annotations A list of annotations applied to the property.
 * @property attributes A mutable map of custom attributes that can be associated with the property.
 * @property field The optional backing field element for this property.
 * @property getter The optional getter function element for this property.
 * @property setter The optional setter function element for this property.
 */
class KtPropertyElementImpl(
    override var name: KtName,
    override var type: KtTypeElement,
    override var declarationsScope: KtDeclarationsScope? = null,
    override var modifiers: MutableSet<KtModifier> = mutableSetOf(),
    override var annotations: MutableList<KtAnnotationElement> = mutableListOf(),
    override var attributes: MutableMap<String, Any> = mutableMapOf(),
    override var field: KtFieldElement? = null,
    override var getter: KtFunctionElement? = null,
    override var setter: KtFunctionElement? = null,
) : KtPropertyElement, KtAttributes {
    override fun toString() = "<PROPERTY> $name"
}