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

package io.github.kshulzh.kefir.model.fir.declaration

import io.github.kshulzh.kefir.model.api.KtName
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationElement
import io.github.kshulzh.kefir.model.api.declaration.KtClassElement
import io.github.kshulzh.kefir.model.api.declaration.KtDeclarationElement
import io.github.kshulzh.kefir.model.api.declaration.KtDeclarationsScope
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.api.type.KtTypeParameterElement
import io.github.kshulzh.kefir.transform.FirWrapper
import io.github.kshulzh.kefir.transform.context.KtFirTransformContext
import org.jetbrains.kotlin.fir.declarations.FirClass

/**
 * Represents a Kotlin class element that is backed by a FIR (Frontend Intermediate Representation) class.
 *
 * The `KtFirClassElement` provides an abstraction over FIR class elements, allowing for integration
 * with Kotlin's higher-level model structure. It supports access to the associated FIR element,
 * transformation context, declarations scope, and other structural components like declarations,
 * annotations, supertypes, and type parameters.
 *
 * @property firElement The FIR class element that this wrapper represents. It provides access to the
 * underlying structure of the class in the FIR layer.
 * @property transformFirContext The transformation context used for FIR-based transformations. This
 * context offers utilities and information necessary during FIR processing operations.
 * @property declarationsScope An optional scope containing the declarations associated with this class.
 */
class KtFirClassElement(
    override val firElement: FirClass,
    var transformFirContext: KtFirTransformContext,
    override var declarationsScope: KtDeclarationsScope? = null,
) : KtClassElement, FirWrapper<FirClass> {
    /**
     * Represents the name of a class element derived from its corresponding `FirClass` symbol.
     * This variable provides both getter and setter functionality, allowing the name to be
     * retrieved or updated as an instance of `KtName`.
     */
    override var name: KtName
        get() = firElement.symbol.name.identifier
        set(value) {}
    /**
     * Represents the set of declaration elements contained within the current Kotlin model element.
     *
     * This property provides access to a mutable set of `KtDeclarationElement` instances associated
     * with the current model element. These declarations represent structural and functional components
     * of the model, such as classes, methods, and properties.
     *
     * The set is populated or managed dynamically depending on the context of the implementation.
     * As this property is not yet implemented, accessing it will result in an exception.
     */
    override val declarations: MutableSet<KtDeclarationElement>
        get() = TODO("Not yet implemented")
    /**
     * Holds a list of annotations associated with a class element.
     *
     * The `annotations` property provides access to the mutable list of annotations
     * applied to the class. Each annotation is represented by an instance of
     * `KtAnnotationElement`, which includes details about the annotation type and
     * its nested scope (if applicable).
     */
    override var annotations: MutableList<KtAnnotationElement>
        get() = TODO("Not yet implemented")
        set(value) {}
    /**
     * Returns the mutable list of `supertypes` associated with this class element.
     *
     * This list represents the direct supertypes of the given Kotlin class or interface
     * being modeled. Each entry in the list is an instance of [KtTypeElement], which
     * provides abstraction and metadata about the type system in Kotlin. This property
     * allows for inspection and manipulation of the type hierarchy of the modeled class.
     *
     * The supertypes are typically derived from the source or intermediary representations
     * in the Kotlin compiler backend processing (e.g., FIR). The contents of this property
     * may be populated lazily or remain unimplemented until further context is provided.
     *
     * Modifications to this list may impact type resolution or analysis elsewhere in
     * contexts where type constraints or inheritance hierarchies are relevant.
     */
    override val supertypes: MutableList<KtTypeElement>
        get() = TODO("Not yet implemented")
    /**
     * Represents the type parameters declared in the class. Each type parameter is represented
     * as an instance of [KtTypeParameterElement], which encapsulates information about the type parameter,
     * such as its name, variance, supertype bounds, and type parameter scope.
     *
     * This property provides both a getter and a setter, allowing retrieval and modification of the
     * list of type parameters for the class.
     *
     * The order of the type parameters in the list corresponds to the order in which they are declared.
     * It is mutable, enabling dynamic updates to the type parameter declarations.
     */
    override var typeParameters: MutableList<KtTypeParameterElement>
        get() = TODO("Not yet implemented")
        set(value) {}
}