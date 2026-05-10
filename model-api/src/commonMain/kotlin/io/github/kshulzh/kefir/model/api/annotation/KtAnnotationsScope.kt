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
import io.github.kshulzh.kefir.model.api.utils.KtVisitor

/**
 * Represents a scope containing annotations in the Kotlin model structure.
 *
 * This interface defines a set of annotation elements that apply to another element in the model
 * (e.g., classes, functions, or properties). It is implemented by elements that can contain annotations.
 */
interface KtAnnotationsScope : KtElement {
    /**
     * Represents a mutable list of `KtAnnotationElement` instances that belong to a specific Kotlin model.
     *
     * Each `KtAnnotationElement` in this list provides information about a defined annotation,
     * including its type and potentially nested annotation scopes. This property enables
     * handling and processing annotations systematically within the model.
     */
    val annotations: MutableList<KtAnnotationElement>

    override fun <R, D> accept(visitor: KtVisitor<R, D>, data: D): R = visitor.visitAnnotationsScope(this, data)
}