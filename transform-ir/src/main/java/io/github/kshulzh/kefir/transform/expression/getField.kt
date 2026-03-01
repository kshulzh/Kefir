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

import io.github.kshulzh.kefir.model.api.expression.KtGetFieldElement
import io.github.kshulzh.kefir.transform.context.KtIrLocalTransformContext
import io.github.kshulzh.kefir.transform.utils.getIr
import io.github.kshulzh.kefir.transform.utils.type.resolveType
import org.jetbrains.kotlin.ir.backend.js.ir.JsIrBuilder.buildGetField
import org.jetbrains.kotlin.ir.declarations.IrField
import org.jetbrains.kotlin.ir.expressions.IrGetField

/**
 * Transforms a [KtGetFieldElement] into an IR (Intermediate Representation) [IrGetField] element.
 *
 * This method converts the provided Kotlin field access representation into its corresponding
 * IR field access structure. It processes the receiver expression and the field's type to
 * construct the desired [IrGetField] element.
 *
 * @param input The [KtGetFieldElement] representing the field access expression in the Kotlin AST.
 *              It contains details about the accessed field, the optional receiver context, and
 *              associated type information.
 * @return An [IrGetField] instance representing the IR equivalent of the input field access,
 *         or `null` if the transformation cannot be performed.
 */
fun KtIrLocalTransformContext.transformIrGetField(input: KtGetFieldElement): IrGetField? {
    val expression = irTransform(input.receiver!!)
    val type = irTransform(input.resolveType()!!)!!

    return buildGetField(
        symbol = input.field.getIr<IrField>()?.symbol!!,
        receiver = expression,
        type = type
    )
}