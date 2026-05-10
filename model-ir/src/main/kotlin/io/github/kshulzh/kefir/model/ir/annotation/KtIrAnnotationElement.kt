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

package io.github.kshulzh.kefir.model.ir.annotation

import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationElement
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationsScope
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.ir.expression.wrapIrExpression
import io.github.kshulzh.kefir.model.ir.type.wrapType
import io.github.kshulzh.kefir.transform.IrWrapper
import io.github.kshulzh.kefir.transform.context.KtTransformContext
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI

/**
 * Represents an annotation element in the Kotlin Intermediate Representation (IR) model.
 *
 * The `KtIrAnnotationElement` class is responsible for wrapping and managing an IR annotation (`IrConstructorCall`)
 * within a Kotlin transformation context. It implements the `KtAnnotationElement` and `IrWrapper` interfaces,
 * facilitating interactions with annotation types and their associated scopes and arguments in the IR domain.
 *
 * @property irElement The underlying `IrConstructorCall` instance representing the annotation in the IR.
 * @property transformContext The context used for transforming IR elements, providing utilities for IR processing and transformation.
 * @property annotationScope Represents an optional scope of annotations associated with this element.
 * @property type The type of the annotation represented as a `KtTypeElement`. It provides type details for the annotation.
 * @property arguments A mutable list of `KtExpressionElement`s representing the arguments of the annotation. This list is populated from the IR arguments.
 * @property argumentMap A mutable map associating annotation argument names with their corresponding `KtExpressionElement` representations.
 */
class KtIrAnnotationElement(
    override val irElement: IrConstructorCall,
    var transformContext: KtTransformContext,
    override var annotationScope: KtAnnotationsScope? = null
) : KtAnnotationElement, IrWrapper<IrConstructorCall> {
    /**
     * Represents the type associated with the annotation element in the intermediate representation (IR) model.
     *
     * This property is an overridden variable from the [KtAnnotationElement] and provides the corresponding
     * [KtTypeElement] representation for the inherited `type`. It is initialized by wrapping the IR-level type
     * information ([IrType]) associated with the [irElement] using the [wrapType] utility function.
     *
     * The [type] variable acts as a bridge between the Kotlin IR and the frontend representation, enabling
     * transformations and analyses of the annotation element's type information during compilation or code transformation.
     *
     * @property type The [KtTypeElement] that represents the annotation's type information in the transformed model.
     *                It is non-null, as the type wrapping is considered essential and is mandated by its usage.
     */
    override var type: KtTypeElement = wrapType(irElement.type, transformContext)!!

    /**
     * Represents the list of arguments for an annotation element in the Kotlin Intermediate Representation (IR).
     *
     * Each argument corresponds to an expression element and is derived from the original IR constructor call
     * arguments. If a specific IR argument is non-null, it is wrapped into a `KtExpressionElement` using
     * `wrapIrExpression`, which transforms and associates it with the current context and parent element.
     *
     * This property is mutable and maintains a collection of argument elements that can be updated or transformed
     * during the compilation or annotation processing phase.
     *
     * The use of `@OptIn(UnsafeDuringIrConstructionAPI::class)` indicates that the construction and
     * transformation process for this property involves potentially unsafe operations during IR construction.
     */
    @OptIn(UnsafeDuringIrConstructionAPI::class)
    override var arguments: MutableList<KtExpressionElement?> = irElement.arguments.map { it?.let { wrapIrExpression(it, transformContext, this)!!} }.toMutableList()
    /**
     * Represents a mutable map that associates parameter names to their corresponding transformed
     * expressions (if available) within the scope of a Kotlin IR constructor call.
     *
     * This property is initialized by mapping the parameters of the underlying IR constructor call
     * to its respective arguments, transforming each argument into a [KtExpressionElement] using
     * the [wrapIrExpression] function. If an argument is absent, the corresponding value in the map
     * is set to `null`.
     *
     * The keys in the map are the parameter names as strings, while the values are instances of
     * [KtExpressionElement] or `null`. This structure enables easy access to transformed arguments
     * based on parameter names, facilitating downstream processing and analysis of annotations or
     * other IR elements.
     *
     * This property is marked with the `@OptIn` annotation due to its usage of APIs under the
     * `UnsafeDuringIrConstructionAPI` experimental flag.
     */
    @OptIn(UnsafeDuringIrConstructionAPI::class)
    override var argumentMap: MutableMap<String, KtExpressionElement?> =
        irElement.symbol.owner.parameters.zip(irElement.arguments.toList()).associate { param2arg ->
            param2arg.first.name.asString() to param2arg.second?.let { wrapIrExpression(it, transformContext, this) }
        }.toMutableMap()
}