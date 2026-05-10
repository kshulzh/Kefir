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

interface KtChildVisitor<D>: KtVoidVisitor<D> {
    override fun visitElement(element: KtElement, data: D) {}

    // Base and scopes
    override fun visitAttributes(element: KtAttributes, data: D) {}
    override fun visitExternal(element: KtExternalElement, data: D) {}

    // Annotation
    override fun visitAnnotation(element: KtAnnotationElement, data: D) {
        element.type.accept(this, data)
        element.arguments.visitNullable(this, data)
    }
    //scopes
    override fun visitAnnotationsScope(element: KtAnnotationsScope, data: D) {
        element.annotations.visitNullable(this, data)
    }

    // Arguments/parameter
    override fun visitParameter(element: KtParameterElement, data: D) {
        element.type.visitNullable(this,data)
        element.value.visitNullable(this,data)
    }
    //scopes
    override fun visitArgumentsScope(element: KtArgumentsScope, data: D) {
        element.arguments.visitNullable(this,data)
    }
    override fun visitParametersScope(element: KtParametersScope, data: D) {
        element.parameters.visit(this,data)
    }

    // Declarations
    override fun visitDeclaration(element: KtDeclarationElement, data: D) {}
    override fun visitClass(element: KtClassElement, data: D) {
        element.annotations.visit(this,data)
        element.typeParameters.visit(this, data)
        element.declarations.visit(this, data)

    }
    override fun visitConstructor(element: KtConstructorElement, data: D) {
        element.typeParameters.visit(this, data)
        element.body.visitNullable(this, data)
    }
    override fun visitField(element: KtFieldElement, data: D) {
        element.annotations.visit(this,data)
        element.type.visitNullable(this,data)
        element.value.visitNullable(this,data)
    }
    override fun visitFunction(element: KtFunctionElement, data: D) {
        element.annotations.visit(this,data)
        element.typeParameters.visit(this, data)
        element.body.visitNullable(this, data)
        element.parameters.visit(this, data)
        element.type.visitNullable(this,data)
    }
    override fun visitProperty(element: KtPropertyElement, data: D) {
        element.annotations.visit(this,data)
        element.type.visitNullable(this,data)
        element.setter.visitNullable(this,data)
        element.getter.visitNullable(this,data)
        element.field.visitNullable(this,data)
    }
    //scopes
    override fun visitDeclarationsScope(element: KtDeclarationsScope, data: D) {
        element.declarations.visit(this,data)
    }

    // IO / package
    override fun visitFile(element: KtFileElement, data: D) {
        element.declarations.visit(this, data)
    }
    override fun visitPackage(element: KtPackageElement, data: D) {
        element.packageElements.visit(this, data)
    }
    override fun visitPackageScopeElement(element: KtPackageScopeElement, data: D) {}
    //scopes
    override fun visitPackageScope(element: KtPackageScope, data: D) {
        element.packageElements.visit(this, data)
    }

    // Statements
    override fun visitStatement(element: KtStatementElement, data: D) {}
    override fun visitReturnStatement(element: KtReturnStatementElement, data: D) {
        element.expression.visitNullable(this,data)
    }

    // Expressions
    override fun visitExpression(element: KtExpressionElement, data: D) {}
    override fun visitBlock(element: KtBlockElement, data: D) {
        element.type.visitNullable(this,data)
        element.statements.visit(this,data)
    }
    override fun visitConst(element: KtConstElement<*>, data: D) {}
    override fun visitConstructorCall(element: KtConstructorCallElement, data: D) {
        element.type.visitNullable(this,data)
        element.arguments.visitNullable(this,data)

    }
    override fun visitDelegatingConstructorCall(element: KtDelegatingConstructorCallElement, data: D) {
        element.type.visitNullable(this,data)
        element.arguments.visitNullable(this,data)
    }
    override fun visitGetField(element: KtGetFieldElement, data: D) {
        element.receiver.visitNullable(this,data)
        element.type.visitNullable(this,data)

    }
    override fun visitGetValue(element: KtGetValueElement, data: D) {
        element.type.visitNullable(this, data)
        element.parameter.visitNullable(this,data)
    }
    override fun visitIf(element: KtIfElement, data: D) {
        element.condition.visitNullable(this,data)
        element.type.visitNullable(this, data)
        element.ifBody.visitNullable(this,data)
        element.elseBody.visitNullable(this,data)
    }
    override fun visitSetField(element: KtSetFieldElement, data: D) {
        element.receiver.visitNullable(this,data)
        element.value.visitNullable(this,data)
        element.type.visitNullable(this,data)
    }

    // Types
    override fun visitType(element: KtTypeElement, data: D) {}
    override fun visitClassType(element: KtClassTypeElement, data: D) {
        element.typeArguments.visit(this, data)
    }
    override fun visitParameterType(element: KtParameterTypeElement, data: D) {
    }
    override fun visitTypeParameter(element: KtTypeParameterElement, data: D) {}
    //scopes
    override fun visitTypeArgumentScope(element: KtTypeArgumentScope, data: D) {
        element.typeArguments.visit(this, data)
    }
    override fun visitTypeParameterScope(element: KtTypeParameterScope, data: D) {
        element.typeParameters.visit(this, data)
    }
}