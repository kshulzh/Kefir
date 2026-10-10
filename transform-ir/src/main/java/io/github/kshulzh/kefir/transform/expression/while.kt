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

package io.github.kshulzh.kefir.transform.expression

import io.github.kshulzh.kefir.model.api.expression.KtBlockElement
import io.github.kshulzh.kefir.model.api.expression.KtDoWhileElement
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.expression.KtLoopElement
import io.github.kshulzh.kefir.model.api.expression.KtWhileElement
import io.github.kshulzh.kefir.transform.context.KtFirLocalTransformContext
import io.github.kshulzh.kefir.transform.context.KtIrLocalTransformContext
import io.github.kshulzh.kefir.transform.utils.linkFir
import io.github.kshulzh.kefir.transform.utils.linkIr
import org.jetbrains.kotlin.fir.FirLabel
import org.jetbrains.kotlin.fir.builder.buildLabel
import org.jetbrains.kotlin.fir.expressions.FirBlock
import org.jetbrains.kotlin.fir.expressions.FirDoWhileLoop
import org.jetbrains.kotlin.fir.expressions.FirWhileLoop
import org.jetbrains.kotlin.fir.expressions.builder.buildBlock
import org.jetbrains.kotlin.fir.expressions.builder.buildDoWhileLoop
import org.jetbrains.kotlin.fir.expressions.builder.buildWhileLoop
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.expressions.IrDoWhileLoop
import org.jetbrains.kotlin.ir.expressions.IrLoop
import org.jetbrains.kotlin.ir.expressions.IrWhileLoop
import org.jetbrains.kotlin.ir.expressions.impl.IrDoWhileLoopImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrWhileLoopImpl
import org.jetbrains.kotlin.ir.types.IrType

private fun KtIrLocalTransformContext.loopType(input: KtLoopElement): IrType =
    input.type?.let { irTransform(it) } ?: pluginContext.irBuiltIns.unitType

private fun <L : IrLoop> KtIrLocalTransformContext.fill(loop: L, input: KtLoopElement): L = loop.apply {
    label = input.label
    condition = irTransform(input.condition)!!
    body = input.body?.let { irTransform(it) }
}

/**
 * Transforms a [KtWhileElement] into an [IrWhileLoop].
 */
fun KtIrLocalTransformContext.transformIrWhile(input: KtWhileElement): IrWhileLoop? =
    fill(IrWhileLoopImpl(UNDEFINED_OFFSET, UNDEFINED_OFFSET, loopType(input), null), input).linkIr(input)

/**
 * Transforms a [KtDoWhileElement] into an [IrDoWhileLoop].
 */
fun KtIrLocalTransformContext.transformIrDoWhile(input: KtDoWhileElement): IrDoWhileLoop? =
    fill(IrDoWhileLoopImpl(UNDEFINED_OFFSET, UNDEFINED_OFFSET, loopType(input), null), input).linkIr(input)

private fun KtFirLocalTransformContext.loopBody(body: KtExpressionElement?): FirBlock =
    when (body) {
        null -> buildBlock { }
        is KtBlockElement -> firTransform(body)!!
        else -> firTransform(body)!!.let { exp -> buildBlock { statements.add(exp) } }
    }

private fun loopLabel(input: KtLoopElement): FirLabel? = input.label?.let { name -> buildLabel { this.name = name } }

/**
 * Transforms a [KtWhileElement] into a [FirWhileLoop]. FIR loops are statements, not expressions.
 */
fun KtFirLocalTransformContext.transformFirWhile(input: KtWhileElement): FirWhileLoop? {
    val condition = firTransform(input.condition)!!
    val block = loopBody(input.body)
    return buildWhileLoop {
        this.condition = condition
        this.block = block
        label = loopLabel(input)
    }.linkFir(input)
}

/**
 * Transforms a [KtDoWhileElement] into a [FirDoWhileLoop]. FIR loops are statements, not expressions.
 */
fun KtFirLocalTransformContext.transformFirDoWhile(input: KtDoWhileElement): FirDoWhileLoop? {
    val condition = firTransform(input.condition)!!
    val block = loopBody(input.body)
    return buildDoWhileLoop {
        this.condition = condition
        this.block = block
        label = loopLabel(input)
    }.linkFir(input)
}
