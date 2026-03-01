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

package io.github.kshulzh.kefir.model.annotation

import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationElement
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationsScope
import io.github.kshulzh.kefir.model.api.arg.KtArgumentElement
import io.github.kshulzh.kefir.model.api.type.KtTypeElement

/**
 * Implementation of the `KtAnnotationElement` interface.
 *
 * The `KtAnnotationElementImpl` class represents an annotation in the Kotlin model structure. It
 * encapsulates details about the annotation's type, its associated scope of annotations, and the
 * arguments provided to it. This implementation provides a concrete representation of an annotation
 * element in the intermediate or frontend representations of Kotlin code.
 *
 * @property type The type of the annotation, represented as a `KtTypeElement`.
 * This defines the annotation type in the model structure.
 *
 * @property annotationScope The scope of annotations associated with this annotation element.
 * This optional property provides access to nested annotations relating to this instance.
 *
 * @property arguments The list of arguments provided to this annotation. Each argument is
 * described as a `KtArgumentElement` and represents the arguments used within the annotation.
 */
class KtAnnotationElementImpl(
    override var type: KtTypeElement,
    override var annotationScope: KtAnnotationsScope?,
    override var arguments: MutableList<KtArgumentElement>
) : KtAnnotationElement