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

import io.github.kshulzh.kefir.ir.helper.KtIrInitDeclarationElement
import io.github.kshulzh.kefir.model.api.declaration.KtClassElement
import io.github.kshulzh.kefir.model.api.declaration.KtConstructorElement
import io.github.kshulzh.kefir.model.api.declaration.KtDeclarationElement
import io.github.kshulzh.kefir.model.api.declaration.KtFieldElement
import io.github.kshulzh.kefir.model.api.declaration.KtFunctionElement
import io.github.kshulzh.kefir.model.api.declaration.KtPropertyElement
import io.github.kshulzh.kefir.transform.context.KtFirLocalTransformContext
import io.github.kshulzh.kefir.transform.context.KtIrLocalTransformContext
import org.jetbrains.kotlin.fir.declarations.FirDeclaration
import org.jetbrains.kotlin.ir.declarations.IrDeclaration

/**
 * Transforms a given `KtDeclarationElement` into an `IrDeclaration` within the current transformation context.
 * The specific type of `IrDeclaration` produced depends on the concrete type of the input `KtDeclarationElement`.
 *
 * @param input The `KtDeclarationElement` to be transformed. Can be one of several types such as `KtClassElement`,
 *              `KtFunctionElement`, `KtPropertyElement`, `KtFieldElement`, `KtConstructorElement`, or `KtIrInitDeclarationElement`.
 * @return An `IrDeclaration` representing the transformed input, or `null` if the input type is not supported for transformation.
 */
fun KtIrLocalTransformContext.transformIrDeclaration(input: KtDeclarationElement): IrDeclaration? {
    return when (input) {
        is KtClassElement -> irTransform(input)
        is KtFunctionElement -> irTransform(input)
        is KtPropertyElement -> irTransform(input)
        is KtFieldElement -> irTransform(input)
        is KtConstructorElement -> irTransform(input)
        is KtIrInitDeclarationElement -> irTransform(input)
        else -> null
    }
}

/**
 * Transforms the given Kotlin declaration element into a FIR (Frontend Intermediate Representation) declaration.
 *
 * Depending on the type of the input element, this method delegates to the appropriate transformation
 * logic to handle specific kinds of elements such as classes, functions, properties, fields, or constructors.
 * If the element type is not supported or recognized, the method returns null.
 *
 * @param input The Kotlin declaration element to be transformed into a FIR declaration. Supported types include
 * [KtClassElement], [KtFunctionElement], [KtPropertyElement], [KtFieldElement], and [KtConstructorElement].
 * @return The resulting FIR declaration if the transformation is successful, or null if the input element
 * is not recognized or cannot be transformed.
 */
fun KtFirLocalTransformContext.transformFirDeclaration(input: KtDeclarationElement): FirDeclaration? {
    return when (input) {
        is KtClassElement -> firTransform(input)
        is KtFunctionElement -> firTransform(input)
        is KtPropertyElement -> firTransform(input)
        is KtFieldElement -> firTransform(input)
        is KtConstructorElement -> firTransform(input)
        else -> null
    }
}

