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

import io.github.kshulzh.kefir.model.api.expression.KtSetFieldElement
import io.github.kshulzh.kefir.transform.context.KtIrLocalTransformContext
import io.github.kshulzh.kefir.transform.utils.getIr
import io.github.kshulzh.kefir.transform.utils.type.resolveType
import org.jetbrains.kotlin.ir.backend.js.ir.JsIrBuilder.buildSetField
import org.jetbrains.kotlin.ir.declarations.IrField
import org.jetbrains.kotlin.ir.expressions.IrSetField

/**
 * Transforms a `KtSetFieldElement`, representing a field assignment in the Kotlin abstract syntax tree (AST),
 * into its corresponding IR (`Intermediate Representation`) `IrSetField`.
 * This conversion involves transforming the type, value, and receiver components of the input element
 * into their respective IR representations and constructing an IR field assignment.
 *
 * @param input The `KtSetFieldElement` to transform. Represents a field assignment,
 *              including elements such as the receiver (context for the field), the value to assign,
 *              and the field type.
 * @return The transformed `IrSetField`, representing the IR equivalent of the field assignment,
 *         or `null` if the transformation cannot be completed successfully.
 */
fun KtIrLocalTransformContext.transformIrSetField(input: KtSetFieldElement): IrSetField? {
    val type = irTransform(input.resolveType()!!)!!
    val value1 = irTransform(input.value!!)!!
    val receiver = irTransform(input.receiver!!)!!
    return buildSetField(
        symbol = input.field.getIr<IrField>()?.symbol!!,
        receiver = receiver,
        value = value1,
        type = type
    )
}