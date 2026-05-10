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
import io.github.kshulzh.kefir.model.api.arg.KtParameterElement
import io.github.kshulzh.kefir.model.api.declaration.KtDeclarationsScope
import io.github.kshulzh.kefir.model.api.declaration.KtFunctionElement
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.modifiers.KtModifier
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.api.type.KtTypeParameterElement
import io.github.kshulzh.kefir.model.ir.annotation.wrapIrAnnotations
import io.github.kshulzh.kefir.model.ir.arg.KtIrParameterElement
import io.github.kshulzh.kefir.model.ir.type.KtIrTypeParameterElement
import io.github.kshulzh.kefir.model.ir.type.kefir
import io.github.kshulzh.kefir.model.ir.type.wrapType
import io.github.kshulzh.kefir.model.utils.createLazyIr2
import io.github.kshulzh.kefir.model.utils.createLazyIrList2
import io.github.kshulzh.kefir.transform.IrWrapper
import io.github.kshulzh.kefir.transform.context.KtTransformContext
import io.github.kshulzh.kefir.transform.model.replaceBody
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.java.ParameterElement
import org.jetbrains.kotlin.ir.declarations.IrFunction

/**
 * Represents an IR-backed abstraction of a Kotlin function element.
 *
 * This class wraps an `IrFunction` element and provides transformation and
 * analysis capabilities for Kotlin IR (Intermediate Representation) functions
 * within the context of a compilation or code transformation process.
 *
 * @property irElement The underlying `IrFunction` element being wrapped.
 * @property transformContext The transformation context used for IR analysis and manipulation.
 * @property declarationsScope The declaration scope associated with this function element, if any.
 */
class KtIrFunctionElement(
    override var irElement: IrFunction,
    var transformContext: KtTransformContext,
    override var declarationsScope: KtDeclarationsScope? = null
) : KtFunctionElement, IrWrapper<IrFunction> {
    /**
     * Represents the lazily initialized or transformed body of the current `KtIrFunctionElement`.
     *
     * This property manages the wrapped representation of the function body as a `KtExpressionElement`,
     * which is derived from the corresponding `IrFunction` body through lazy transformation.
     * The transformation ensures compatibility with the Kotlin intermediate representation (IR),
     * converting the original IR body into an equivalent Kotlin syntax tree element.
     *
     * If accessed before explicit initialization, the body is lazily resolved through the
     * provided transformation logic (`wrapIrBody`) in conjunction with the `createLazyIr2` utility.
     * When the body changes, it is replaced within the underlying IR (`irElement`) using
     * the appropriate replacement mechanism (`replaceBody`).
     *
     * The parent property of the resulting `KtExpressionElement` is updated to reference this instance.
     *
     * @property transformContext The transformation context used to process and initialize the body.
     * @property irElement A reference to the underlying `IrFunction`, whose body is wrapped or replaced.
     */
    override var body: KtExpressionElement? by
    createLazyIr2(
        transformContext,
        irElement::body,
        { it?.let { wrapIrBody(it, transformContext, this) } },
        property = KtExpressionElement::parent
    ) {
        it.let { irElement.replaceBody(it) }
    }
    /**
     * Represents the type of the current function or declaration as a lazily initialized [KtTypeElement].
     *
     * This property is implemented using a custom delegate created by the `createLazyIr2` function.
     * It encapsulates logic for lazy initialization, transformation, and optional setting behavior.
     *
     * The delegate transforms and wraps the underlying IR type (`irElement.returnType`)
     * into its corresponding frontend representation ([KtTypeElement]) using the `wrapType` utility.
     * It integrates with the provided transform context ([KtTransformContext]) for
     * consistent type mapping and analysis purposes.
     *
     * If the type is explicitly set, the behavior is customizable through the `onSet` lambda,
     * which provides a place to handle custom logic during assignment. Currently, the `onSet` logic
     * is marked as a TODO and returns null by default.
     *
     * This property is core to enabling unified type modeling and transformations
     * in the Kotlin IR to frontend integration layer.
     */
    override var type: KtTypeElement? by createLazyIr2(transformContext,irElement::returnType,
        { wrapType(it, transformContext) },
        onSet = {
            //todo
            null
        }
    )
    /**
     * Represents the modifiers applied to the Kotlin function element.
     *
     * The `modifiers` property is used to manage a mutable set of `KtModifier` instances
     * specific to this function. It encapsulates aspects such as visibility (`public`, `private`),
     * modality (`final`, `open`, `abstract`), and other behavior modifiers relevant to
     * Kotlin's language constructs.
     *
     * The content of the `modifiers` property can be dynamically evaluated or set, depending
     * on the state and implementation of the underlying IR transformation or wrapping
     * logic. This property interacts with the `KtModifier` interface and its implementations.
     *
     * This property is declared within the `KtIrFunctionElement` class and plays a key role
     * in modeling and transforming Kotlin IR (Intermediate Representation) functions in
     * a structured manner.
     */
    override var modifiers: MutableSet<KtModifier>
        get() = TODO("Not yet implemented")
        set(value) {}
    /**
     * Represents the name of the function element wrapped by this class.
     *
     * This property provides access to the name of the underlying IR function.
     * It can be retrieved or updated, though the setter currently does not
     * modify the IR element's state.
     */
    override var name: KtName
        get() = irElement.name.asString()
        set(value) {}
    /**
     * List of annotation elements associated with the current Kotlin IR function element.
     *
     * This property provides access to annotations that are transformed from the underlying IR representation
     * (`IrConstructorCall`) to a Kotlin-specific annotation model (`KtAnnotationElement`).
     *
     * The annotations are wrapped using the `wrapIrAnnotations` utility function, which transforms the IR annotations
     * into a mutable list of `KtAnnotationElement`. The transformation context (`transformContext`) is used to assist
     * in this conversion, and the current function element (`this`) serves as the annotation scope.
     *
     * Modifications to this property allow updating the list of transformed annotations associated with this
     * function element.
     */
    override var annotations: MutableList<KtAnnotationElement> =
        wrapIrAnnotations(irElement.annotations, transformContext, this)
        set(value) {}
    /**
     * Represents the list of parameter elements associated with the function.
     *
     * This property holds a mutable list of `KtParameterElement` instances, where each instance
     * describes a parameter defined for the function. Parameters typically include those explicitly
     * defined in the function signature, as well as any additional parameters inferred or synthesized
     * during transformations.
     *
     * The list is lazily initialized using the `createLazyIrList2` mechanism, which allows the transformation
     * context and underlying IR structure to be efficiently wrapped into `KtParameterElement` instances.
     *
     * The following transformations and behaviors are applied:
     * - Each parameter is mapped to a `KtIrParameterElement` using the provided transformation context, IR element,
     *   and references to the containing scope (`KtParameterElement::parametersScope`).
     * - When adding parameters to the list, the optional `onAdd` callback is invoked to allow additional customization
     *   or post-processing.
     *
     * The `parameters` property can be used to retrieve, modify, or analyze the complete set of parameters
     * associated with the function within the Kotlin model.
     */
    override var parameters: MutableList<KtParameterElement> by createLazyIrList2(
        transformContext,
        irElement::parameters,
        { KtIrParameterElement(it, transformContext, this) },
        property = KtParameterElement::parametersScope,
        onAdd = { i,e ->
            //todo
            null
        }
    )

    /**
     * Generates a string representation of the function element.
     *
     * The format of the string is `<FUN> functionName`, where `functionName`
     * represents the name of the function.
     *
     * @return A string representation of the function.
     */
    override fun toString() = "<FUN> $name"
    /**
     * Represents a lazily-initialized list of type parameters associated with the function element.
     *
     * The `typeParameters` property provides a modifiable collection of `KtTypeParameterElement` instances
     * that correspond to the type parameters defined in the function's IR representation. This list is populated
     * by transforming the IR type parameters into their Kotlin frontend equivalents within the given transformation context.
     *
     * Delegated to a `lazy` block to ensure the transformation process occurs only when the property is accessed for the
     * first time. The transformation process involves creating `KtIrTypeParameterElement` instances for each IR type parameter.
     */
    override val typeParameters: MutableList<KtTypeParameterElement> by lazy {
        irElement.typeParameters.map { KtIrTypeParameterElement(it, transformContext, this) }.toMutableList()
    }
}