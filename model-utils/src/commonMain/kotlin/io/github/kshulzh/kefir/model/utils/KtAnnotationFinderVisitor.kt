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

package io.github.kshulzh.kefir.model.utils

import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationsScope
import io.github.kshulzh.kefir.model.api.arg.KtParameterElement
import io.github.kshulzh.kefir.model.api.declaration.KtClassElement
import io.github.kshulzh.kefir.model.api.declaration.KtConstructorElement
import io.github.kshulzh.kefir.model.api.declaration.KtFunctionElement
import io.github.kshulzh.kefir.model.api.declaration.KtPropertyElement
import io.github.kshulzh.kefir.model.api.io.KtFileElement
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.api.type.KtTypeParameterElement
import io.github.kshulzh.kefir.model.api.utils.KtChildVisitor
import java.util.LinkedList

class KtAnnotationFinderVisitor(
    val typeElement: KtTypeElement,
    targets: List<AnnotationTarget> = emptyList(),
) : KtChildVisitor<Unit> {
    val result = LinkedList<KtAnnotationsScope>()
    private val allowedClass = targets.contains(AnnotationTarget.CLASS)
    private val allowedAnnotationClass = targets.contains(AnnotationTarget.ANNOTATION_CLASS)
    private val allowedTypeParameter = targets.contains(AnnotationTarget.TYPE_PARAMETER)
    private val allowedProperty = targets.contains(AnnotationTarget.PROPERTY)
    private val allowedField = targets.contains(AnnotationTarget.FIELD)
    private val allowedLocalVariable = targets.contains(AnnotationTarget.LOCAL_VARIABLE)
    private val allowedValueParameter = targets.contains(AnnotationTarget.VALUE_PARAMETER)
    private val allowedConstructor = targets.contains(AnnotationTarget.CONSTRUCTOR)
    private val allowedFunction = targets.contains(AnnotationTarget.FUNCTION)
    private val allowedPropertyGetter = targets.contains(AnnotationTarget.PROPERTY_GETTER)
    private val allowedPropertySetter = targets.contains(AnnotationTarget.PROPERTY_SETTER)
    private val allowedType = targets.contains(AnnotationTarget.TYPE)
    private val allowedExpression = targets.contains(AnnotationTarget.EXPRESSION)
    private val allowedFile = targets.contains(AnnotationTarget.FILE)
    private val allowedTypeAlias = targets.contains(AnnotationTarget.TYPEALIAS)

    fun add(annotated: KtAnnotationsScope) {
        if (annotated.isAnnotated(typeElement)) {
            result.add(annotated)
        }
    }

    override fun visitFile(element: KtFileElement, data: Unit) {
        if (allowedFile) {
            add(element)
        }
        //todo optimize
        super.visitFile(element, data)
    }

    override fun visitClass(element: KtClassElement, data: Unit) {
        if (allowedClass) {
            add(element)
        }
        //todo optimize
        super.visitClass(element, data)
    }

    override fun visitFunction(element: KtFunctionElement, data: Unit) {
        if (allowedFunction) {
            add(element)
        }
        //todo optimize
        super.visitFunction(element, data)
    }

    override fun visitProperty(element: KtPropertyElement, data: Unit) {
        if (allowedProperty) {
            add(element)
        }
        if (allowedField) {
            element.field?.also { add(it) }
        }
        if (allowedPropertyGetter) {
            element.getter?.also { add(it) }
        }
        if (allowedPropertySetter) {
            element.setter?.also { add(it) }
        }
        super.visitProperty(element, data)
    }

    override fun visitParameter(element: KtParameterElement, data: Unit) {
        if (allowedValueParameter) {
            add(element)
        }

        super.visitParameter(element, data)
    }

    override fun visitConstructor(element: KtConstructorElement, data: Unit) {
        if (allowedConstructor) {
            add(element)
        }

        //todo optimize
        super.visitConstructor(element, data)
    }

    override fun visitTypeParameter(element: KtTypeParameterElement, data: Unit) {
        if (allowedTypeParameter) {
            add(element)
        }
        //todo optimize
        super.visitTypeParameter(element, data)
    }
}