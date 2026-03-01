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

import io.github.kshulzh.kefir.ir.helper.KtIrInitDeclarationElement
import io.github.kshulzh.kefir.ir.helper.KtIrInitExpressionElement
import io.github.kshulzh.kefir.ir.helper.KtIrInitStatementElement
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
import io.github.kshulzh.kefir.transform.arg.transformIrParameter
import io.github.kshulzh.kefir.transform.context.KtIrLocalTransformContext
import io.github.kshulzh.kefir.transform.declaration.*
import io.github.kshulzh.kefir.transform.expression.*
import io.github.kshulzh.kefir.transform.statement.transformIrInitStatement
import io.github.kshulzh.kefir.transform.statement.transformIrReturn
import io.github.kshulzh.kefir.transform.statement.transformIrStatement
import io.github.kshulzh.kefir.transform.statement.transformIrStatementExpression
import io.github.kshulzh.kefir.transform.type.transformBaseIrType
import io.github.kshulzh.kefir.transform.type.transformClassIrType
import io.github.kshulzh.kefir.transform.type.transformIrType
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.types.IrType

/**
 * Base class for IR (Intermediate Representation) transformations that operates
 * on various Kotlin code elements and transforms them into their respective IR representations.
 *
 * Serves as a common entry point for handling IR transformation tasks like converting
 * declarations, types, expressions, statements, and structural components of Kotlin code
 * into corresponding IR elements.
 *
 * Transformation is facilitated in a given context implemented by [KtIrLocalTransformContext],
 * which provides the necessary state and utilities for the IR processing pipeline.
 */
open class IrTransformer {
    /**
     * Invokes the transformation of a given Kotlin file element (`KtFileElement`) into an IR file (`IrFile`).
     * This method utilizes the provided transformation context to perform the conversion.
     *
     * @param element The source Kotlin file element to be transformed.
     * @return The resulting IR file after the transformation, or `null` if the transformation fails.
     */
//declarations
    context(c: KtIrLocalTransformContext)
    open operator fun invoke(element: KtFileElement): IrFile? = c.transformIrFile(element)

    /**
     * Transforms a given Kotlin declaration element into its corresponding IR (Intermediate Representation)
     * declaration using the specified transformation context.
     *
     * @param element The `KtDeclarationElement` to be transformed. Represents a declaration in the Kotlin model structure.
     *                This can be any kind of declaration, such as a class, function, property, or field.
     * @return The transformed `IrDeclaration` corresponding to the input element, or `null` if the transformation cannot be performed.
     */
    context(c: KtIrLocalTransformContext)
    open operator fun invoke(element: KtDeclarationElement): IrDeclaration? = c.transformIrDeclaration(element)

    /**
     * Invokes the transformation of a `KtClassElement` into an `IrClass` representation using the given context.
     *
     * This operator function acts as a shorthand entry point for transforming a class element
     * into its corresponding intermediate representation. It leverages the context's transformation
     * logic to produce an `IrClass` or returns `null` if the transformation cannot be performed.
     *
     * @param element The `KtClassElement` instance to be transformed. This represents the
     *                Kotlin class in its model structure, containing its declarations,
     *                annotations, and supertypes.
     * @return The transformed `IrClass` representation, or `null` if the transformation fails or is not applicable.
     */
    context(c: KtIrLocalTransformContext)
    open operator fun invoke(element: KtClassElement): IrClass? = c.transformIrClass(element)

    /**
     * Transforms a given `KtFunctionElement` into its corresponding `IrFunction` representation using the
     * provided transformation context.
     *
     * @param element The `KtFunctionElement` representing the Kotlin function to be transformed.
     * @return The resulting `IrFunction` representation if the transformation is successful, or `null` if
     *         the transformation cannot be performed or the input is invalid.
     */
    context(c: KtIrLocalTransformContext)
    open operator fun invoke(element: KtFunctionElement): IrFunction? = c.transformIrFunction(element)

    /**
     * Transforms a given `KtPropertyElement` into an `IrProperty` using the provided transformation context.
     *
     * @param element The `KtPropertyElement` to be transformed.
     * @return The resulting `IrProperty` if the transformation is successful, or `null` if no transformation is applied.
     */
    context(c: KtIrLocalTransformContext)
    open operator fun invoke(element: KtPropertyElement): IrProperty? = c.transformIrProperty(element)

    /**
     * Transforms a given [KtFieldElement] into its corresponding IR (Intermediate Representation) field representation.
     *
     * @param element The field element to be transformed.
     * @return The transformed [IrField] representation of the given [KtFieldElement], or `null` if the transformation cannot be performed.
     */
    context(c: KtIrLocalTransformContext)
    open operator fun invoke(element: KtFieldElement): IrField? = c.transformIrField(element)

    /**
     * Transforms a given Kotlin constructor element into its IR (Intermediate Representation) equivalent.
     *
     * @param element The Kotlin constructor element to be transformed.
     * @return The transformed IR constructor if the transformation is successful, or `null` if the input element cannot be transformed.
     */
    context(c: KtIrLocalTransformContext)
    open operator fun invoke(element: KtConstructorElement): IrConstructor? = c.transformIrConstructor(element)

    /**
     * Invokes the transformation process for a given IR initialization declaration element.
     *
     * @param element The `KtIrInitDeclarationElement` to be transformed. This represents an IR initialization declaration,
     *                including the initialization logic and scope metadata. It is used to define and manage the declaration
     *                element that needs to be initialized.
     * @return The transformed `IrDeclaration`, or `null` if the transformation cannot be performed.
     */
    context(c: KtIrLocalTransformContext)
    open operator fun invoke(element: KtIrInitDeclarationElement): IrDeclaration? =
        c.transformIrInitDeclaration(element)

    /**
     * Transforms a `KtTypeElement` into its corresponding `IrType` representation.
     *
     * This method leverages the provided local transformation context to convert elements
     * of the Kotlin type system into their intermediate representation during compilation
     * or code analysis processes.
     *
     * @param element The `KtTypeElement` to be transformed. Represents a type-related
     *                component in the Kotlin type system, such as primitive or class types.
     * @return The transformed `IrType` representation of the input, or `null` if the
     *         transformation could not be performed.
     */
//type
    context(c: KtIrLocalTransformContext)
    open operator fun invoke(element: KtTypeElement): IrType? = c.transformIrType(element)

    /**
     * Transforms a given `KtClassTypeElement` instance into its corresponding `IrType` representation within the context.
     *
     * @param element The `KtClassTypeElement` to be transformed. Represents a model of a Kotlin class type, including its
     * package, class name, nullability, and type arguments.
     * @return The transformed `IrType` representation of the provided `KtClassTypeElement`. Returns `null` if the transformation
     * cannot be performed or the given element is invalid in the current context.
     */
    context(c: KtIrLocalTransformContext)
    open operator fun invoke(element: KtClassTypeElement): IrType? = c.transformClassIrType(element)

    /**
     * Transforms a base type element into its corresponding IR representation.
     *
     * @param element The Kotlin base type element (`KtBaseTypes`) to be transformed.
     * @return The resulting IR type (`IrType`) after the transformation, or `null` if the transformation is not applicable.
     */
    context(c: KtIrLocalTransformContext)
    open operator fun invoke(element: KtBaseTypes): IrType? = c.transformBaseIrType(element)

    /**
     * Transforms the provided Kotlin expression element into an equivalent IR (Intermediate Representation) expression.
     * Delegates the transformation to the context's `transformIrExpression` function.
     *
     * @param element The Kotlin expression element to be transformed. It represents a general
     *                expression element in the abstract syntax tree (AST) and may vary
     *                from block expressions to specific field or value access elements.
     * @return The resulting IR expression if the transformation is successful; otherwise, returns null.
     */
//expression
    context(c: KtIrLocalTransformContext)
    open operator fun invoke(element: KtExpressionElement): IrExpression? = c.transformIrExpression(element)

    /**
     * Invokes the transformation of a `KtBlockElement` into its corresponding IR (`Intermediate Representation`) block.
     * Delegates the transformation logic to the provided `KtIrLocalTransformContext`.
     *
     * @param element The `KtBlockElement` to be transformed. Represents a block in the Kotlin AST that contains
     *                a sequence of statements and expressions.
     * @return The transformed `IrBlock` representing the IR equivalent of the input block, or `null` if the transformation fails.
     */
    context(c: KtIrLocalTransformContext)
    open operator fun invoke(element: KtBlockElement): IrBlock? = c.transformIrBlock(element)

    /**
     * Transforms the provided constant Kotlin AST element (`KtConstElement`) into its corresponding
     * IR (Intermediate Representation) constant (`IrConst`).
     *
     * @param element The `KtConstElement` to be transformed. Represents a constant expression,
     *                such as a string, boolean, or other compile-time constant value in Kotlin AST.
     * @return The transformed `IrConst` representation of the input element in the IR system,
     *         or `null` if the transformation is not applicable or cannot be performed.
     */
    context(c: KtIrLocalTransformContext)
    open operator fun invoke(element: KtConstElement<*>): IrConst? = c.transformIrConst(element)

    /**
     * Transforms a `KtGetFieldElement` into an `IrGetField` representation within the provided local IR transformation context.
     *
     * @param element The `KtGetFieldElement` representing the field access expression to be transformed. Includes details about the field being accessed and its optional receiver
     *  expression.
     * @return An `IrGetField` instance if the transformation is successful, or `null` if the transformation cannot be applied.
     */
    context(c: KtIrLocalTransformContext)
    open operator fun invoke(element: KtGetFieldElement): IrGetField? = c.transformIrGetField(element)

    /**
     * Transforms a `KtGetValueElement` into its corresponding IR (`Intermediate Representation`) representation
     * by invoking the `transformIrGetValue` function within the provided transformation context.
     *
     * @param element The `KtGetValueElement` to be transformed. This represents a value access in the Kotlin AST.
     * @return The resulting `IrGetValue` object representing the value access in IR form, or `null` if the
     *         transformation cannot be performed.
     */
    context(c: KtIrLocalTransformContext)
    open operator fun invoke(element: KtGetValueElement): IrGetValue? = c.transformIrGetValue(element)

    /**
     * Transforms a `KtSetFieldElement` into an `IrSetField` representation within the context of local IR transformation.
     *
     * This method utilizes the provided transformation context to convert a field-setting expression
     * from the Kotlin AST (Abstract Syntax Tree) into its corresponding IR (Intermediate Representation) node.
     *
     * @param element The `KtSetFieldElement` representing the field-setting expression to be transformed.
     * @return The `IrSetField` representing the intermediate representation of the provided element,
     *         or `null` if the transformation could not be completed or is not applicable.
     */
    context(c: KtIrLocalTransformContext)
    open operator fun invoke(element: KtSetFieldElement): IrSetField? = c.transformIrSetField(element)

    /**
     * Transforms a [KtConstructorCallElement] into an [IrConstructorCall].
     *
     * @param element The constructor call element to be transformed. Represents a specific invocation
     * of a Kotlin constructor within the abstract syntax tree (AST).
     * @return The transformed [IrConstructorCall] representation or `null` if the transformation fails
     * or is not applicable within the current context.
     */
    context(c: KtIrLocalTransformContext)
    open operator fun invoke(element: KtConstructorCallElement): IrConstructorCall? =
        c.transformIrConstructorCall(element)

    /**
     * Invokes a transformation on the given delegating constructor call element to produce an IR (Intermediate Representation)
     * for a delegating constructor call.
     *
     * @param element The delegating constructor call element to be transformed.
     * @return The resulting [IrDelegatingConstructorCall] if the transformation is successful, or null if the transformation cannot be performed.
     */
    context(c: KtIrLocalTransformContext)
    open operator fun invoke(element: KtDelegatingConstructorCallElement): IrDelegatingConstructorCall? =
        c.transformIrDelegatingConstructorCall(element)

    /**
     * Transforms a given `KtIrInitExpressionElement` into its corresponding `IrExpression`
     * within the provided `KtIrLocalTransformContext`.
     *
     * @param element The `KtIrInitExpressionElement` representing the IR-backed expression
     *                element that needs to be transformed.
     * @return An `IrExpression` representing the transformed intermediate representation
     *         of the provided element, or `null` if the transformation cannot be performed.
     */
    context(c: KtIrLocalTransformContext)
    open operator fun invoke(element: KtIrInitExpressionElement): IrExpression? = c.transformIrInitExpression(element)

    /**
     * Invokes the transformation of a Kotlin statement element into its corresponding intermediate representation (IR).
     *
     * @param element The Kotlin statement element to be transformed. This could represent various types of statements
     *                in the Kotlin AST (Abstract Syntax Tree), encapsulated by [KtStatementElement].
     * @return The corresponding [IrStatement] instance if the transformation is successful, or `null` if the input
     *         cannot be transformed.
     */
//statement
    context(c: KtIrLocalTransformContext)
    open operator fun invoke(element: KtStatementElement): IrStatement? = c.transformIrStatement(element)

    /**
     * Processes a given `return` statement element and transforms it into an IR (Intermediate Representation) return statement.
     *
     * @param element The `KtReturnStatementElement` representing a `return` statement in the Kotlin abstract syntax tree (AST).
     * It may include an optional expression representing the returned value or a target indicating the destination of the return.
     * @return An `IrReturn` instance representing the transformed return statement in IR, or `null` if the transformation is not applicable.
     */
    context(c: KtIrLocalTransformContext)
    open operator fun invoke(element: KtReturnStatementElement): IrReturn? = c.transformIrReturn(element)

    /**
     * Invokes the transformation of a `KtExpressionStatement` into an `IrExpression` within the
     * provided `KtIrLocalTransformContext`.
     *
     * @param element The `KtExpressionStatement` representing a single expression as a statement in
     *        the Kotlin abstract syntax tree (AST). This element is transformed into its corresponding
     *        intermediate representation.
     * @return An `IrExpression` resulting from the transformation of the given `element`, or `null`
     *         if the transformation is not applicable or fails.
     */
    context(c: KtIrLocalTransformContext)
    open operator fun invoke(element: KtExpressionStatement): IrExpression? = c.transformIrStatementExpression(element)

    /**
     * Transforms the provided `KtIrInitStatementElement` into an `IrStatement` within the context
     * of the given transformation logic.
     *
     * @param element The `KtIrInitStatementElement` representing the statement element to be transformed.
     * @return An `IrStatement` resulting from the transformation, or `null` if the transformation could not be completed.
     */
    context(c: KtIrLocalTransformContext)
    open operator fun invoke(element: KtIrInitStatementElement): IrStatement? = c.transformIrInitStatement(element)

    /**
     * Transforms a given `KtParameterElement` into its corresponding `IrValueParameter` representation
     * using the provided transformation context.
     *
     * @param element The `KtParameterElement` to be transformed.
     * @return The resulting `IrValueParameter` if the transformation is successful, or `null` if it fails.
     */
//other
    context(c: KtIrLocalTransformContext)
    open operator fun invoke(element: KtParameterElement): IrValueParameter? = c.transformIrParameter(element)

}