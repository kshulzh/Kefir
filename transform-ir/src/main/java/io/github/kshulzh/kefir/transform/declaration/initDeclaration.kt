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
import io.github.kshulzh.kefir.transform.context.KtIrLocalTransformContext
import io.github.kshulzh.kefir.transform.utils.initialize
import io.github.kshulzh.kefir.transform.utils.linkIr
import org.jetbrains.kotlin.ir.declarations.IrDeclaration

/**
 * Transforms a given IR (Intermediate Representation) initialization declaration element
 * into its corresponding IR declaration by initializing and linking it.
 *
 * @param input The initialization declaration element to be transformed. This element represents
 * an IR initialization declaration and contains the necessary initialization logic.
 * @return The resulting IR declaration after transformation, or `null` if the transformation
 * cannot be performed.
 */
fun KtIrLocalTransformContext.transformIrInitDeclaration(input: KtIrInitDeclarationElement): IrDeclaration? {
    return input.initialize().linkIr(input)
}