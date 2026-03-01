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

package io.github.kshulzh.kefir.transform.transformer

import io.github.kshulzh.kefir.model.api.KtElement
import io.github.kshulzh.kefir.model.api.arg.KtParameterElement
import io.github.kshulzh.kefir.model.api.declatation.*
import io.github.kshulzh.kefir.model.api.expression.KtBlockElement
import io.github.kshulzh.kefir.model.api.expression.KtConstElement
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.io.KtFileElement
import io.github.kshulzh.kefir.model.api.statement.KtReturnStatementElement
import io.github.kshulzh.kefir.model.api.type.KtBaseTypes
import io.github.kshulzh.kefir.model.api.type.KtClassTypeElement
import io.github.kshulzh.kefir.transform.context.KtFirLocalTransformContext
import io.github.kshulzh.kefir.transform.context.KtFirLocalTransformContext1
import io.github.kshulzh.kefir.transform.utils.getFir
import io.github.kshulzh.kefir.transform.utils.getNodeFir
import io.github.kshulzh.problemgraph.v1.calc
import org.jetbrains.kotlin.backend.common.pop
import org.jetbrains.kotlin.backend.common.push
import org.jetbrains.kotlin.fir.declarations.*
import org.jetbrains.kotlin.fir.expressions.FirBlock
import org.jetbrains.kotlin.fir.expressions.FirExpression
import org.jetbrains.kotlin.fir.expressions.FirLiteralExpression
import org.jetbrains.kotlin.fir.expressions.FirReturnExpression
import org.jetbrains.kotlin.fir.types.ConeKotlinType

/**
 * The `AdvancedFirTransformer` is a specialized implementation of the `FirTransformer` class.
 * It provides transformation logic for various Kotlin elements, producing FIR (Front-End Intermediate Representation) components.
 * This class leverages contextual processing to handle different Kotlin elements effectively.
 */
open class AdvancedFirTransformer : FirTransformer() {
    /**
     * Invokes the transformation logic on a given Kotlin file element, producing a corresponding FIR (Frontend Intermediate Representation) file.
     *
     * @param element A Kotlin file element that implements [KtFileElement]. This element represents a file in a package scope, containing declarations and package hierarchy information
     * .
     * @return A [FirFile] instance representing the transformed file in the FIR structure, or `null` if the transformation fails or is not applicable.
     */
//declarations
    context(c: KtFirLocalTransformContext)
    override operator fun invoke(element: KtFileElement): FirFile? = wrap(element) { super.invoke(it) }

    /**
     * Transforms a given `KtClassElement` into a `FirClass`, if possible, using the current transformation context.
     * This method leverages the `wrap` utility to handle the transformation process.
     *
     * @param element The `KtClassElement` to be transformed into a `FirClass`.
     * @return The resulting `FirClass` after the transformation, or `null` if the transformation cannot be performed.
     */
    context(c: KtFirLocalTransformContext)
    override operator fun invoke(element: KtClassElement): FirClass? = wrap(element) { super.invoke(it) }

    /**
     * Transforms a given [KtFunctionElement] into a corresponding [FirFunction] representation
     * using the current transformation context.
     *
     * @param element the Kotlin function element to be transformed
     * @param c the local transformation context for the FIR transformation process
     * @return the transformed FIR function, or null if the transformation cannot be completed
     */
    context(c: KtFirLocalTransformContext)
    override operator fun invoke(element: KtFunctionElement): FirFunction? = wrap(element) { super.invoke(it) }

    /**
     * Transforms a given [KtPropertyElement] into a [FirProperty] representation within the specified context.
     *
     * @param element The property element to be transformed.
     * @return The transformed [FirProperty] if the transformation is successful, or `null` if the transformation
     *         cannot be performed for the given element.
     */
    context(c: KtFirLocalTransformContext)
    override operator fun invoke(element: KtPropertyElement): FirProperty? = wrap(element) { super.invoke(it) }

    /**
     * Transforms a given `KtFieldElement` into its corresponding `FirField` representation within the current
     * transformation context.
     *
     * @param element The `KtFieldElement` to be transformed. Represents a field declaration in the Kotlin model.
     * @return The transformed `FirField` instance if the transformation is successful, or `null` if the
     * transformation could not produce a valid `FirField`.
     */
    context(c: KtFirLocalTransformContext)
    override operator fun invoke(element: KtFieldElement): FirField? = wrap(element) { super.invoke(it) }

    /**
     * Invokes the transformation process for a given [KtConstructorElement] within the provided
     * [KtFirLocalTransformContext]. This function wraps the element using the specified `wrap` method
     * and delegates the actual transformation logic to the superclass implementation.
     *
     * @param element The [KtConstructorElement] to be transformed. Represents a Kotlin constructor
     * element within the model structure, which includes its constructor body and other properties.
     * @return A [FirConstructor] instance representing the transformed version of the provided
     * [KtConstructorElement], or null if the transformation is not applicable.
     */
    context(c: KtFirLocalTransformContext)
    override operator fun invoke(element: KtConstructorElement): FirConstructor? = wrap(element) { super.invoke(it) }

    /**
     * Transforms the given Kotlin class type element into a corresponding ConeKotlinType
     * using the provided FIR transformation context.
     *
     * This method wraps the transformation process to ensure the necessary contextual
     * operations and validations are applied while invoking the superclass transformation logic.
     *
     * @param element The Kotlin class type element to be transformed.
     * @return The resulting ConeKotlinType, or null if the transformation fails or is not applicable.
     */
//type
    context(c: KtFirLocalTransformContext)
    override operator fun invoke(element: KtClassTypeElement): ConeKotlinType? = wrap(element) { super.invoke(it) }

    /**
     * Invokes the transformation process on a given `KtBaseTypes` element within
     * a provided `KtFirLocalTransformContext`. Utilizes the `wrap` function to
     * handle wrapping logic and delegates the actual operation to the superclass implementation.
     *
     * @param element The `KtBaseTypes` element to be transformed. Represents a predefined collection
     * of base Kotlin types such as primitives (e.g., `INT`, `FLOAT`, `STRING`) or `Unit`.
     * @return The resulting `ConeKotlinType` after the element has been transformed, or `null` if
     * the transformation cannot be performed.
     */
    context(c: KtFirLocalTransformContext)
    override operator fun invoke(element: KtBaseTypes): ConeKotlinType? = wrap(element) { super.invoke(it) }

    /**
     * Transforms a given [KtExpressionElement] into a corresponding [FirExpression] using the current
     * transformation context.
     *
     * This method wraps the transformation process to ensure proper handling of the element and invokes
     * the superclass implementation for the actual conversion.
     *
     * @param element The [KtExpressionElement] to be transformed. Represents an expression node in the Kotlin AST.
     * @return The resulting [FirExpression] if the transformation is successful, or `null` if the transformation cannot be performed.
     */
//expression
    context(c: KtFirLocalTransformContext)
    override operator fun invoke(element: KtExpressionElement): FirExpression? = wrap(element) { super.invoke(it) }

    /**
     * Transforms a given [KtBlockElement] into a corresponding [FirBlock] representation.
     *
     * @param element The block element to be transformed. It represents a sequence of statements or expressions
     * encapsulating the logic for the block within the Kotlin AST.
     * @return The [FirBlock] resulting from the transformation of the provided [KtBlockElement], or `null`
     * if the transformation cannot be performed.
     */
    context(c: KtFirLocalTransformContext)
    override operator fun invoke(element: KtBlockElement): FirBlock? = wrap(element) { super.invoke(it) }

    /**
     * Transforms a constant element into its corresponding FIR literal expression within
     * the context of FIR local transformations. The transformation leverages the `wrap` utility
     * method to apply the transformation safely and handle any contextual requirements.
     *
     * @param element The constant element of the Kotlin abstract syntax tree (AST) to be transformed.
     *                This element extends the `KtConstElement` interface and may hold a specific
     *                constant value of a generic type.
     * @return The resulting FIR literal expression derived from the given constant element, or null
     *         if the transformation could not be performed.
     */
    context(c: KtFirLocalTransformContext)
    override operator fun invoke(element: KtConstElement<*>): FirLiteralExpression? = wrap(element) { super.invoke(it) }

    /**
     * Transforms a Kotlin return statement element into a FIR (Frontend Intermediate Representation) return expression.
     * This method is invoked within the context of a FIR local transformation process.
     *
     * @param element The `KtReturnStatementElement` representing the return statement in the Kotlin AST.
     *                This parameter includes information about the return expression and its target context.
     * @return A nullable `FirReturnExpression` that represents the transformed FIR equivalent of the Kotlin
     *         return statement. Returns `null` if the transformation fails or is inapplicable.
     */
//statement
    context(c: KtFirLocalTransformContext)
    override operator fun invoke(element: KtReturnStatementElement): FirReturnExpression? =
        wrap(element) { super.invoke(it) }

    /**
     * Transforms a given `KtParameterElement` into a `FirValueParameter` within the context of the FIR (Frontend Intermediate Representation) pipeline.
     * Utilizes the `wrap` function to apply the transformation with additional handling specific to `KtFirLocalTransformContext`.
     *
     * @param element The parameter element to be transformed. Represents a parameter in the Kotlin language model.
     * @return The transformed `FirValueParameter`, or `null` if the transformation could not be completed.
     */
//other
    context(c: KtFirLocalTransformContext)
    override operator fun invoke(element: KtParameterElement): FirValueParameter? = wrap(element) { super.invoke(it) }

    /**
     * Wraps the transformation of a Kotlin element, providing additional context handling
     * for FIR (Frontend Intermediate Representation) transformations. If the element cannot
     * be directly transformed, invokes a backup process tailored to the provided
     * transformation context.
     *
     * @param T The type of the Kotlin element to be transformed.
     * @param R The result type after applying the transformation.
     * @param element The Kotlin element to be transformed.
     * @param transform A crossinline lambda representing the transformation logic, executed within a given context.
     * @return The result of the transformation applied to the element.
     */
    context(c: KtFirLocalTransformContext)
    private inline fun <T : KtElement, R> wrap(
        element: T,
        crossinline transform: context(KtFirLocalTransformContext) (T) -> R
    ): R {
        return element.getFir() as? R? ?: if (c is KtFirLocalTransformContext1) {
            val node = element.getNodeFir {
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
}