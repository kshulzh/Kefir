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

package io.github.kshulzh.kefir.model.ir.arg

import io.github.kshulzh.kefir.model.api.KtName
import io.github.kshulzh.kefir.model.api.arg.KtArgumentElement
import io.github.kshulzh.kefir.model.api.arg.KtArgumentsScope
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.createLazy
import io.github.kshulzh.kefir.model.ir.expression.wrapExpression
import io.github.kshulzh.kefir.transform.IrWrapper
import io.github.kshulzh.kefir.transform.context.KtTransformContext
import org.jetbrains.kotlin.ir.expressions.IrExpression

class KtIrArgumentElement(
    override var name: KtName?,
    override val irElement: IrExpression,
    override var position: Int = -1,
    var onSet: (KtExpressionElement?) -> Unit = { },
    var transformContext: KtTransformContext,
    override var argumentsScope: KtArgumentsScope? = null,
) : KtArgumentElement, IrWrapper<IrExpression> {
    override var value: KtExpressionElement? by createLazy(
        initializer = { wrapExpression(irElement, transformContext, this) },
        onSet = {
            it?.parent = this
            onSet(it)
        }
    )
}