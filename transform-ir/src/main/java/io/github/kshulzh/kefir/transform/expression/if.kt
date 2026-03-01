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
import io.github.kshulzh.kefir.transform.utils.type.resolveType
import org.jetbrains.kotlin.ir.expressions.IrWhen

/**
 * Transforms a Kotlin `KtSetFieldElement` into its corresponding IR (`Intermediate Representation`) conditional construct.
 * The transformation process involves converting various components of the `KtSetFieldElement` such as its type, value,
 * and receiver into their IR representations.
 *
 * @param input The `KtSetFieldElement` to be transformed, representing a field assignment expression in the Kotlin AST.
 *              This includes components like the field type, receiver, and value that require transformation.
 * @return The transformed `IrWhen` representing the conditional IR structure related to the input, or `null` if the transformation fails.
 */
fun KtIrLocalTransformContext.transformIrIf(input: KtSetFieldElement): IrWhen? {
    //todo implement

    irTransform(input.resolveType()!!)!!
    irTransform(input.value!!)!!
    irTransform(input.receiver!!)!!
    return null
}