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

import io.github.kshulzh.kefir.model.api.expression.KtConstructorCallElement
import io.github.kshulzh.kefir.transform.context.KtIrLocalTransformContext
import io.github.kshulzh.kefir.transform.utils.getIr
import org.jetbrains.kotlin.ir.backend.js.ir.JsIrBuilder
import org.jetbrains.kotlin.ir.declarations.IrConstructor
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrDelegatingConstructorCall

/**
 * Transforms a [KtConstructorCallElement] into an [IrConstructorCall] representation.
 *
 * This method converts a Kotlin constructor call element (`KtConstructorCallElement`) from the abstract
 * syntax tree (AST) into its corresponding Intermediate Representation (IR) form as an `IrConstructorCall`.
 * The transformation includes resolving the target constructor and mapping arguments from the source
 * element into the resulting IR entity.
 *
 * @param input The [KtConstructorCallElement] instance representing the constructor call in the AST.
 *              This contains the information about the called constructor and its arguments.
 * @return The transformed [IrConstructorCall] object if successful, or null if the transformation fails or cannot be performed.
 */
fun KtIrLocalTransformContext.transformIrConstructorCall(input: KtConstructorCallElement): IrConstructorCall? {
    val target = input.constructor.getIr<IrConstructor>()!!.symbol
    return JsIrBuilder.buildConstructorCall(
        target
    ).apply {
        fork {
            input.arguments.forEachIndexed { index, ktArgument ->
                arguments.add(null)
                if (ktArgument != null) {
                    arguments[index] = irTransform(ktArgument)!!
                }
            }
        }
    }
}

/**
 * Transforms a given [KtConstructorCallElement] into an IR representation of a delegating constructor call.
 *
 * The method processes the input constructor call element and resolves its corresponding IR constructor.
 * If the transformation is successful, it produces an [IrDelegatingConstructorCall], which represents
 * a delegating constructor call in the Kotlin intermediate representation (IR). If no transformation
 * is applicable, the method returns `null`.
 *
 * @param input The [KtConstructorCallElement] representing the constructor call to be transformed.
 * @param a An auxiliary parameter of type [Any], which can be used to provide additional information
 *          or context necessary for the transformation process. This parameter's purpose is determined
 *          by specific transformation scenarios.
 * @return An [IrDelegatingConstructorCall] if the transformation is successful; otherwise, `null`.
 */
fun KtIrLocalTransformContext.transformIrConstructorCall(
    input: KtConstructorCallElement,
    a: Any
): IrDelegatingConstructorCall? {
    input.constructor.getIr<IrConstructor>()!!.symbol
    return null
}