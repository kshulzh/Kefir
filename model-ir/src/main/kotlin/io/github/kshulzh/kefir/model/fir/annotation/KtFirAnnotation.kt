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

package io.github.kshulzh.kefir.model.fir.annotation

import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationElement
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationsScope
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.transform.FirWrapper
import io.github.kshulzh.kefir.transform.context.KtFirTransformContext
import org.jetbrains.kotlin.fir.expressions.FirAnnotation


/**
 * Represents a Kotlin annotation element in the FIR (Frontend Intermediate Representation) pipeline.
 *
 * This class wraps a [FirAnnotation] and provides access to its associated transformation context,
 * annotation scope, and various annotation-specific properties such as arguments and type information.
 */
class KtFirAnnotation(
    override val firElement: FirAnnotation,
    var transformFirContext: KtFirTransformContext,
    override var annotationScope: KtAnnotationsScope? = null,
) : KtAnnotationElement, FirWrapper<FirAnnotation> {
    /**
     * Represents a mutable list of argument elements associated with an annotation.
     *
     * Each element in the list corresponds to an instance of [KtExpressionElement], which can
     * represent various forms of expressions in the Kotlin abstract syntax tree (AST). The list
     * can include null entries when certain argument values are unspecified or unavailable.
     *
     * The `arguments` property facilitates the handling, transformation, and analysis of annotation
     * arguments within the Kotlin FIR (Frontend Intermediate Representation) and model structures.
     */
    override var arguments: MutableList<KtExpressionElement?>
        get() = TODO("Not yet implemented")
        set(value) {

        }
    /**
     * Represents a mapping of argument names to their corresponding expression elements.
     *
     * The `argumentMap` property is a mutable map where each key is a `String` representing
     * an argument name, and the value is a nullable `KtExpressionElement` that defines
     * the associated expression for the argument. It is useful for handling named arguments
     * within annotations or other constructs in the Kotlin model.
     *
     * This property is overridden to provide specialized behavior or structure
     * for argument mapping within the context of the `KtFirAnnotation` class.
     */
    override var argumentMap: MutableMap<String, KtExpressionElement?>
        get() = TODO("Not yet implemented")
        set(value) {}
    /**
     * Represents the type associated with the annotation element in the Kotlin model structure.
     *
     * This property is defined as an override of the `type` property in the [KtAnnotationElement]
     * interface. It is used to specify and access the type-related information of the annotation.
     *
     * The [KtTypeElement] serves as the base abstraction for handling type representations
     * within the Kotlin type system, including primitive types, class types, and user-defined types.
     * This property enables performing transformations, analyses, or validations involving the annotation's type
     * in intermediate and frontend models.
     */
    override var type: KtTypeElement
        get() = TODO("Not yet implemented")
        set(value) {}
}