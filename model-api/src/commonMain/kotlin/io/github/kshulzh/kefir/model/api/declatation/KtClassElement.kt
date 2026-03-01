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

package io.github.kshulzh.kefir.model.api.declatation

import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationsScope
import io.github.kshulzh.kefir.model.api.type.KtTypeElement

/**
 * Represents a class element in the Kotlin model structure.
 *
 * A `KtClassElement` is a specific type of declaration element that encapsulates the
 * structure and characteristics of a class in Kotlin. It provides a scope for storing
 * its declarations and annotations and a list of its supertypes.
 */
interface KtClassElement : KtDeclarationElement,
    KtDeclarationsScope,
    KtAnnotationsScope {
    /**
     * Represents the list of supertype elements declared in a class or interface.
     * Each supertype is represented as a [KtTypeElement].
     *
     * This property is mutable and can be used to add or remove supertypes
     * associated with the class or interface declaration. Supertypes define
     * the inheritance and type hierarchy for the associated [KtClassElement].
     */
    val supertypes: MutableList<KtTypeElement>
}