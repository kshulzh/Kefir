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

import io.github.kshulzh.kefir.model.api.expression.KtDelegatingConstructorCallElement
import io.github.kshulzh.kefir.transform.context.KtIrLocalTransformContext
import io.github.kshulzh.kefir.transform.utils.getIr
import org.jetbrains.kotlin.ir.backend.js.ir.JsIrBuilder
import org.jetbrains.kotlin.ir.declarations.IrConstructor
import org.jetbrains.kotlin.ir.expressions.IrDelegatingConstructorCall

/**
 * Transforms a delegating constructor call element from the Kotlin AST into an IR (Intermediate Representation)
 * delegating constructor call.
 *
 * This function converts a [KtDelegatingConstructorCallElement] into an [IrDelegatingConstructorCall] while ensuring
 * that all arguments are appropriately transformed and mapped to the target IR structure.
 *
 * @param input The [KtDelegatingConstructorCallElement] to be transformed into an IR representation.
 *              It represents a constructor call that delegates to another constructor.
 * @return An [IrDelegatingConstructorCall] corresponding to the input element, or `null` if the transformation
 *         cannot be performed.
 */
fun KtIrLocalTransformContext.transformIrDelegatingConstructorCall(input: KtDelegatingConstructorCallElement): IrDelegatingConstructorCall? {
    val target = input.constructor.getIr<IrConstructor>()!!.symbol
    return JsIrBuilder.buildDelegatingConstructorCall(
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