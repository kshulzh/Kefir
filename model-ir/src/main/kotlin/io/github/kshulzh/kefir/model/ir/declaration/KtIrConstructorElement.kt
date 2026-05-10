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

package io.github.kshulzh.kefir.model.ir.declaration

import io.github.kshulzh.kefir.model.api.KtName
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationElement
import io.github.kshulzh.kefir.model.api.declaration.KtConstructorElement
import io.github.kshulzh.kefir.model.api.declaration.KtDeclarationsScope
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.modifiers.KtModifier
import io.github.kshulzh.kefir.model.api.type.KtTypeParameterElement
import io.github.kshulzh.kefir.model.ir.annotation.KtIrAnnotationElement
import io.github.kshulzh.kefir.model.ir.type.KtIrTypeParameterElement
import io.github.kshulzh.kefir.model.utils.createLazyIr2
import io.github.kshulzh.kefir.transform.IrWrapper
import io.github.kshulzh.kefir.transform.context.KtTransformContext
import io.github.kshulzh.kefir.transform.model.replaceBody
import org.jetbrains.kotlin.ir.declarations.IrConstructor

/**
 * Represents a Kotlin Intermediate Representation (IR) constructor element within the IR transformation context.
 *
 * The `KtIrConstructorElement` is a specific implementation of [KtConstructorElement] that wraps
 * an [IrConstructor] and provides functionality for managing its transformation and contextual
 * information. It is used within the Kotlin compilation pipeline for IR transformations and
 * encapsulates metadata, modifiers, type parameters, and annotations related to the constructor.
 *
 * This class serves as an integration point between the Kotlin FIR (Frontend IR) and IR during
 * transformation processes, enabling plugins and other tools to work with constructor elements
 * in a structured and extensible way.
 *
 * @property irElement The underlying [IrConstructor] element associated with this instance.
 * @property transformContext The [KtTransformContext] used for managing transformation-related data.
 * @property declarationsScope The optional [KtDeclarationsScope] that defines the scope of declarations
 *                              related to this constructor element.
 */
class KtIrConstructorElement(
    override var irElement: IrConstructor,
    var transformContext: KtTransformContext,
    override var declarationsScope: KtDeclarationsScope? = null
) : KtConstructorElement, IrWrapper<IrConstructor> {
    /**
     * The `body` property represents the body of a Kotlin constructor in the intermediate representation (IR).
     * It serves as a wrapper around an `IrBody`, allowing transformation and lazy evaluation of the body within
     * the associated transformation context.
     *
     * The body is an optional property and can be null. When not null, it holds a concrete implementation of
     * a `KtExpressionElement`, providing an abstraction for interacting with the constructor's IR body.
     *
     * Lazy evaluation is facilitated using `createLazyIr2`, enabling deferred initialization or transformation
     * of the body element. This includes wrapping the underlying IR body into an appropriate `KtExpressionElement`
     * and applying any necessary transformations in the process. Additionally, `createLazyIr2` provides hooks
     * for observing changes upon property updates and safely managing parent-child relationships in the syntax tree.
     *
     * When the body is modified, the updated body is replaced in the associated IR element (`irElement`) using
     * the `replaceBody` function, which handles clearing, updating, or re-creating the IR body depending on the
     * provided input.
     */
    override var body: KtExpressionElement? by createLazyIr2(
        transformContext,
        irElement::body,
        { it?.let { wrapIrBody(it, transformContext, this) } },
        property = KtExpressionElement::parent
    ) {
        it?.let { irElement.replaceBody(it) }
    }
    /**
     * Represents the set of modifiers associated with the declaration of a Kotlin constructor element
     * within the Intermediate Representation (IR) model.
     *
     * This set comprises instances of `KtModifier`, which define characteristics or behaviors
     * that can modify the semantics of the constructor. Examples of such modifiers might include
     * visibility (`public`, `private`, etc.), modality (`open`, `final`, etc.), or other
     * behaviors specific to Kotlin language constructs.
     *
     * The property can be both retrieved and updated, encapsulating the current state of the
     * constructor's modifiers and enabling manipulation of these attributes in IR-based
     * transformations or analysis tasks.
     */
    override var modifiers: MutableSet<KtModifier>
        get() = TODO("Not yet implemented")
        set(value) {}
    /**
     * Represents the name of the current constructor element. This name is derived from the
     * corresponding `IrConstructor` instance (`irElement`) and converted to its string representation.
     *
     * The name signifies the identifier of the constructor being wrapped and processed in the context
     * of IR (Intermediate Representation) transformation.
     */
    override var name: KtName = irElement.name.asString()
    /**
     * Returns a string representation of the constructor element.
     *
     * @return a string containing the literal "<CONSTRUCTOR>".
     */
    override fun toString(): String = "<CONSTRUCTOR>"
    /**
     * Represents a lazily initialized list of type parameters associated with the constructor element.
     *
     * This property retrieves the type parameters defined in the corresponding `IrConstructor` element
     * and wraps them into `KtIrTypeParameterElement` instances. The transformation utilizes the provided
     * transformation context and sets the current constructor element as the parent of the newly created
     * type parameter elements.
     *
     * The resulting list can be modified, as it is backed by a mutable list.
     */
    override val typeParameters: MutableList<KtTypeParameterElement> by lazy {
        irElement.typeParameters.map { KtIrTypeParameterElement(it, transformContext, this) }.toMutableList()
    }
    /**
     * Represents the list of annotation elements associated with the Kotlin Intermediate Representation (IR) constructor.
     *
     * The `annotations` property is lazily initialized and provides a mutable list of `KtAnnotationElement` instances.
     * Each annotation element is created by wrapping an annotation in the underlying IR constructor (`IrConstructor`)
     * using the `KtIrAnnotationElement` class. This transformation integrates the annotations into the Kotlin model
     * structure, enabling further analysis or transformation in the compilation process.
     *
     * The property leverages the `lazy` delegate to defer initialization until it is accessed for the first time,
     * ensuring efficient resource utilization during execution.
     *
     * @property annotations A lazily initialized mutable list containing `KtAnnotationElement` instances.
     *                        Each element corresponds to an annotation defined in the IR constructor.
     */
    override val annotations: MutableList<KtAnnotationElement> by lazy {
        irElement.annotations.map { KtIrAnnotationElement(it, transformContext, this) }.toMutableList()
    }
}