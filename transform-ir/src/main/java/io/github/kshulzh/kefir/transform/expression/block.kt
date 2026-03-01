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

package io.github.kshulzh.kefir.transform.expression

import io.github.kshulzh.kefir.model.api.expression.KtBlockElement
import io.github.kshulzh.kefir.transform.context.KtFirLocalTransformContext
import io.github.kshulzh.kefir.transform.context.KtIrLocalTransformContext
import io.github.kshulzh.kefir.transform.utils.linkFir
import io.github.kshulzh.kefir.transform.utils.linkIr
import org.jetbrains.kotlin.fir.expressions.FirBlock
import org.jetbrains.kotlin.fir.expressions.builder.buildBlock
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.expressions.IrBlock
import org.jetbrains.kotlin.ir.expressions.impl.IrBlockImpl

/**
 * Transforms a Kotlin block element (`KtBlockElement`) into its corresponding IR (`Intermediate Representation`)
 * block (`IrBlock`).
 *
 * @param input The `KtBlockElement` to be transformed, which represents a block in the Kotlin AST.
 *              This block contains a sequence of statements and expressions that are to be converted
 *              into IR representation.
 * @return The transformed `IrBlock` reflecting the input block in IR form, or `null` if the transformation fails.
 */
fun KtIrLocalTransformContext.transformIrBlock(input: KtBlockElement): IrBlock? {
    val type = irTransform(input.type!!)!!
    val statements = input.statements.map { it -> irTransform(it)!! }
    return IrBlockImpl(
        UNDEFINED_OFFSET,
        UNDEFINED_OFFSET,
        type = type,
        null,
        statements
    ).linkIr(input)
}

/**
 * Transforms a [KtBlockElement] into a corresponding [FirBlock] within the given Fir transformation context.
 * This method processes the type and statements of the input block element, converts them into their
 * FIR representations, and assembles a FIR block with the resulting data.
 *
 * @param input The [KtBlockElement] to be transformed into a [FirBlock].
 * @return The transformed [FirBlock] if the input is successfully processed, or null if the transformation fails.
 */
fun KtFirLocalTransformContext.transformFirBlock(input: KtBlockElement): FirBlock? {
    val type = firTransform(input.type!!)

    val statements = input.statements.map { it -> firTransform(it)!! }
    return buildBlock {
        coneTypeOrNull = type
        this.statements.addAll(statements)
    }.linkFir(input)
}