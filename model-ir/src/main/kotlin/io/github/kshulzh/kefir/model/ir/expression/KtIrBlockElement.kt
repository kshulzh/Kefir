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
import io.github.kshulzh.kefir.model.api.expression.KtBlockElement
import io.github.kshulzh.kefir.model.api.statement.KtStatementElement
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.createLazyIrList
import io.github.kshulzh.kefir.model.ir.statement.wrapIrStatement
import io.github.kshulzh.kefir.transform.IrWrapper
import io.github.kshulzh.kefir.transform.context.KtTransformContext
import org.jetbrains.kotlin.ir.expressions.IrBlock

class KtIrBlockElement(
    override val irElement: IrBlock,
    var transformContext: KtTransformContext,
    override var parent: KtElement? = null,
) : KtBlockElement, IrWrapper<IrBlock> {
    override var type: KtTypeElement?
        get() = TODO("Not yet implemented")
        set(value) {}
    override var statements: MutableList<KtStatementElement> by
    createLazyIrList(
        transformContext,
        { irElement.statements.map { wrapIrStatement(it, transformContext, this)!! } },
        onAdd = { e, i ->
            {
                val element = irTransform(e)!!
                if (i > -1) {
                    irElement.statements.add(i, element)
                } else {
                    irElement.statements.add(element)
                }
            }
        },
        onDelete = { e, i ->
            {
                if (i > -1) {
                    irElement.statements.removeAt(i)
                } else {
                    irElement.statements.remove(irTransform(e))
                }
            }
        },
    )
}