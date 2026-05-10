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

package io.github.kshulzh.kefir.model.api.utils

import io.github.kshulzh.kefir.model.api.KtElement
import io.github.kshulzh.kefir.model.api.KtAttributes
import io.github.kshulzh.kefir.model.api.KtExternalElement
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationElement
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationsScope
import io.github.kshulzh.kefir.model.api.arg.KtArgumentsScope
import io.github.kshulzh.kefir.model.api.arg.KtParameterElement
import io.github.kshulzh.kefir.model.api.arg.KtParametersScope
import io.github.kshulzh.kefir.model.api.declaration.KtClassElement
import io.github.kshulzh.kefir.model.api.declaration.KtConstructorElement
import io.github.kshulzh.kefir.model.api.declaration.KtDeclarationElement
import io.github.kshulzh.kefir.model.api.declaration.KtDeclarationsScope
import io.github.kshulzh.kefir.model.api.declaration.KtFieldElement
import io.github.kshulzh.kefir.model.api.declaration.KtFunctionElement
import io.github.kshulzh.kefir.model.api.declaration.KtPropertyElement
import io.github.kshulzh.kefir.model.api.expression.KtBlockElement
import io.github.kshulzh.kefir.model.api.expression.KtConstElement
import io.github.kshulzh.kefir.model.api.expression.KtConstructorCallElement
import io.github.kshulzh.kefir.model.api.expression.KtDelegatingConstructorCallElement
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.expression.KtGetFieldElement
import io.github.kshulzh.kefir.model.api.expression.KtGetValueElement
import io.github.kshulzh.kefir.model.api.expression.KtIfElement
import io.github.kshulzh.kefir.model.api.expression.KtSetFieldElement
import io.github.kshulzh.kefir.model.api.io.KtFileElement
import io.github.kshulzh.kefir.model.api.io.KtPackageElement
import io.github.kshulzh.kefir.model.api.io.KtPackageScope
import io.github.kshulzh.kefir.model.api.io.KtPackageScopeElement
import io.github.kshulzh.kefir.model.api.statement.KtReturnStatementElement
import io.github.kshulzh.kefir.model.api.statement.KtStatementElement
import io.github.kshulzh.kefir.model.api.type.KtClassTypeElement
import io.github.kshulzh.kefir.model.api.type.KtParameterTypeElement
import io.github.kshulzh.kefir.model.api.type.KtTypeArgumentScope
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.api.type.KtTypeParameterElement
import io.github.kshulzh.kefir.model.api.type.KtTypeParameterScope

/**
 * Visitor interface for traversing and processing elements of the Kotlin model.
 * Defines methods to visit various types of Kotlin elements and optionally carry data
 * during the visit process. Supports a generic result type `R` and a generic parameter
 * type `D` for flexibility in data processing.
 *
 * @param R The return type of the visit methods.
 * @*/
interface KtVisitor<out R, in D> {
    /**
     * Visits a Kotlin element and applies the visitor logic to it.
     *
     * @param element The Kotlin element to be visited.
     * @param data Additional data required for the visitor's operation.
     * @return The result of visiting the element.
     */
    fun visitElement(element: KtElement, data: D): R = element.accept(this, data)

    /**
     * Visits a given `KtAttributes` element, which represents an element capable of storing attributes in a mutable map.
     * Delegates the actual handling of the element to the `visitElement` method.
     *
     * @param element The `KtAttributes` element to be visited. It represents an element that supports associated attributes.
     * @param data Additional data of type `D` used*/
// Base and scopes
    fun visitAttributes(element: KtAttributes, data: D): R = visitElement(element, data)
    /**
     * Visits a Kotlin external element within the model.
     *
     * @param element The external Kotlin element being visited. Typically represents an element
     *                originating from external sources, such as libraries or generated code.
     * @param data    Additional data of type D passed to the visitor, providing context or state
     *                to assist in processing the element.
     * @return A result of*/
    fun visitExternal(element: KtExternalElement, data: D): R = visitElement(element, data)

    /**
     * Visits a `KtAnnotationElement` in the Kotlin model structure.
     *
     * This method processes annotations and allows for custom logic to be implemented
     * when an annotation element is encountered during traversal.
     *
     * @param element the `KtAnnotationElement` instance representing the target annotation.
     * @param data the additional data or context passed to the visitor during traversal.
     * @return the result*/
// Annotation
    fun visitAnnotation(element: KtAnnotationElement, data: D): R = visitElement(element, data)
    /**
     * Visits a scope containing annotations in the Kotlin model structure.
     *
     * This method is called for elements that implement the `KtAnnotationsScope` interface. It enables
     * processing or traversal of annotation-related scopes in the model.
     *
     * @param element The `KtAnnotationsScope` element representing a scope of annotations to be visited.
     * @param data Contextual or additional data of type `*/
//scopes
    fun visitAnnotationsScope(element: KtAnnotationsScope, data: D): R = visitElement(element, data)

    /**
     * Visits a parameter element within the Abstract Syntax Tree (AST).
     *
     * @param element The parameter element to be visited.
     * @param data Additional context or data required for the visit operation.
     * @return The result of the visit operation.
     */
// Arguments/parameter
    fun visitParameter(element: KtParameterElement, data: D): R = visitElement(element, data)
    /**
     * Visits a `KtArgumentsScope` element in the Kotlin model.
     *
     * This method is used to traverse or process a scope containing argument elements,
     * such as those defined within function calls, annotations, or similar constructs in Kotlin.
     *
     * @param element The `KtArgumentsScope` instance representing the argument scope to be visited.
     * @param data The additional context or data*/
//scopes
    fun visitArgumentsScope(element: KtArgumentsScope, data: D): R = visitElement(element, data)
    /**
     * Visits a `*/
    fun visitParametersScope(element: KtParametersScope, data: D): R = visitElement(element, data)

    /**
     * Visits a declaration element in the Kotlin model structure.
     *
     * This method is invoked to perform operations or transformations
     * on elements representing declarations, such as classes, properties,
     * and functions. It delegates the handling to the `visitElement` method
     * in the base class.
     *
     * @param element The `KtDeclarationElement` to be visited. It represents
     **/
// Declarations
    fun visitDeclaration(element: KtDeclarationElement, data: D): R = visitElement(element, data)
    /**
     * Visits a class element in the Kotlin model structure.
     *
     * This method is responsible for processing or interpreting a `KtClassElement`,
     * delegating the handling to the `visitDeclaration` implementation for further processing.
     *
     * @param element The class element to visit, encapsulating the structure and characteristics of a Kotlin class.
     * @*/
    fun visitClass(element: KtClassElement, data: D): R = visitDeclaration(element, data)
    /**
     * Visits a Kotlin constructor element within the Kotlin model structure.
     *
     * This method processes a `KtConstructorElement`, which represents a specialized
     * declaration element for constructor logic, by delegating*/
    fun visitConstructor(element: KtConstructorElement, data: D): R = visitDeclaration(element, data)
    /**
     * Visits a field element within the Kotlin structure.
     *
     * This method is designed to process or analyze a specific field element, represented by [KtFieldElement],
     * along with the associated data of type [D]. It provides a way to traverse or perform operations on a field
     * declaration within the Kotlin model. The behavior for this method is inherited and specialized from the
     */
    fun visitField(element: KtFieldElement, data: D): R = visitDeclaration(element, data)
    /**
     * Visits a function*/
    fun visitFunction(element: KtFunctionElement, data: D): R = visitDeclaration(element, data)
    /**
     * Visits a property element in the Kotlin model structure.
     *
     * This method is invoked for processing or analyzing a `KtPropertyElement`. It delegates
     * the handling of the property element to the `visitDeclaration` method, which provides
     * base-level behavior for visiting declaration elements.
     *
     * @param element the `KtPropertyElement` representing the property to be visited.
     */
    fun visitProperty(element: KtPropertyElement, data: D): R = visitDeclaration(element, data)
    /**
     * Visits a declarations*/
//scopes
    fun visitDeclarationsScope(element: KtDeclarationsScope, data: D): R = visitElement(element, data)

    /**
     * Visits a Kotlin file element and performs an operation defined by the visitor.
     *
     * @param element The file element being visited, which represents a Kotlin file in the package scope.
     * @param data Additional data or context required for the visit operation.
     * @return The result of the visit operation, determined by the implementation of the visitor.
     */
// IO / package
    fun visitFile(element: KtFileElement, data: D): R = visitElement(element, data)
    /**
     * Visits a `KtPackageElement` within the Kotlin package hierarchy and performs an operation
     * defined by the specific implementation of the visitor.
     *
     * @param element The `KtPackageElement` to be visited. Represents a package in the Kotlin hierarchy,
     *                which may contain subpackages or file elements.
     * @param data The additional data passed to the visitor to contextualize the processing of the element*/
    fun visitPackage(element: KtPackageElement, data: D): R = visitElement(element, data)
    /**
     * Visits a `KtPackageScopeElement` within the Kotlin package scope hierarchy.
     *
     * This method is invoked to process or analyze the provided `KtPackageScopeElement`,
     * which could represent a file or a subpackage within a broader package scope.
     * It delegates the visitation to `visitElement` for further handling and traversal
     * logic, potentially interacting with its parent or sibling package scope elements.
     *
     * @*/
    fun visitPackageScopeElement(element: KtPackageScopeElement, data: D): R = visitElement(element, data)
    /**
     * Visits a `KtPackageScope` element and processes it with the given data.
     *
     * @param element The `KtPackageScope` instance representing the scope of a Kotlin package,
     *                containing subpackages and file elements for processing.
     * @param data    Additional data used during the visit operation.
     * @return The result of processing the `KtPackage*/
//scopes
    fun visitPackageScope(element: KtPackageScope, data: D): R = visitElement(element, data)

    /**
     * Visits a statement element in the Kotlin abstract syntax tree (AST).
     *
     * This method allows traversal or processing of a [KtStatementElement]
     * and delegates to the [visitElement] function for general handling.
     *
     * @param element The statement element to be visited. It represents a statement node
     *                in the AST and extends the [KtElement] interface.
     * @param data Additional data required for the visit operation. Its type is generic
     *             and*/
// Statements
    fun visitStatement(element: KtStatementElement, data: D): R = visitElement(element, data)
    /**
     *
     */
    fun visitReturnStatement(element: KtReturnStatementElement, data: D): R = visitStatement(element, data)

    /**
     * Visits a general Kotlin expression element in the abstract syntax tree (AST).
     * This method provides a way to process or transform an expression element
     * by delegating to the `visitStatement` method.
     *
     * @param element The Kotlin expression element being visited. This element is
     *                a basic unit of expressions in the Kotlin syntax model and
     *               */
// Expressions
    fun visitExpression(element: KtExpressionElement, data: D): R = visitStatement(element, data)
    /**
     * Visits a block element in the Kotlin abstract syntax tree (AST).
     * A block element typically consists of a scope encapsulating a sequence
     * of statements and expressions.
     *
     * @param element the [KtBlockElement] instance representing the block to visit
     * @param data additional contextual data used during the visitation
     * @return the result of processing the block element
     */
    fun visitBlock(element: KtBlockElement, data: D): R = visitExpression(element, data)
    /**
     * Visits a constant expression element within the Kotlin abstract syntax tree (AST).
     * This method delegates to the `visitExpression` method to handle the processing of the constant.
     *
     * @param element The constant expression element to visit. Represents a compile-time evaluable constant.
     * @param data Additional data or context required for processing the element.
     * @return The*/
    fun visitConst(element: KtConstElement<*>, data: D): R = visitExpression(element, data)
    /**
     * Visits a constructor call element in the Kotlin abstract syntax tree (AST).
     * Delegates the call to visitExpression for further processing.
     *
     * @param element The [KtConstructorCallElement] representing the constructor call to be visited.
     * @param data The contextual data of type [D] to be passed along during the visit.
     * @return The result of the visit*/
    fun visitConstructorCall(element: KtConstructorCallElement, data: D): R = visitExpression(element, data)
    /**
     * Visits a delegating constructor call element within the Kotlin abstract syntax tree (AST).
     *
     * A delegating constructor call is an expression used to invoke another constructor
     * either within the same class or a parent class for the purpose of initialization.
     *
     * @param element the delegating constructor call element to be visited.
     * @param data additional data passed to the visitor during the visit.
     * @return the result of processing the delegating constructor call element.
     */
    fun visitDelegatingConstructorCall(element: KtDelegatingConstructorCallElement, data: D): R = visitExpression(element, data)
    /**
     * Visits a `KtGetFieldElement` representing a field access operation in the Kotlin AST.
     *
     * This*/
    fun visitGetField(element: KtGetFieldElement, data: D): R = visitExpression(element, data)
    /**
     * Visits a `KtGetValueElement` instance and processes it with the provided data.
     *
     * @param element the `KtGetValueElement` instance to be visited.
     * @param data the context-specific data to be used during the visit.
     * @return the result of processing the `KtGetValueElement`.
     */
    fun visitGetValue(element: KtGetValueElement, data: D): R = visitExpression(element, data)
    /**
     * Visits an `if` expression element in the Kotlin abstract syntax tree (AST).
     *
     * This method provides a mechanism for traversing and processing an `if` construct,
     * represented by a [KtIfElement], within the Kotlin AST. The method is typically
     * overridden to perform custom operations on `if` elements during AST traversal.
     *
     * @*/
    fun visitIf(element: KtIfElement, data: D): R = visitExpression(element, data)
    /**
     * Visits a `KtSetFieldElement`, which represents a field-setting expression in the Kotlin abstract syntax tree (AST).
     * This method delegates to [visitExpression] to handle the common behavior for expression elements.
     *
     * @param element the `KtSetFieldElement` instance being visited; it models a field assignment operation
     *                with potential receiver and value*/
    fun visitSetField(element: KtSetFieldElement, data: D): R = visitExpression(element, data)

    /**
     * Visits a Kotlin type element within the syntax tree.
     *
     * This method processes instances of [KtTypeElement], providing a mechanism for performing specific
     * operations or transformations on type-related elements in the Kotlin intermediate representation.
     *
     * @param element the type element to be visited, representing type-related abstractions such as
     * primitive types, class types, or user-defined*/
// Types
    fun visitType(element: KtTypeElement, data: D): R = visitElement(element, data)
    /**
     * Visits a class type element within the Kotlin type system.
     *
     * This*/
    fun visitClassType(element: KtClassTypeElement, data: D): R = visitType(element, data)
    /**
     * Visits a parameter type element within the Kotlin Abstract Syntax Tree.
     *
     * @param element the parameter type element being visited.
     * @param data additional data required for the visit operation.
     * @return the result of the visit operation.
     */
    fun visitParameterType(element: KtParameterTypeElement, data: D): R = visitType(element, data)
    /**
     * Visits a type parameter element in the Kotlin type system.
     *
     * This method provides a mechanism to process a type parameter element by delegating
     * the action to the `visitType` method, which serves as a base handler for all type-related elements.
     *
     * @param element The type parameter element to be visited. Represents a type parameter in the Kotlin type*/
    fun visitTypeParameter(element: KtTypeParameterElement, data: D): R = visitType(element, data)
    /**
     * Visits a scope that holds type arguments within the Kotlin type system.
     *
     * This method is invoked when traversing or processing a `KtTypeArgumentScope`,
     * which represents a collection of type arguments associated with a generic type declaration.
     * The function is typically used in the context of syntax tree analysis or transformations.
     *
     * @param element The `KtTypeArgumentScope*/
//scopes
    fun visitTypeArgumentScope(element: KtTypeArgumentScope, data: D): R = visitElement(element, data)
    /**
     * Visits the specified `KtTypeParameterScope` element and processes it.
     *
     * @param element The `KtTypeParameterScope` element to be visited. Represents a scope
     * containing a collection of Kotlin type parameters.
     * @param data Additional context or information required to process this element.
     * @return The result of visiting the `KtTypeParameterScope`, as determined by the visitor.
     */
    fun visitTypeParameterScope(element: KtTypeParameterScope, data: D): R = visitElement(element, data)
}

/**
 **/
fun <E : KtElement,D, R> E?.visitNullable(visitor: KtVisitor<R,D>, data: D) :R? {
    return this?.accept(visitor, data)
}

/**
 * Applies a visitor operation to each element of the list and returns a new list with the results.
 *
 * @param visitor The visitor instance used to process each element in the list.
 **/
fun <E : KtElement,D, R> List<E>.visit(visitor: KtVisitor<R,D>, data: D) : List<R> {
    return this.map { it.accept(visitor, data) }
}

/**
 * Visits each element in the set by applying the specified visitor and data, returning the results
 **/
fun <E : KtElement,D, R> Set<E>.visit(visitor: KtVisitor<R,D>, data: D) : List<R> {
    return this.map { it.accept(visitor, data) }
}

/**
 * Visits each nullable element in the list using the provided visitor and data, mapping the results to a new list.
 *
 * @param visitor The visitor used to process each non-null element in the list.
 * @param data The contextual data passed to the visitor during the visit.
 * @return A list of results produced by applying the visitor to each non-null element*/
fun <E : KtElement,D, R> List<E?>.visitNullable(visitor: KtVisitor<R,D>, data: D) : List<R?> {
    return this.map { it?.accept(visitor, data) }
}