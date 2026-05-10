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

package io.github.kshulzh.kefir.transform.declaration

import io.github.kshulzh.kefir.model.api.declaration.KtFieldElement
import io.github.kshulzh.kefir.transform.context.KtFirLocalTransformContext
import io.github.kshulzh.kefir.transform.context.KtIrLocalTransformContext
import io.github.kshulzh.kefir.transform.utils.getIrOrExternal
import io.github.kshulzh.kefir.transform.utils.linkIr
import io.github.kshulzh.kefir.transform.utils.transform
import io.github.kshulzh.kefir.transform.utils.type.resolveType
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.fir.declarations.FirField
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrField
import org.jetbrains.kotlin.ir.symbols.impl.IrFieldSymbolImpl

/**
 * Transforms a given `KtFieldElement` into an `IrField` representation within the IR transformation pipeline.
 *
 * This method resolves the type of the input field, creates a new `IrField` with the resolved type,
 * and links the field to its parent IR or external scope. If the field has an initializer value,
 * the value is transformed and assigned as the field's initializer expression.
 *
 * @param input The `KtFieldElement` to be transformed. Represents a field definition in the Kotlin model.
 * @return An `IrField` representing the transformed field, or `null` if the transformation context fails.
 */
fun KtIrLocalTransformContext.transformIrField(input: KtFieldElement): IrField? {
    val type = irTransform(input.resolveType()!!)!!

    return transformContext.pluginContext.irFactory.createField(
        startOffset = UNDEFINED_OFFSET,
        endOffset = UNDEFINED_OFFSET,
        origin = IrDeclarationOrigin.DEFINED,
        name = input.name.transform(),
        visibility = DescriptorVisibilities.PRIVATE,
        symbol = IrFieldSymbolImpl(),
        type = type,
        isStatic = false,
        isExternal = false,
        isFinal = false
    ).linkIr(input).apply {
        fork {
            initializer = input.value?.let { value ->
                transformContext.irTransform(value)?.let {
                    transformContext.pluginContext.irFactory.createExpressionBody(
                        UNDEFINED_OFFSET,
                        UNDEFINED_OFFSET,
                        it
                    )
                }
            }
        }
        fork {
            parent = input.declarationsScope.getIrOrExternal()!!
        }

    }
}

/**
 * Transforms a `KtFieldElement` into an equivalent `FirField` representation within the current
 * FIR transformation context. This operation adapts the provided field element by converting it
 * into the appropriate Frontend Intermediate Representation (FIR).
 *
 * @param input The `KtFieldElement` to be transformed into a `FirField`. Represents a Kotlin field
 * element in the source model structure.
 * @return A `FirField` instance that corresponds to the given `KtFieldElement`, or null if the
 * transformation cannot be performed.
 */
fun KtFirLocalTransformContext.transformFirField(input: KtFieldElement): FirField? {
    //todo implement
    return null
}