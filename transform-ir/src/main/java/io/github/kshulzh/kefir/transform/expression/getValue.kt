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

import io.github.kshulzh.kefir.model.api.expression.KtGetValueElement
import io.github.kshulzh.kefir.transform.context.KtIrLocalTransformContext
import io.github.kshulzh.kefir.transform.utils.getIr
import org.jetbrains.kotlin.ir.backend.js.ir.JsIrBuilder.buildGetValue
import org.jetbrains.kotlin.ir.declarations.IrValueDeclaration
import org.jetbrains.kotlin.ir.expressions.IrGetValue

/**
 * Transforms a `KtGetValueElement` from the Kotlin AST into an IR (`Intermediate Representation`)
 * `IrGetValue` object.
 *
 * @param input The `KtGetValueElement` to be transformed. This element represents a value access
 *              in the Kotlin AST and may include parameters that need to be resolved into IR symbols.
 * @return The transformed `IrGetValue` representing the value access in IR form, or `null` if the
 *         transformation cannot be completed due to missing or invalid parameters.
 */
fun KtIrLocalTransformContext.transformIrGetValue(input: KtGetValueElement): IrGetValue? {
    return buildGetValue(input.parameter?.getIr<IrValueDeclaration>()?.symbol!!)
}