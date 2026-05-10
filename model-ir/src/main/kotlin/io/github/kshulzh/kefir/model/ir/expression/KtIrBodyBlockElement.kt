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

package io.github.kshulzh.kefir.model.ir.expression

import io.github.kshulzh.kefir.model.api.KtElement
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationElement
import io.github.kshulzh.kefir.model.api.expression.KtBlockElement
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.statement.KtStatementElement
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.ir.declaration.wrapIrBody
import io.github.kshulzh.kefir.model.ir.statement.wrapIrStatement
import io.github.kshulzh.kefir.model.utils.createLazyIr2
import io.github.kshulzh.kefir.model.utils.createLazyIrList2
import io.github.kshulzh.kefir.transform.IrWrapper
import io.github.kshulzh.kefir.transform.context.KtTransformContext
import io.github.kshulzh.kefir.transform.model.replaceBody
import org.jetbrains.kotlin.ir.expressions.IrBlockBody

/**
 * Represents a Kotlin IR (Intermediate Representation) body block element.
 *
 * This class implements the [KtBlockElement] and [IrWrapper] interfaces to encapsulate
 * an [IrBlockBody] within a Kotlin abstract syntax tree (AST) hierarchy. It serves as a bridge
 * between the IR structure and the Kotlin model, providing a way to transform, access,
 * and manipulate the statements and annotations within a block body in the Kotlin IR pipeline.
 *
 * @constructor Creates an instance of [KtIrBodyBlockElement].
 * @param irElement The [IrBlockBody] IR element associated with this block element.
 * @param transformContext The [KtTransformContext] used for transforming and wrapping IR statements
 *                         and interacting with the IR during transformation processes.
 * @param parent An optional parent [KtElement] indicating the hierarchical context of this element.
 *               Defaults to `null` if no parent is specified.
 */
class KtIrBodyBlockElement(
    override val irElement: IrBlockBody,
    var transformContext: KtTransformContext,
    override var parent: KtElement? = null,
) : KtBlockElement, IrWrapper<IrBlockBody> {
    /**
     * Represents the type information associated with this element.
     *
     * This property allows for interaction with the type hierarchy and provides
     * metadata about the type of this element in the Kotlin Intermediate Representation (IR).
     * It is primarily used for type checking, type transformation, or resolving type-related
     * behaviors during processing or compilation stages.
     *
     * It returns an instance of [KtTypeElement] if a valid type is associated, or `null`
     * if no type has been explicitly assigned.
     */
    override var type: KtTypeElement? = null
    /**
     * Represents a collection of statement elements within the Kotlin abstract syntax tree (AST)
     * and their corresponding intermediate representation (IR).
     *
     * This property provides a mutable list of `KtStatementElement` instances, which allows
     * dynamic additions, deletions, and management of statements for the current element.
     * The list is lazily initialized and its contents are automatically synchronized with the
     * IR transformation context.
     *
     * The delegate leverages the transformation framework to ensure that any additions or deletions
     * to this list propagate to the associated IR block body (`IrBlockBody`) while maintaining
     * a consistent parent-child relationship structure in the AST. The delegation logic with
     * `createLazyIrList2` ensures that statements adhere to proper transformation rules and context constraints.
     *
     * Key characteristics:
     * - Changes to the list (additions/removals) are reflected in the associated `IrBlockBody`.
     * - Statement transformations are handled using `wrapIrStatement` to wrap IR statements into valid `KtStatementElement` objects.
     * - Ensures integration with the parent hierarchy of `KtElement`.
     *
     * This property is declared as `override` to fulfill the contract of a pre-defined structure
     * for managing statements in the higher-level API or model.
     */
    override var statements: MutableList<KtStatementElement> by createLazyIrList2(
        transformContext = transformContext,
        initializer = irElement::statements,
        transformer = { wrapIrStatement(it, transformContext, this) },
        property = KtStatementElement::parent,
        onAdd = { e, i ->
            val element = irTransform(e)!!
            if (i > -1) {
                irElement.statements.add(i, element)
            } else {
                irElement.statements.add(element)
            }
            null
        },
        onDelete = { e, i ->
            if (i > -1) {
                irElement.statements.removeAt(i)
            } else {
                irElement.statements.remove(irTransform(e))
            }
        }
    )

    /**
     * Stores the list of annotations applied to this element.
     *
     * This mutable list contains instances of `KtAnnotationElement` and allows access to
     * the annotations defined in the Kotlin model structure for the corresponding element.
     *
     * Each annotation element provides information about the type and associated annotation scope.
     */
    override val annotations: MutableList<KtAnnotationElement> = mutableListOf()
}