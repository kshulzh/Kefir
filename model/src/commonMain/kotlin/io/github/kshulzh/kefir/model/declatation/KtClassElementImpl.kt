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
import io.github.kshulzh.kefir.model.api.declatation.KtClassElement
import io.github.kshulzh.kefir.model.api.declatation.KtDeclarationElement
import io.github.kshulzh.kefir.model.api.declatation.KtDeclarationsScope
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.utils.createSetDelegate

/**
 * Implementation of the [KtClassElement] interface.
 *
 * This class represents a concrete class element in the Kotlin model structure,
 * providing a container for declarations, annotations, supertypes, and attributes
 * associated with a Kotlin class or interface. It extends the functionality of
 * [KtDeclarationElement], [KtDeclarationsScope], [KtAnnotationsScope], and [KtAttributes].
 *
 * @property name The name of the class element.
 * @property declarationsScope The scope of declarations associated with the class.
 * @property annotations The list of annotations applied to the class.
 * @property attributes A map of key-value attribute pairs associated with the class.
 * @property supertypes The list of supertypes of the class or interface.
 */
class KtClassElementImpl(
    override var name: KtName,
    override var declarationsScope: KtDeclarationsScope? = null,
    declarations: MutableSet<KtDeclarationElement> = mutableSetOf(),
    override var annotations: MutableList<KtAnnotationElement> = mutableListOf(),
    override var attributes: MutableMap<String, Any> = mutableMapOf(),
    override val supertypes: MutableList<KtTypeElement> = mutableListOf(),
) : KtClassElement, KtAttributes {
    /**
     * A mutable set of `KtDeclarationElement` objects representing the declarations within the class element.
     *
     * This property allows management and traversal of the contained declarations, such as classes, functions,
     * or fields, that are part of the Kotlin class represented by the `KtClassElementImpl` instance.
     *
     * The backing field for this property utilizes a delegate created with `createSetDelegate`, providing functionality
     * to synchronize changes between the set and the corresponding scope property `declarationsScope`
     * within each `KtDeclarationElement`.
     */
    override var declarations: MutableSet<KtDeclarationElement> by createSetDelegate(
        declarations,
        KtDeclarationElement::declarationsScope
    )

    override fun toString() = "<CLASS> $name"
}