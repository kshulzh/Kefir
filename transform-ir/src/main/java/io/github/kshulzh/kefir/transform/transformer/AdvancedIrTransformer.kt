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

package io.github.kshulzh.kefir.transform.transformer

import io.github.kshulzh.kefir.ir.helper.KtIrInitDeclarationElement
import io.github.kshulzh.kefir.ir.helper.KtIrInitExpressionElement
import io.github.kshulzh.kefir.ir.helper.KtIrInitStatementElement
import io.github.kshulzh.kefir.model.api.KtElement
import io.github.kshulzh.kefir.model.api.arg.KtParameterElement
import io.github.kshulzh.kefir.model.api.declatation.*
import io.github.kshulzh.kefir.model.api.expression.*
import io.github.kshulzh.kefir.model.api.io.KtFileElement
import io.github.kshulzh.kefir.model.api.statement.KtExpressionStatement
import io.github.kshulzh.kefir.model.api.statement.KtReturnStatementElement
import io.github.kshulzh.kefir.model.api.statement.KtStatementElement
import io.github.kshulzh.kefir.model.api.type.KtBaseTypes
import io.github.kshulzh.kefir.model.api.type.KtClassTypeElement
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.transform.context.KtIrLocalTransformContext
import io.github.kshulzh.kefir.transform.context.KtIrLocalTransformContext1
import io.github.kshulzh.kefir.transform.utils.getIr
import io.github.kshulzh.kefir.transform.utils.getNodeIr
import io.github.kshulzh.problemgraph.v1.calc
import org.jetbrains.kotlin.backend.common.pop
import org.jetbrains.kotlin.backend.common.push
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.types.IrType

/**
 * AdvancedIrTransformer is an enhanced implementation of the IrTransformer.
 * It provides transformation capabilities for various Kotlin IR (Intermediate Representation) elements.
 *
 * The class extends IrTransformer and overrides several of its methods to provide additional wrapping functionality.
 * It utilizes contextual information (`KtIrLocalTransformContext`) to process and transform different elements of a Kotlin program.
 */
class AdvancedIrTransformer : IrTransformer() {
    /**
     * Processes a given `KtFileElement` and transforms it into an `IrFile` representation using the provided transformation context.
     *
     * @param element the Kotlin file element to be transformed
     * @return the transformed `IrFile`, or `null` if the transformation is not applicable
     */
//declarations
    context(c: KtIrLocalTransformContext)
    override fun invoke(element: KtFileElement): IrFile? {
        return wrap(element) { super.invoke(it) }
    }

    /**
     * Transforms a given Kotlin declaration element into its corresponding IR declaration.
     *
     * @param element The `KtDeclarationElement` to be transformed. Represents a Kotlin declaration in the source code.
     * @return The transformed `IrDeclaration`, or `null` if the transformation cannot be performed.
     */
    context(c: KtIrLocalTransformContext)
    override operator fun invoke(element: KtDeclarationElement): IrDeclaration? {
        return wrap2(element) { super.invoke(it) }
    }

    /**
     * Transforms a given class element in the Kotlin model structure into an IR (Intermediate Representation) class instance
     * within the context of local IR transformation. The transformation is applied using the parent class's implementation,
     * wrapped with additional processing.
     *
     * @param element The class element to transform, represented as a [KtClassElement]. This element encapsulates the
     * structure and characteristics of a Kotlin class.
     * @return The transformed IR class, represented as an [IrClass], or `null` if the transformation could not be performed.
     */
    context(c: KtIrLocalTransformContext)
    override fun invoke(element: KtClassElement): IrClass? {
        return wrap(element) { super.invoke(it) }
    }

    /**
     * Transforms the provided Kotlin function element into an intermediate representation (IR) function.
     *
     * This method uses the context of the [KtIrLocalTransformContext] to apply specific transformations
     * on a [KtFunctionElement]. It wraps the input element and delegates to the superclass implementation
     * to perform the transformation.
     *
     * @param element The Kotlin function element to be transformed, represented as a [KtFunctionElement].
     *                This element contains the structure, annotations, and body of the function
     *                being processed.
     * @return An [IrFunction] representing the intermediate representation of the transformed function,
     *         or `null` if the transformation could not be performed.
     */
    context(c: KtIrLocalTransformContext)
    override fun invoke(element: KtFunctionElement): IrFunction? {
        return wrap(element) { super.invoke(it) }
    }

    /**
     * Transforms a given Kotlin property element (`KtPropertyElement`) into its IR (Intermediate Representation)
     * equivalent (`IrProperty`) within the transformation context.
     *
     * @param element The Kotlin property element to be transformed.
     * @return The IR representation of the provided property element, or `null` if the transformation cannot be performed.
     */
    context(c: KtIrLocalTransformContext)
    override fun invoke(element: KtPropertyElement): IrProperty? {
        return wrap(element) { super.invoke(it) }
    }

    /**
     * Transforms a given Kotlin field element into an intermediate representation (IR) field.
     * This method leverages the transformation logic provided by the superclass, applying additional
     * wrapping using the context-aware `wrap` function.
     *
     * @param element The `KtFieldElement` to be transformed. Represents a field in the Kotlin model structure,
     * including its type, value, annotations, and modifiers.
     * @return The transformed `IrField` representing the field element in the intermediate representation,
     * or `null` if the transformation is not applicable or fails.
     */
    context(c: KtIrLocalTransformContext)
    override fun invoke(element: KtFieldElement): IrField? {
        return wrap(element) { super.invoke(it) }
    }

    /**
     * Transforms a [KtConstructorElement] into an [IrConstructor] using the provided transformation context.
     *
     * @param element The [KtConstructorElement] to be transformed. Represents a Kotlin constructor
     *        element within the Kotlin model structure, which includes initialization logic for the
     *        class it belongs to.
     * @return An [IrConstructor] representing the IR (Intermediate Representation) equivalent of the
     *         given [KtConstructorElement], or `null` if the conversion cannot be performed.
     */
    context(c: KtIrLocalTransformContext)
    override fun invoke(element: KtConstructorElement): IrConstructor? {
        return wrap(element) { super.invoke(it) }
    }

    /**
     * Transforms the provided `KtIrInitDeclarationElement` into its corresponding `IrDeclaration` using
     * the transformation context. The transformation is wrapped to allow additional processing, if needed.
     *
     * @param element The initialization declaration element to be transformed into an IR representation.
     * @return The transformed IR representation of the given element, or `null` if the transformation is
     *         not applicable or fails.
     */
    context(c: KtIrLocalTransformContext)
    override fun invoke(element: KtIrInitDeclarationElement): IrDeclaration? {
        return wrap(element) { super.invoke(it) }
    }

    //type

    /**
     * Transforms the provided `KtTypeElement` into an `IrType` within the given `KtIrLocalTransformContext`.
     *
     * The transformation leverages the `wrap2` utility to wrap the element and delegate
     * processing to the base implementation.
     *
     * @param element The `KtTypeElement` to be transformed.
     * @return An `IrType` representation of the provided `element`, or `null` if the transformation is not applicable.
     */
    context(c: KtIrLocalTransformContext)
    override operator fun invoke(element: KtTypeElement): IrType? {
        return wrap2(element) { super.invoke(it) }
    }

    /**
     * Transforms the given `KtClassTypeElement` into an `IrType` within the provided IR transformation context.
     * This method utilizes the `wrap` function to augment the transformation process while delegating
     * the core transformation to the super implementation.
     *
     * @param element The `KtClassTypeElement` being transformed into an intermediate representation type.
     * @return The resulting `IrType` after the transformation, or `null` if no transformation could be applied.
     */
    context(c: KtIrLocalTransformContext)
    override fun invoke(element: KtClassTypeElement): IrType? {
        return wrap(element) { super.invoke(it) }
    }

    /**
     * Processes a given `KtBaseTypes` element within the context of the local IR transformation and returns
     * the corresponding `IrType` if applicable. This method wraps the processing logic and delegates the
     * invocation to the superclass implementation.
     *
     * @param element the `KtBaseTypes` element to be transformed into an `IrType`
     * @return the resulting `IrType` corresponding to the input `KtBaseTypes` element,
     * or null if the transformation is not applicable
     */
    context(c: KtIrLocalTransformContext)
    override fun invoke(element: KtBaseTypes): IrType? {
        return wrap(element) { super.invoke(it) }
    }

    /**
     * Invokes the transformation on the given expression element and returns the resulting IR expression.
     *
     * @param element The `KtExpressionElement` to be transformed. Represents a general expression element
     *                in the Kotlin abstract syntax tree (AST).
     * @return The resulting `IrExpression`, or `null` if the transformation is not applicable or fails.
     */
//expression
    context(c: KtIrLocalTransformContext)
    override operator fun invoke(element: KtExpressionElement): IrExpression? {
        return wrap2(element) { super.invoke(it) }
    }

    /**
     * Transforms a given `KtBlockElement` into an equivalent `IrBlock` representation within the specified transformation context.
     *
     * @param element The `KtBlockElement` to be transformed. Represents a block element in the Kotlin AST, encapsulating a sequence of statements or expressions.
     * @return The `IrBlock` representation of the provided `KtBlockElement` after transformation, or `null` if the transformation is not applicable.
     */
    context(c: KtIrLocalTransformContext)
    override fun invoke(element: KtBlockElement): IrBlock? {
        return wrap(element) { super.invoke(it) }
    }

    /**
     * Transforms a constant element into its corresponding Intermediate Representation (IR) constant, if applicable.
     *
     * @param element The constant element to be transformed. Represents a compile-time evaluable value.
     * @return The transformed IR representation of the constant element, or null if the transformation is not applicable.
     */
    context(c: KtIrLocalTransformContext)
    override fun invoke(element: KtConstElement<*>): IrConst? {
        return wrap(element) { super.invoke(it) }
    }

    /**
     * Transforms a given [KtGetFieldElement] into an [IrGetField] representation within the current
     * transformation context.
     *
     * @param element The [KtGetFieldElement] representing a field access operation in the Kotlin
     * abstract syntax tree (AST).
     * @return An [IrGetField] corresponding to the transformed IR representation of the provided
     * [KtGetFieldElement], or `null` if the transformation cannot be performed.
     */
    context(c: KtIrLocalTransformContext)
    override fun invoke(element: KtGetFieldElement): IrGetField? {
        return wrap(element) { super.invoke(it) }
    }

    /**
     * Transforms a given `KtGetValueElement` into an `IrGetValue` representation, utilizing the local transformation
     * context. This method applies the transformation logic defined in the superclass while providing additional
     * context-specific wrapping behavior.
     *
     * @param element The `KtGetValueElement` to be transformed. Represents an expression element that retrieves a value.
     * @return The transformed `IrGetValue` instance, or `null` if the transformation is not applicable or unsupported.
     */
    context(c: KtIrLocalTransformContext)
    override fun invoke(element: KtGetValueElement): IrGetValue? {
        return wrap(element) { super.invoke(it) }
    }

    /**
     * Transforms a given [KtSetFieldElement] into its corresponding [IrSetField] representation
     * within the current local IR transformation context.
     *
     * This method utilizes the `wrap` utility to apply the transformation logic from the superclass
     * to the provided element, encapsulating any additional behavior required during the transformation.
     *
     * @param element The [KtSetFieldElement] representing a field-setting expression in the Kotlin abstract syntax tree (AST),
     * including properties such as the field being set, the receiver context, and the value being assigned.
     * @return The resulting [IrSetField] instance representing the transformed field-setting operation,
     * or `null` if the transformation fails or is not applicable.
     */
    context(c: KtIrLocalTransformContext)
    override fun invoke(element: KtSetFieldElement): IrSetField? {
        return wrap(element) { super.invoke(it) }
    }

    /**
     * Transforms a given [KtConstructorCallElement] into an [IrConstructorCall] by wrapping
     * the transformation process with additional processing defined in the context.
     *
     * @param element The [KtConstructorCallElement] representing a constructor call in the Kotlin AST.
     *                This element is processed and transformed into its corresponding IR (Intermediate Representation).
     * @return An [IrConstructorCall] representing the transformed version of the input [KtConstructorCallElement],
     *         or `null` if the transformation fails or is not applicable.
     */
    context(c: KtIrLocalTransformContext)
    override fun invoke(element: KtConstructorCallElement): IrConstructorCall? {
        return wrap(element) { super.invoke(it) }
    }

    /**
     * Transforms a given [KtDelegatingConstructorCallElement] into an [IrDelegatingConstructorCall] within the provided
     * local transformation context. The transformation process applies any necessary changes based on the intermediate
     * representation (IR) requirements.
     *
     * @param element The delegating constructor call element to be transformed.
     * @return The resulting IR delegating constructor call, or null if the transformation could not be performed.
     */
    context(c: KtIrLocalTransformContext)
    override fun invoke(element: KtDelegatingConstructorCallElement): IrDelegatingConstructorCall? {
        return wrap(element) { super.invoke(it) }
    }

    /**
     * Transforms a given `KtIrInitExpressionElement` into an `IrExpression` within the provided transformation context.
     *
     * @param element The `KtIrInitExpressionElement` to be transformed. Represents an IR-backed initialization expression
     *                element in the Kotlin tree.
     * @return The transformed `IrExpression` result of the provided element, or `null` if the transformation cannot be performed.
     */
    context(c: KtIrLocalTransformContext)
    override fun invoke(element: KtIrInitExpressionElement): IrExpression? {
        return wrap(element) { super.invoke(it) }
    }

    /**
     * Invokes the transformation logic on the given [KtStatementElement], wrapping it within the provided
     * transformation context and executing the inherited transformation if applicable.
     *
     * @param element The [KtStatementElement] to be transformed. Represents a statement in the Kotlin abstract syntax tree.
     * @return An [IrStatement] result of the transformation, or `null` if the transformation does not yield a result.
     */
//statement
    context(c: KtIrLocalTransformContext)
    override operator fun invoke(element: KtStatementElement): IrStatement? {
        return wrap2(element) { super.invoke(it) }
    }

    /**
     * Transforms a `KtReturnStatementElement` into an `IrReturn` representation through
     * the local IR transformation context.
     *
     * @param element The `KtReturnStatementElement` representing the Kotlin `return` statement
     * being transformed. This element may include the returned expression and/or the target symbol.
     * @return An `IrReturn` instance representing the transformed `return` statement in Intermediate
     * Representation (IR), or `null` if the transformation is not applicable.
     */
    context(c: KtIrLocalTransformContext)
    override fun invoke(element: KtReturnStatementElement): IrReturn? {
        return wrap(element) { super.invoke(it) }
    }

    /**
     * Transforms a [KtExpressionStatement] into an IR representation.
     *
     * @param element The [KtExpressionStatement] to be transformed.
     * @return The resulting [IrExpression] after transformation, or null if the transformation does not yield an IR expression.
     */
    context(c: KtIrLocalTransformContext)
    override fun invoke(element: KtExpressionStatement): IrExpression? {
        return wrap(element) { super.invoke(it) }
    }

    /**
     * Invokes the transformation on the provided `KtIrInitStatementElement` instance.
     *
     * @param element The `KtIrInitStatementElement` to be transformed. Represents an IR-backed
     *                statement element within the Kotlin tree, containing initialization logic
     *                and statement properties.
     * @return An optional `IrStatement` resulting from the transformation, or `null` if the
     *         transformation does not produce a result.
     */
    context(c: KtIrLocalTransformContext)
    override fun invoke(element: KtIrInitStatementElement): IrStatement? {
        return wrap(element) { super.invoke(it) }
    }

    /**
     * Transforms the given Kotlin parameter element into an IR value parameter representation.
     *
     * The method applies a transformation encapsulated within a wrapping function, providing
     * capabilities for advanced processing of parameter elements within the specified IR transformation context.
     *
     * @param element The Kotlin parameter element (`KtParameterElement`) to be transformed.
     * @return The transformed IR value parameter (`IrValueParameter`) or `null` if the transformation is not applicable.
     */
//other
    context(c: KtIrLocalTransformContext)
    override fun invoke(element: KtParameterElement): IrValueParameter? {
        return wrap(element) { super.invoke(it) }
    }

    /**
     * Wraps a transformation operation on a given `KtElement`, applying specific context-based transformations
     * and handling node stacking in the process. This method ensures the correct IR (Intermediate Representation)
     * is produced for the given element within the provided context.
     *
     * @param element The Kotlin element to transform.
     * @param transform A lambda expression representing the transformation logic to be applied to the element
     *        within the specified `KtIrLocalTransformContext`.
     * @return The transformed result of type `R` obtained after applying the transformation logic.
     */
    context(c: KtIrLocalTransformContext)
    private inline fun <T : KtElement, R> wrap(
        element: T,
        crossinline transform: context(KtIrLocalTransformContext) (T) -> R
    ): R {
        return element.getIr() as? R? ?: if (c is KtIrLocalTransformContext1) {
            val node = element.getNodeIr {
                c.nodeStack.push(this)
                try {
                    transform(element)!!
                } finally {
                    c.nodeStack.pop()
                }
            }
            c.nodeStack.last().calc(node)
        } else {
            transform(element)
        }
    }

    /**
     * Wraps a transformation operation on the given element, applying a transformation if the element cannot be directly
     * converted to the target type using `getIr`.
     *
     * @param element The Kotlin element to be processed and transformed.
     * @param transform A lambda function that performs a specific transformation on the given element
     *                  within the provided context.
     * @return The result of the transformation, either obtained directly from the element's IR representation
     *         or by applying the specified transformation function.
     */
    context(c: KtIrLocalTransformContext)
    private inline fun <T : KtElement, R> wrap2(
        element: T,
        crossinline transform: context(KtIrLocalTransformContext) (T) -> R
    ): R {
        return element.getIr() as? R? ?: transform(element)
    }
}