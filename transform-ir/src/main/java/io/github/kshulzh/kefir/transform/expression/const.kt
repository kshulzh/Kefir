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

import io.github.kshulzh.kefir.model.api.expression.KtConstElement
import io.github.kshulzh.kefir.transform.context.KtFirLocalTransformContext
import io.github.kshulzh.kefir.transform.context.KtIrLocalTransformContext
import io.github.kshulzh.kefir.transform.utils.linkFir
import io.github.kshulzh.kefir.transform.utils.linkIr
import org.jetbrains.kotlin.fir.expressions.FirLiteralExpression
import org.jetbrains.kotlin.fir.expressions.builder.buildLiteralExpression
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.impl.IrConstImpl
import org.jetbrains.kotlin.types.ConstantValueKind

/**
 * Transforms a constant Kotlin const element (`KtConstElement`) into its corresponding IR (`Intermediate Representation`) constant (`IrConst`).
 *
 * @param input The `KtConstElement` to be transformed. This represents a constant expression in the Kotlin AST,
 *              such as a string, boolean, or other constant value.
 * @return The transformed `IrConst` reflecting the input in IR form, or `null` if the transformation cannot be completed.
 */
fun KtIrLocalTransformContext.transformIrConst(input: KtConstElement<*>): IrConst? {
    return when (val value = input.value) {
        is String -> IrConstImpl.string(
            UNDEFINED_OFFSET,
            UNDEFINED_OFFSET,
            pluginContext.irBuiltIns.stringType,
            value
        )

        is Boolean -> IrConstImpl.boolean(
            UNDEFINED_OFFSET,
            UNDEFINED_OFFSET,
            pluginContext.irBuiltIns.booleanType,
            value
        )

        else -> IrConstImpl.constNull(
            UNDEFINED_OFFSET,
            UNDEFINED_OFFSET,
            irTransform(input.type!!)!!
        )
    }.linkIr(input)
}

/**
 * Transforms a constant element (`KtConstElement`) into a corresponding FIR literal expression (`FirLiteralExpression`).
 *
 * This method utilizes the value of the constant element to determine its type and build
 * the appropriate FIR representation. Supported constant types include primitive types (e.g., Int, String, Boolean)
 * as well as nullable types.
 *
 * @param input The `KtConstElement` to be transformed, representing a constant value in the Kotlin AST.
 * @return The transformed `FirLiteralExpression` representing the constant, or `null` if the transformation fails.
 */
fun KtFirLocalTransformContext.transformFirConst(input: KtConstElement<*>): FirLiteralExpression? {
    return buildLiteralExpression(
        null,
        if (input.value != null) {
            when (input.value) {
                is String -> ConstantValueKind.String
                is Int -> ConstantValueKind.Int
                is Double -> ConstantValueKind.Double
                is Float -> ConstantValueKind.Float
                is Long -> ConstantValueKind.Long
                is Short -> ConstantValueKind.Short
                is Byte -> ConstantValueKind.Byte
                is Char -> ConstantValueKind.Char
                is Boolean -> ConstantValueKind.Boolean
                is UInt -> ConstantValueKind.UnsignedInt
                is ULong -> ConstantValueKind.UnsignedLong
                is UShort -> ConstantValueKind.UnsignedShort
                is UByte -> ConstantValueKind.UnsignedByte
                else -> ConstantValueKind.Error
            }
        } else {
            ConstantValueKind.Null
        },
        input.value,
        setType = true,
    ).linkFir(input)
}