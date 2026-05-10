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

package io.github.kshulzh.kefir.model.ir.arg

import io.github.kshulzh.kefir.model.api.KtName
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationElement
import io.github.kshulzh.kefir.model.api.arg.KtParameterElement
import io.github.kshulzh.kefir.model.api.arg.KtParametersScope
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.ir.annotation.wrapIrAnnotations
import io.github.kshulzh.kefir.model.ir.declaration.wrapIrBody
import io.github.kshulzh.kefir.model.ir.type.wrapType
import io.github.kshulzh.kefir.model.utils.createLazyIr2
import io.github.kshulzh.kefir.transform.IrWrapper
import io.github.kshulzh.kefir.transform.context.KtTransformContext
import io.github.kshulzh.kefir.transform.model.replaceDefaultValue
import org.jetbrains.kotlin.ir.declarations.IrValueParameter

/**
 * Represents a Kotlin intermediate representation (IR) parameter element.
 *
 * This class is responsible for encapsulating an `IrValueParameter` and connecting it
 * to the Kotlin Transformation Context (`KtTransformContext`). It implements the `KtParameterElement`
 * interface to participate in the transformation process between IR and Kotlin model elements.
 *
 * @property irElement The IR value parameter being wrapped by this element. It serves as the underlying IR representation.
 * @property transformContext The transformation context associated with this parameter element, providing utilities for IR processing.
 * @property parametersScope (Optional) The parameter scope that contains this parameter. This can be used to manage
 * parameters in callable entities like functions or constructors.
 */
class KtIrParameterElement(
    override val irElement: IrValueParameter,
    var transformContext: KtTransformContext,
    override var parametersScope: KtParametersScope? = null,
) : KtParameterElement, IrWrapper<IrValueParameter> {
    /**
     * Represents the name of the parameter element as a `KtName`.
     *
     * This property provides the name of the parameter, which is derived from the
     * underlying `irElement`. It can be retrieved or updated to reflect
     * modifications to the parameter's name in the Kotlin Intermediate Representation (IR) model.
     */
    override var name: KtName
        get() = irElement.name.asString()
        set(value) {}
    /**
     * Represents the type of the parameter in the intermediate representation (IR) model.
     * This property is lazily initialized and managed using a transformation context.
     *
     * The backing property is created using the `createLazyIr2` function, which provides a mechanism
     * for deferred initialization and transformation of the type element associated with this parameter.
     * The type information is encapsulated as a `KtTypeElement`, which serves as the abstraction for
     * type-related constructs in the Kotlin type system.
     *
     * The `wrapType` function is utilized to convert the IR type element into a `KtTypeElement`
     * by applying the necessary transformations based on the provided context. The transformed type
     * element can represent various forms of type constructs, such as class types or parameter types.
     *
     * This property also provides an `onSet` behavior, ensuring any necessary actions are taken when the
     * type is updated or modified during the transformation process. However, the current implementation
     * of the `onSet` block is deferred and needs to be completed.
     *
     * @property type The transformed representation of the type element associated with the parameter.
     * @see KtTypeElement
     * @see createLazyIr2
     * @see wrapType
     */
    override var type: KtTypeElement? by createLazyIr2(
        transformContext,
        irElement::type,
        { wrapType(it,transformContext) },
        onSet = {
            //todo
            null
        }
    )
    /**
     * Represents the default value of the parameter element in the intermediate representation (IR).
     *
     * This property is lazily initialized and transformed using the `createLazyIr2` utility. Its behavior
     * is defined as follows:
     *
     * - Retrieves the initial IR default value from the `IrValueParameter` and transforms it into a
     *   `KtExpressionElement` representation.
     * - Applies the transformation context, ensuring the default value is wrapped appropriately via the
     *   `wrapIrBody` function.
     * - When the value is updated, replaces the IR-level default value of the parameter by calling
     *   `replaceDefaultValue` on the underlying `IrValueParameter`.
     *
     * A `null` value indicates that the parameter has no defined default value.
     */
    override var value: KtExpressionElement? by createLazyIr2(
        transformContext,
        irElement::defaultValue,
        { it?.let { wrapIrBody(it, transformContext, this)  }},
        onSet = {
            irElement.replaceDefaultValue(it)
            null
        }
    )
    /**
     * Represents the kind of the parameter, providing additional context or categorization regarding its role.
     *
     * The `kind` property describes the nature of the parameter and is typically used to distinguish between
     * regular parameters and those with specialized purposes, such as being a dispatch receiver.
     *
     * Possible values are defined in the nested `Kind` enum, including options like `Regular` and `DispatchReceiver`.
     * This property can be null to indicate default or unspecified behavior.
     */
    override var kind: KtParameterElement.Kind?
        get() = TODO("Not yet implemented")
        set(value) {}

    /**
     * Represents a lazily initialized mutable list of annotation elements associated with the current parameter element.
     *
     * This property is backed by the transformation of `irElement` annotations into corresponding `KtAnnotationElement`
     * instances. The transformation is performed using the `wrapIrAnnotations` function, which maps IR constructor calls
     * to Kotlin annotation elements within the context of the provided `transformContext`.
     *
     * The initialization of this property is deferred until it is first accessed, ensuring that the annotations are not
     * computed unnecessarily. Once computed, the list can be modified, allowing for the addition or removal of annotation
     * elements.
     *
     * The primary purpose of this property is to provide convenient access to metadata annotations defined on the current
     * parameter element within the Kotlin IR transformation pipeline.
     */
    override val annotations: MutableList<KtAnnotationElement> by lazy {
        wrapIrAnnotations(irElement.annotations, transformContext, this).toMutableList()
    }
}