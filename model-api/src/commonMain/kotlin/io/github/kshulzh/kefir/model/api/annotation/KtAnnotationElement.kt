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

package io.github.kshulzh.kefir.model.api.annotation

import io.github.kshulzh.kefir.model.api.KtElement
import io.github.kshulzh.kefir.model.api.arg.KtArgumentsScope
import io.github.kshulzh.kefir.model.api.type.KtTypeElement

/**
 * Represents an annotation element in the Kotlin model structure.
 * This interface combines the functionalities of `KtElement` and `KtArgumentsScope`.
 *
 * An instance of `KtAnnotationElement` provides access to the associated annotation type
 * and its nested annotation scope if applicable.
 */
interface KtAnnotationElement : KtElement, KtArgumentsScope {
    /**
     * Represents the type element associated with the annotation.
     * This property is used to define the type of the annotation.
     */
    var type: KtTypeElement

    /**
     * Represents the scope of annotations associated with the annotation element.
     *
     * The `annotationScope` property refers to an optional `KtAnnotationsScope` instance that defines
     * the list of annotations directly related to the declaring `KtAnnotationElement`.
     */
    var annotationScope: KtAnnotationsScope?
}