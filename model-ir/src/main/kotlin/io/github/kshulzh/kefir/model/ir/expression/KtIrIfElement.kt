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
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.expression.KtIfElement
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.transform.IrWrapper
import io.github.kshulzh.kefir.transform.context.KtTransformContext
import org.jetbrains.kotlin.ir.expressions.IrBranch
import org.jetbrains.kotlin.ir.expressions.IrElseBranch
import org.jetbrains.kotlin.ir.expressions.IrWhen

class KtIrIfElement(
    override val irElement: IrWhen,
    var transformContext: KtTransformContext,
    override var parent: KtElement? = null,
) : KtIfElement, IrWrapper<IrWhen> {
    var ifBranch: IrBranch = irElement.branches.first()
    var elseBranch: IrElseBranch? = null

    init {
        if (irElement.branches.size < 3) {
            elseBranch = irElement.branches.last() as IrElseBranch
        } else {
            throw RuntimeException("If expression must have 1 or 2 branches")
        }
    }

    override var type: KtTypeElement?
        get() = TODO("Not yet implemented")
        set(value) {
        }
    override var condition: KtExpressionElement
        get() = TODO("Not yet implemented")
        set(value) {}
    override var ifBody: KtExpressionElement?
        get() = TODO("Not yet implemented")
        set(value) {}
    override var elseBody: KtExpressionElement?
        get() = TODO("Not yet implemented")
        set(value) {}
}