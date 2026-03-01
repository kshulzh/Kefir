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

import io.github.kshulzh.kefir.model.api.arg.KtArgumentElement
import io.github.kshulzh.kefir.model.api.arg.KtArgumentsScope
import io.github.kshulzh.kefir.transform.context.KtTransformContext
import io.github.kshulzh.kefir.transform.context.local
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.expressions.IrExpression

fun wrapIrArguments(
    params: List<IrValueParameter>,
    expressions: MutableList<IrExpression?>,
    argumentsScope: KtArgumentsScope? = null,
    transformContext: KtTransformContext,
): List<KtArgumentElement> {
    return params.zip(expressions).mapIndexed { index, it ->
        KtIrArgumentElement(
            it.first.name.identifier,
            it.second!!,
            position = index,
            onSet = {
                expressions[index] = it?.let { ir ->
                    with(transformContext.local()) {
                        transformContext.irTransform(ir)
                    }
                }
            },
            argumentsScope = argumentsScope,
            transformContext = transformContext,
        )
    }
}