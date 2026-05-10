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

import io.github.kshulzh.kefir.model.api.arg.KtParameterElement
import io.github.kshulzh.kefir.model.api.declaration.KtClassElement
import io.github.kshulzh.kefir.model.api.declaration.KtConstructorElement
import io.github.kshulzh.kefir.model.api.declaration.KtDeclarationElement
import io.github.kshulzh.kefir.model.api.declaration.KtFieldElement
import io.github.kshulzh.kefir.model.api.declaration.KtFunctionElement
import io.github.kshulzh.kefir.model.api.declaration.KtPropertyElement
import io.github.kshulzh.kefir.model.api.expression.KtBlockElement
import io.github.kshulzh.kefir.model.api.expression.KtConstElement
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.io.KtFileElement
import io.github.kshulzh.kefir.model.api.statement.KtReturnStatementElement
import io.github.kshulzh.kefir.model.api.statement.KtStatementElement
import io.github.kshulzh.kefir.model.api.type.KtBaseTypes
import io.github.kshulzh.kefir.model.api.type.KtClassTypeElement
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.transform.arg.transformFirParameter
import io.github.kshulzh.kefir.transform.context.KtFirLocalTransformContext
import io.github.kshulzh.kefir.transform.declaration.*
import io.github.kshulzh.kefir.transform.expression.transformFirBlock
import io.github.kshulzh.kefir.transform.expression.transformFirConst
import io.github.kshulzh.kefir.transform.expression.transformFirExpression
import io.github.kshulzh.kefir.transform.statement.transformFirReturn
import io.github.kshulzh.kefir.transform.statement.transformFirStatement
import io.github.kshulzh.kefir.transform.type.transformBaseFirType
import io.github.kshulzh.kefir.transform.type.transformClassFirType
import io.github.kshulzh.kefir.transform.type.transformFirType
import org.jetbrains.kotlin.fir.declarations.*
import org.jetbrains.kotlin.fir.expressions.*
import org.jetbrains.kotlin.fir.types.ConeKotlinType

/**
 * A base class for transforming various Kotlin source elements into their corresponding
 * Frontend Intermediate Representation (FIR) elements. The `FirTransformer` serves as
 * an extensible framework for handling multiple types of Kotlin constructs, including
 * files, declarations, classes, functions, properties, and expressions, among others.
 *
 * This class provides transformation capabilities for converting high-level Kotlin source
 * elements into a more detailed, analyzable, and resolvable intermediate representation
 * used in the Kotlin compiler pipeline.
 *
 * The transformations leverage a contextual approach using `KtFirLocalTransformContext`,
 * which supplies necessary data and tools for accurate and consistent conversion.
 *
 * Subclasses may override specific transformation methods to customize FIR generation
 * for certain element types.
 */
open class FirTransformer {
    /**
     * Invokes the transformation of a given [KtFileElement] into a FIR (Frontend Intermediate Representation) [FirFile].
     * Provides an operation to create a FIR representation of a Kotlin source file using a specific transformation context.
     *
     * @param element the Kotlin file element to be transformed into a FIR representation.
     * @return the resulting [FirFile] representation if the transformation is successful, or `null` if it fails.
     */
//declarations
    context(c: KtFirLocalTransformContext)
    open operator fun invoke(element: KtFileElement): FirFile? = c.transformFirFile(element)

    /**
     * Invokes the transformation of a given Kotlin declaration element into a FIR (Frontend Intermediate Representation) declaration
     * within the context of a local FIR transformation.
     *
     * Delegates the transformation process to the underlying `transformFirDeclaration` method using the provided transformation context.
     *
     * @param element The Kotlin declaration element to be transformed. Supported types include various subclasses of `KtDeclarationElement`.
     * @return The resulting FIR declaration if the transformation is successful, or null if the input element is not recognized or cannot be transformed.
     */
    context(c: KtFirLocalTransformContext)
    open operator fun invoke(element: KtDeclarationElement): FirDeclaration? = c.transformFirDeclaration(element)

    /**
     * Transforms a Kotlin class element into its corresponding FIR (Frontend Intermediate Representation) class structure
     * by invoking the provided transformation context.
     *
     * This operator function serves as a direct entry point for transforming a [io.github.kshulzh.kefir.model.api.declaration.KtClassElement] into a [FirClass] using
     * the current [KtFirLocalTransformContext]. It delegates the transformation process to the `transformFirClass` method
     * of the context.
     *
     * @param element The Kotlin class element ([io.github.kshulzh.kefir.model.api.declaration.KtClassElement]) to be transformed.
     * @return A [FirClass] representing the transformed structure of the given [io.github.kshulzh.kefir.model.api.declaration.KtClassElement],
     * or `null` if the transformation cannot be completed.
     */
    context(c: KtFirLocalTransformContext)
    open operator fun invoke(element: KtClassElement): FirClass? = c.transformFirClass(element)

    /**
     * Invokes the transformation of a `KtFunctionElement` into a `FirFunction` within the context
     * provided by `KtFirLocalTransformContext`.
     *
     * @param element The `KtFunctionElement` to be transformed. This represents a function declaration
     *                in the Kotlin model, including its body, parameters, return type, and other attributes.
     * @return A `FirFunction` representation of the input `KtFunctionElement` or `null` if the transformation
     *         could not be completed.
     */
    context(c: KtFirLocalTransformContext)
    open operator fun invoke(element: KtFunctionElement): FirFunction? = c.transformFirFunction(element)

    /**
     * Transforms a provided [io.github.kshulzh.kefir.model.api.declaration.KtPropertyElement] into its corresponding [FirProperty] representation
     * using the given transformation context. This operator function enables the transformation of
     * Kotlin property elements as part of the FIR (Frontend Intermediate Representation) pipeline.
     *
     * @param element The [io.github.kshulzh.kefir.model.api.declaration.KtPropertyElement] representing the Kotlin property to be transformed.
     * @return A [FirProperty] corresponding to the provided [io.github.kshulzh.kefir.model.api.declaration.KtPropertyElement], or null if the
     * transformation cannot be applied.
     */
    context(c: KtFirLocalTransformContext)
    open operator fun invoke(element: KtPropertyElement): FirProperty? = c.transformFirProperty(element)

    /**
     * Invokes the transformation of a `KtFieldElement` into its FIR (Frontend Intermediate Representation)
     * equivalent, represented as a `FirField`. The transformation process utilizes the provided
     * `KtFirLocalTransformContext` to perform the conversion.
     *
     * @param element The `KtFieldElement` to be transformed. Represents a field declaration in the Kotlin model structure.
     * @return A `FirField` instance that corresponds to the transformed `KtFieldElement` or null if the transformation fails.
     */
    context(c: KtFirLocalTransformContext)
    open operator fun invoke(element: KtFieldElement): FirField? = c.transformFirField(element)

    /**
     * Invokes the transformation of a `KtConstructorElement` into a `FirConstructor` within the given context.
     *
     * @param element The Kotlin constructor element to be transformed into a FIR constructor.
     * @return The transformed `FirConstructor`, or null if the transformation fails.
     */
    context(c: KtFirLocalTransformContext)
    open operator fun invoke(element: KtConstructorElement): FirConstructor? = c.transformFirConstructor(element)
    //type

    /**
     * Transforms the given type element into a FIR (Frontend Intermediate Representation) type.
     *
     * @param element the Kotlin type element to be transformed.
     * @return the corresponding FIR type as a [ConeKotlinType], or null if the transformation is not applicable.
     */
    context(c: KtFirLocalTransformContext)
    open operator fun invoke(element: KtTypeElement): ConeKotlinType? = c.transformFirType(element)

    /**
     * Transforms a [KtClassTypeElement] into a [ConeKotlinType] representation using the current
     * [KtFirLocalTransformContext]. The transformation process involves mapping the provided
     * type element into its FIR equivalent, including processing its type arguments, nullability,
     * and class details.
     *
     * @param element The [KtClassTypeElement] to be transformed into a [ConeKotlinType].
     * This type element comprises the package name, class name, nullability, and type arguments that
     * describe the Kotlin class type within the type system.
     *
     * @return A [ConeKotlinType] representing the FIR transformation of the provided [KtClassTypeElement],
     * or `null` if the transformation fails or is not applicable.
     */
    context(c: KtFirLocalTransformContext)
    open operator fun invoke(element: KtClassTypeElement): ConeKotlinType? = c.transformClassFirType(element)

    /**
     * Transforms the given base type element into a corresponding `ConeKotlinType` representation
     * using the provided transformation context.
     *
     * This function leverages the `transformBaseFirType` method of the `KtFirLocalTransformContext`
     * to map instances of `KtBaseTypes` (such as `STRING`, `INT`, etc.) to their respective
     * `ConeKotlinType` equivalents.
     *
     * @param element The base type element (`KtBaseTypes`) to be transformed.
     * @return The corresponding `ConeKotlinType` representation, or `null` if transformation is not possible.
     */
    context(c: KtFirLocalTransformContext)
    open operator fun invoke(element: KtBaseTypes): ConeKotlinType? = c.transformBaseFirType(element)
    //expression

    /**
     * Invokes the transformation of the provided Kotlin expression element into a corresponding
     * Frontend Intermediate Representation (FIR) expression.
     *
     * This function utilizes the provided transformation context to transform the input
     * into a FIR expression. If the transformation is not applicable for the input element,
     * it returns `null`.
     *
     * @param element The Kotlin expression element to be transformed. Represents a general
     *                expression in the Kotlin abstract syntax tree (AST).
     * @return The transformed FIR expression if the transformation is applicable; otherwise, returns `null`.
     */
    context(c: KtFirLocalTransformContext)
    open operator fun invoke(element: KtExpressionElement): FirExpression? = c.transformFirExpression(element)

    /**
     * Transforms a [KtBlockElement] into its corresponding [FirBlock] representation within the given context.
     * This function delegates the transformation process to the [transformFirBlock] method of the provided context.
     *
     * @param element The [KtBlockElement] to transform. Represents a block of statements or expressions.
     * @return The transformed [FirBlock] if the input element is successfully processed, or null if the transformation fails.
     */
    context(c: KtFirLocalTransformContext)
    open operator fun invoke(element: KtBlockElement): FirBlock? = c.transformFirBlock(element)

    /**
     * Transforms a constant element (`KtConstElement`) into its corresponding FIR literal expression (`FirLiteralExpression`).
     * Utilizes the provided transformation context to execute the conversion process.
     *
     * @param element The `KtConstElement` representing a constant value in the Kotlin abstract syntax tree.
     *                It contains the value to be transformed into a FIR representation.
     * @return The transformed `FirLiteralExpression` representing the constant, or `null` if the transformation fails or is not applicable.
     */
    context(c: KtFirLocalTransformContext)
    open operator fun invoke(element: KtConstElement<*>): FirLiteralExpression? = c.transformFirConst(element)
    //statement

    /**
     * Transforms a given [KtStatementElement] into a corresponding [FirStatement].
     *
     * This operator function utilizes the provided transformation context to convert
     * the specified Kotlin statement element into its FIR representation. The transformation
     * is performed based on the underlying type and structure of the input statement element.
     *
     * @param element The [KtStatementElement] representing a statement in the Kotlin abstract syntax tree.
     * @return A [FirStatement] resulting from the transformation, or `null` if the transformation cannot be applied.
     */
    context(c: KtFirLocalTransformContext)
    open operator fun invoke(element: KtStatementElement): FirStatement? = c.transformFirStatement(element)

    /**
     * Transforms a Kotlin `return` statement element into its FIR (Frontend Intermediate Representation) equivalent.
     *
     * @param element The `KtReturnStatementElement` representing the Kotlin `return` statement to be transformed.
     *                This element contains optional properties such as the expression being returned and the target
     *                of the return statement.
     * @return The `FirReturnExpression` resulting from the transformation, or `null` if the transformation fails.
     */
    context(c: KtFirLocalTransformContext)
    open operator fun invoke(element: KtReturnStatementElement): FirReturnExpression? = c.transformFirReturn(element)
    //other

    /**
     * Transforms a `KtParameterElement` into a `FirValueParameter` within the current context.
     *
     * @param element the `KtParameterElement` to be transformed
     * @return the resulting `FirValueParameter` after transformation, or null if the transformation fails
     */
    context(c: KtFirLocalTransformContext)
    open operator fun invoke(element: KtParameterElement): FirValueParameter? = c.transformFirParameter(element)
}