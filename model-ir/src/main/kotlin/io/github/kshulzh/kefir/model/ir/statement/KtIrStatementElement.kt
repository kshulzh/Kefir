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

package io.github.kshulzh.kefir.model.ir.statement


import io.github.kshulzh.kefir.model.api.KtElement
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationElement
import io.github.kshulzh.kefir.model.api.statement.KtStatementElement
import io.github.kshulzh.kefir.transform.IrWrapper
import io.github.kshulzh.kefir.transform.context.KtTransformContext
import org.jetbrains.kotlin.ir.IrStatement

/**
 * Represents a concrete implementation of a statement element within the Kotlin Intermediate
 * Representation (IR) transformation model.
 *
 * The `KtIrStatementElement` connects a Kotlin statement-level IR element with the transformation
 * context and the hierarchical structure of elements within the IR model.
 *
 * This class extends [KtStatementElement], providing additional capabilities for handling
 * annotations and wrapping IR-specific elements as defined by [IrWrapper].
 *
 * @property irElement The associated IR statement element encapsulated by this class. This property
 * provides access to the underlying IR structure for analysis or transformation purposes.
 * @property transformContext The [KtTransformContext] in which this element resides. This context
 * provides utilities and scope for performing transformations on the IR graph.
 * @property parent The parent [KtElement] in the abstract syntax tree hierarchy. This property
 * allows navigation to higher levels of the tree, aiding in traversal and context-aware modifications.
 * @property annotations A mutable list of annotations applied to this element. Annotations provide
 * metadata or additional semantics to the statement represented by this class.
 */
class KtIrStatementElement(
    override val irElement: IrStatement,
    var transformContext: KtTransformContext,
    override var parent: KtElement? = null,
) : KtStatementElement, IrWrapper<IrStatement> {
    /**
     * Represents a mutable list of annotation elements associated with this statement element.
     *
     * The `annotations` property allows access to and manipulation of the annotations
     * applied to the current Kotlin IR (Intermediate Representation) statement element. Each annotation
     * in the list corresponds to a `KtAnnotationElement`, which provides details such as the annotation
     * type and its associated scope.
     */
    override val annotations: MutableList<KtAnnotationElement> = mutableListOf()
}