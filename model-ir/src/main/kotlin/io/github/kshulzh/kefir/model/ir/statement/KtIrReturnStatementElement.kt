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

package io.github.kshulzh.kefir.model.ir.statement

import io.github.kshulzh.kefir.model.api.KtElement
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationElement
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.statement.KtReturnStatementElement
import io.github.kshulzh.kefir.model.ir.expression.wrapIrExpression
import io.github.kshulzh.kefir.model.utils.createLazyIr2
import io.github.kshulzh.kefir.transform.IrWrapper
import io.github.kshulzh.kefir.transform.context.KtTransformContext
import org.jetbrains.kotlin.ir.expressions.IrReturn

/**
 * Represents a `return` statement element wrapping an IR (Intermediate Representation) [IrReturn] instance,
 * within the context of Kotlin's abstract syntax tree (AST) and IR transformation processes.
 *
 * This class implements [KtReturnStatementElement] and serves as a bridge between the Kotlin AST and corresponding
 * IR structures, enabling bidirectional interaction and transformation. The `return` statement is modeled with its
 * associated expression, target, and annotations.
 *
 * @constructor Creates a new instance of [KtIrReturnStatementElement].
 * @param irElement The [IrReturn] element associated with this return statement.
 * @param transformContext The context used for transforming IR elements into their Kotlin AST counterparts.
 * @param parent An optional parent element within the Kotlin AST representing the hierarchical position of this element.
 */
class KtIrReturnStatementElement(
    override val irElement: IrReturn,
    var transformContext: KtTransformContext,
    override var parent: KtElement? = null,
) : KtReturnStatementElement, IrWrapper<IrReturn> {
    /**
     * Represents the expression component within a `KtIrReturnStatementElement`.
     *
     * This property lazily initializes and wraps the underlying `IrExpression` of the
     * associated `IrReturn` element into a `KtExpressionElement`. The transformation is
     * driven by the `wrapIrExpression` utility function, which converts raw IR elements
     * into their Kotlin-specific counterparts within the syntax tree.
     *
     * Key characteristics:
     * - Uses a custom lazy delegate (`createLazyIr2`) for initialization, enabling
     *   deferred computation and efficient resource usage.
     * - Establishes a bidirectional association with the `parent` property of
     *   `KtExpressionElement`, ensuring proper hierarchy within the AST.
     * - Allows transformation context (`transformContext`) to be consistently
     *   applied during the wrapping and initialization of the expression.
     * - The `onSet` handler can be customized for specific actions when the property is modified.
     *
     * Value semantics:
     * - Nullable: The property can be `null` if the expression is not defined or has not been set.
     * - Computed: The actual value is computed on-demand from the associated IR element.
     */
    override var expression: KtExpressionElement? by createLazyIr2(transformContext,
        irElement::value,
        { wrapIrExpression(it, transformContext) },
        property = KtExpressionElement::parent,
        onSet = {
            //todo
            null
        })
    /**
     * Represents the target element associated with this return statement in the intermediate representation (IR) model.
     *
     * This property references a [KtElement] that acts as the destination of the return operation. The target element
     * typically corresponds to a surrounding construct within the Kotlin IR that this return is associated with, such as
     * a function or a lambda expression.
     *
     * The value may be null if the target has not been resolved or is not applicable.
     */
    override var target: KtElement?
        get() = TODO("Not yet implemented")
        set(value) {}

    /**
     * A mutable list containing all the annotations associated with this element.
     *
     * This property provides access to the Kotlin annotations (represented as `KtAnnotationElement`)
     * directly attached to this element. Modifications to this list will affect the set of annotations
     * present on the associated IR element.
     *
     * Annotations in this list represent metadata or additional declarations associated with the
     * element, which can be utilized for various purposes such as code generation, reflection, or
     * custom programmatic transformations.
     */
    override val annotations: MutableList<KtAnnotationElement> = mutableListOf()
}