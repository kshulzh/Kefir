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

import io.github.kshulzh.kefir.model.api.KtAttributes
import io.github.kshulzh.kefir.model.api.KtElement
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
 * An interface for visiting Kotlin elements with no specific return value (Unit).
 * This visitor allows processing elements based on their type and associated data.
 * The generic type parameter [D] allows passing additional information during the visiting process.
 */
interface KtVoidVisitor<D>: KtVisitor<Unit, D> {
    /**
     * Visits a generic Kotlin element within the model.
     *
     * @param element The Kotlin element to be visited. Represents the base type for various specialized elements.
     * @param data Additional data or context required for visiting the element.
     */
    override fun visitElement(element: KtElement, data: D) {}

    /**
     * Visits the attributes of a Kotlin element and performs operations based on the provided data.
     *
     * @param element the element that supports a collection of attributes to be visited
     * @param data additional contextual information or state passed during the visitation
     */
// Base and scopes
    override fun visitAttributes(element: KtAttributes, data: D) {}
    /**
     * Visits an external Kotlin element, allowing custom behavior to be applied to elements
     * defined outside the primary Kotlin source.
     *
     * This method is intended to process elements represented by [KtExternalElement], which
     * typically originate from external libraries, dynamically generated sources, or other
     * non-primary contexts.
     *
     * @param element the external Kotlin element to be visited.
     * @param data additional contextual data to guide the processing or behavior during the visit.
     */
    override fun visitExternal(element: KtExternalElement, data: D) {}

    /**
     * Visits a `KtAnnotationElement` as part of the Kotlin model structure traversal.
     *
     * @param element The annotation element being visited. Represents a specific annotation
     *                in the Kotlin code structure, including its type and associated annotation scope.
     * @param data    Additional contextual data passed along during the visitor's traversal.
     */
// Annotation
    override fun visitAnnotation(element: KtAnnotationElement, data: D) {}
    /**
     * Visits the scope containing annotations in the Kotlin model structure.
     *
     * This method is invoked to process a `KtAnnotationsScope`, which represents a collection
     * of annotations applied to a specific element in the Kotlin code model. The implementation
     * can include logic to traverse or analyze the annotations within this scope.
     *
     * @param element The `KtAnnotationsScope` instance representing the annotation scope to be visited.
     * @param data Additional contextual information of type `D` that may be used during the visit process.
     */
//scopes
    override fun visitAnnotationsScope(element: KtAnnotationsScope, data: D) {}

    /**
     * Visits a parameter element in the Kotlin code structure.
     *
     * @param element The parameter element being visited.
     * @param data Additional data or context passed during the visit.
     */
// Arguments/parameter
    override fun visitParameter(element: KtParameterElement, data: D) {}
    /**
     * Visits a scope containing argument elements in the Kotlin model.
     *
     * This method is invoked when traversing or analyzing a `KtArgumentsScope`, which represents
     * a collection of argument elements associated with constructs such as function calls,
     * annotations, or other contexts that define and pass arguments.
     *
     * @param element The `KtArgumentsScope` instance representing the scope of arguments
     *                to be visited. This scope contains argument elements and their associated
     *                metadata, such as names and values.
     * @param data    The context-specific data required for visiting or analyzing the
     *                `KtArgumentsScope`. This data may be used to carry information necessary
     *                for processing the elements within the scope.
     */
//scopes
    override fun visitArgumentsScope(element: KtArgumentsScope, data: D) {}
    /**
     * Visits a given `KtParametersScope` element within the Kotlin model.
     *
     * This method is invoked to perform an operation or analysis on a `KtParametersScope`,
     * which represents a scope containing parameter elements in the Kotlin structure.
     * The operation is typically context-specific and may include traversing or processing
     * the parameters contained within the scope.
     *
     * @param element the `KtParametersScope` instance being visited, which contains the parameter elements.
     * @param data a contextual object of type `D` that may be used to carry additional information
     *        or state required for processing the scope.
     */
    override fun visitParametersScope(element: KtParametersScope, data: D) {}

    /**
     * Visits a Kotlin declaration element during traversal of the Kotlin model structure.
     *
     * This method is called for any element that implements the `KtDeclarationElement` interface.
     * It provides a way to process or analyze declarations, such as classes, properties, fields,
     * and functions, as part of a visitor pattern implementation.
     *
     * @param element The declaration element being visited. Represents a structural component
     *                within the Kotlin model, such as a class, property, or function.
     * @param data Additional data that can be passed to the visitor during traversal
     *             to maintain context or state.
     */
// Declarations
    override fun visitDeclaration(element: KtDeclarationElement, data: D) {}
    /**
     * Visits a class element in the Kotlin model structure.
     *
     * This method processes a `KtClassElement`, which represents a class declaration
     * along with its associated information, such as supertypes, annotations, and enclosed
     * declarations.
     *
     * @param element the class element being visited. It encapsulates the structure and
     *                characteristics of a Kotlin class, including its supertypes and declarations.
     * @param data additional contextual information or payload passed to the visitor.
     */
    override fun visitClass(element: KtClassElement, data: D) {}
    /**
     * Visits a Kotlin constructor element within the Kotlin model structure.
     *
     * This method is invoked to handle logic specific to a `KtConstructorElement` during
     * traversal of the Kotlin model.
     *
     * @param element the Kotlin constructor element being visited.
     * @param data additional data required for processing the element.
     */
    override fun visitConstructor(element: KtConstructorElement, data: D) {}
    /**
     * Visits a field element in the Kotlin model structure.
     *
     * This method is invoked for instances of `KtFieldElement` and is used to process or analyze field declarations in a Kotlin program.
     *
     * @param element The `KtFieldElement` instance representing the field to be visited. This object encapsulates details about the field, including its type, value, and any associated
     *  modifiers or annotations.
     * @param data Additional contextual information of type `D` that may be required during the visit operation.
     */
    override fun visitField(element: KtFieldElement, data: D) {}
    /**
     * Visits a Kotlin function element within the model structure.
     *
     * This method is invoked for processing or analyzing a `KtFunctionElement`,
     * which represents a function and its definitions, such as its name, parameters,
     * body, return type, and annotations. Implementations of this method
     * can define specific behaviors for handling function elements.
     *
     * @param element The function element being visited. Represents the structure
     * of the function, including its parameters, return type, and body.
     * @param data Additional data passed during the visitation, which might be
     * used to store contextual or processing-related information.
     */
    override fun visitFunction(element: KtFunctionElement, data: D) {}
    /**
     * Visits a property element in the Kotlin model structure.
     *
     * This method is invoked to handle processing or analysis of a `KtPropertyElement`.
     * The element represents a property with optional type, backing field, getter, setter,
     * and annotations in the model. Custom logic can be applied to examine, modify, or interact
     * with the property element during traversal.
     *
     * @param element The property element being visited. It models a Kotlin property, including its type,
     *                backing field, accessors, and annotations.
     * @param data Additional data or context information that may be required during the visitation
     *             process. The type of this parameter depends on the specific implementation.
     */
    override fun visitProperty(element: KtPropertyElement, data: D) {}
    /**
     * Visits the specified declarations scope within the Kotlin program structure.
     *
     * This method is invoked to process a Kotlin declarations scope represented by
     * the [KtDeclarationsScope] interface. A declarations scope generally contains
     * various Kotlin declaration elements, such as classes, functions, fields, and properties.
     * The provided data can be used during the visitation process for custom operations
     * or transformations.
     *
     * @param element The Kotlin declarations scope to be visited, represented by [KtDeclarationsScope].
     * @param data Additional data passed to the visitor for use during the visitation process.
     */
//scopes
    override fun visitDeclarationsScope(element: KtDeclarationsScope, data: D) {}

    /**
     * Visits a Kotlin file element within a package scope.
     *
     * This method is invoked to process or handle the specified file element,
     * enabling operations such as analysis, transformation, or traversal of
     * the element and its associated declarations, annotations, and package context.
     *
     * @param element The Kotlin file element to be visited.
     * @param data Additional data passed to the visitor, which can be used
     *             for contextual operations or accumulating results.
     */
// IO / package
    override fun visitFile(element: KtFileElement, data: D) {}
    /**
     * Visits a Kotlin package element during traversal of the package hierarchy.
     *
     * This method is invoked to process an instance of `KtPackageElement`, which represents
     * a package within the Kotlin package structure. It can be used to analyze, modify, or
     * collect information about the package and its associated elements.
     *
     * @param element The `KtPackageElement` being visited, representing a specific package
     *                in the Kotlin package hierarchy.
     * @param data    An additional parameter of type `D` that can be used to pass contextual
     *                information or state between visit operations.
     */
    override fun visitPackage(element: KtPackageElement, data: D) {}
    /**
     * Visits a `KtPackageScopeElement` within the Kotlin package scope.
     *
     * This method is invoked for elements that represent structural entities
     * within a package, such as files or subpackages. Provides a way to perform
     * operations, analysis, or transformations on these elements as part of a
     * traversal through the package scope hierarchy.
     *
     * @param element the `KtPackageScopeElement` being visited, which represents
     *                an individual component within the package scope, such as
     *                a file or subpackage.
     * @param data contextual information or state passed along during the visit,
     *             allowing for customized processing logic.
     */
    override fun visitPackageScopeElement(element: KtPackageScopeElement, data: D) {}
    /**
     * Visits the given `KtPackageScope` element and processes it.
     *
     * This method is part of the visitor pattern implementation for traversing
     * and handling elements within the Kotlin package scope, allowing customized
     * behavior to be defined for package scope elements during traversal.
     *
     * @param element The `KtPackageScope` element being visited, representing the scope
     *                of a Kotlin package and its elements.
     * @param data Additional data or context of type `D` that may be used during the visit.
     */
//scopes
    override fun visitPackageScope(element: KtPackageScope, data: D) {}

    /**
     * Visits a Kotlin statement element within the abstract syntax tree (AST).
     *
     * This method provides traversal or processing logic specific to elements that
     * represent statements in the Kotlin AST. It can be overridden to implement
     * custom behavior for handling statement elements during AST traversal.
     *
     * @param element The statement element being visited. Represents a concrete
     *                instance of [KtStatementElement] within the Kotlin AST.
     * @param data    Additional data provided during the visit, which can
     *                be used to carry contextual information or instructions
     *                for processing the element.
     */
// Statements
    override fun visitStatement(element: KtStatementElement, data: D) {}
    /**
     * Processes a `return` statement element within the Kotlin abstract syntax tree (AST).
     *
     * This method is invoked to handle a `return` statement, including its associated expression
     * and target context, if specified. It provides a way to traverse or operate on the structure
     * and semantics of the return statement during AST analysis or transformation.
     *
     * @param element The `KtReturnStatementElement` representing the `return` statement in the AST
     *                to be visited. This element may include an optional expression being returned
     *                and an optional target context.
     * @param data Contextual data passed along during the traversal or visitation process.
     */
    override fun visitReturnStatement(element: KtReturnStatementElement, data: D) {}

    /**
     * Visits a general Kotlin expression element within the abstract syntax tree (AST).
     *
     * This method is invoked for instances of [KtExpressionElement], allowing specific handling
     * or processing of expression-related nodes. Expressions may include complex constructs,
     * such as function calls and operations, or simpler constructs like constants or variable references.
     *
     * @param element The expression element in the Kotlin abstract syntax tree being visited.
     *                Provides structural and type-related information about the specific expression.
     * @param data Additional data passed to the visitor, which can be used for custom processing
     *             or carrying state during the traversal of the AST.
     */
// Expressions
    override fun visitExpression(element: KtExpressionElement, data: D) {}
    /**
     * Visits a block element within the Kotlin abstract syntax tree (AST).
     *
     * A block element typically represents a sequence of statements and expressions,
     * which are evaluated in a specific order. This method provides a mechanism to interact
     * with or process block elements during a traversal or analysis of the AST.
     *
     * @param element The block element being visited.
     * @param data Additional data or context required for processing the block element.
     */
    override fun visitBlock(element: KtBlockElement, data: D) {}
    /**
     * Visits a constant expression element within the Kotlin abstract syntax tree (AST).
     *
     * This method is invoked for elements of type [KtConstElement], which represent
     * constant expressions such as strings, numbers, or boolean values. These constants
     * are evaluated or resolved at compile time.
     *
     * @param element The constant expression element being visited.
     * @param data Additional data passed to the visitor during traversal.
     */
    override fun visitConst(element: KtConstElement<*>, data: D) {}
    /**
     * Visits a `KtConstructorCallElement` within the Kotlin abstract syntax tree (AST).
     * This method is invoked to process or analyze a constructor call expression, which
     * represents the invocation of a constructor for class instantiation.
     *
     * @param element the `KtConstructorCallElement` representing the constructor call in the AST.
     *                This element provides details about the invoked constructor and its arguments.
     * @param data additional data of type `D` passed during the visit, useful for carrying
     *             context or state information needed for the processing of the element.
     */
    override fun visitConstructorCall(element: KtConstructorCallElement, data: D) {}
    /**
     * Visits a delegating constructor call element in the Kotlin abstract syntax tree (AST).
     *
     * A delegating constructor call represents a call to another constructor within the same class
     * or a parent class, used for delegating initialization logic.
     *
     * @param element The delegating constructor call element being visited.
     * @param data Additional data or context passed to this visit operation.
     */
    override fun visitDelegatingConstructorCall(element: KtDelegatingConstructorCallElement, data: D) {}
    /**
     * Visits a `KtGetFieldElement` which represents an expression element that retrieves
     * the value of a specified field in the Kotlin abstract syntax tree (AST).
     *
     * @param element the `KtGetFieldElement` instance being visited, representing a field
     *                access operation that may include an associated receiver and field.
     * @param data    additional data passed to the visitor, typically used to carry
     *                state or context information during the visitation process.
     */
    override fun visitGetField(element: KtGetFieldElement, data: D) {}
    /**
     * Visits a `KtGetValueElement` and performs operations on the given element and associated data.
     *
     * @param element The `KtGetValueElement` instance to be visited.
     * @param data Additional data or context required for processing the given element.
     */
    override fun visitGetValue(element: KtGetValueElement, data: D) {}
    /**
     * Visits a Kotlin `if` expression element within the abstract syntax tree (AST).
     *
     * This method provides a mechanism to process or analyze an `if` construct, which represents
     * conditional branching in Kotlin code. The `if` element includes a condition, and optional
     * true and false branches (ifBody and elseBody).
     *
     * @param element The `KtIfElement` instance representing an `if` expression in the Kotlin AST.
     *                This includes the associated condition, true branch, and else branch if present.
     * @param data Additional data or state passed to the visitor during the traversal process. It
     *             can be used to maintain context or gather results during the visit.
     */
    override fun visitIf(element: KtIfElement, data: D) {}
    /**
     * Visits a field-setting expression within the Kotlin abstract syntax tree (AST).
     *
     * This method is called when encountering a `KtSetFieldElement`, representing an assignment
     * operation to set a value for a field, possibly with a receiver. It can be used to analyze
     * or process such expressions during tree traversal.
     *
     * @param element The `KtSetFieldElement` instance representing the field-setting operation in the AST.
     * @param data Additional contextual data passed during the visitor's traversal.
     */
    override fun visitSetField(element: KtSetFieldElement, data: D) {}

    /**
     * Visits a type element in the Kotlin program structure.
     *
     * @param element the type element being visited. Represents an abstraction for type-related elements in Kotlin.
     * @param data additional contextual information or data required during the visiting process.
     */
// Types
    override fun visitType(element: KtTypeElement, data: D) {}
    /**
     * Visits a class type element in the Kotlin type system.
     *
     * This method is called when traversing or analyzing a `KtClassTypeElement`,
     * which represents a specific class type within Kotlin's type model. Class type
     * elements include information about the package, class name, nullability,
     * and any associated type arguments.
     *
     * @param element The class type element being visited. This represents a model of
     *                a class type in the Kotlin type system, including its package and class path,
     *                nullability, and type arguments.
     * @param data    Additional data passed during the visit, which can be used to
     *                provide context or carry information needed for processing the class type.
     */
    override fun visitClassType(element: KtClassTypeElement, data: D) {}
    /**
     * Visits a parameterized type element in the Kotlin code structure.
     *
     * @param element the parameter type element to be processed
     * @param data additional data that may be required during the visit
     */
    override fun visitParameterType(element: KtParameterTypeElement, data: D) {}
    /**
     * Visits a type parameter element in the Kotlin type system.
     *
     * This method is responsible for processing instances of [KtTypeParameterElement],
     * which represent type parameters in the Kotlin language. Type parameters allow
     * for generic type declarations and can have constraints, annotations, and variance
     * modifiers. The visitor pattern enables the traversal and handling of such elements
     * within a larger hierarchy of elements.
     *
     * @param element The [KtTypeParameterElement] instance to be visited. This element
     * represents a type parameter with optional annotations, supertypes, and variance
     * information.
     * @param data Additional contextual information or state to be carried during the
     * visit. The type of this parameter is generic and can be tailored to the specific
     * processing logic needed in the visitor's implementation.
     */
    override fun visitTypeParameter(element: KtTypeParameterElement, data: D) {}
    /**
     * Visits a type argument scope in the Kotlin type system.
     *
     * This method is invoked when traversing or analyzing a `KtTypeArgumentScope`,
     * which represents a scope containing type arguments in a generic type context.
     *
     * @param element The `KtTypeArgumentScope` instance being visited.
     * @param data Additional data or context used during the visit process.
     */
//scopes
    override fun visitTypeArgumentScope(element: KtTypeArgumentScope, data: D) {}
    /**
     * Visits a type parameter scope element in the Kotlin syntax tree.
     *
     * This method is called when traversing or processing a `KtTypeParameterScope`,
     * which represents a collection of type parameters within a specific scope context.
     *
     * @param element The type parameter scope element being visited.
     * @param data Additional contextual information or data used during the visit.
     */
    override fun visitTypeParameterScope(element: KtTypeParameterScope, data: D) {}
}