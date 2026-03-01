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

package io.github.kshulzh.kefir.transform.statement

import io.github.kshulzh.kefir.ir.helper.KtIrInitStatementElement
import io.github.kshulzh.kefir.transform.context.KtIrLocalTransformContext
import io.github.kshulzh.kefir.transform.utils.initialize
import io.github.kshulzh.kefir.transform.utils.linkIr
import org.jetbrains.kotlin.ir.IrStatement

/**
 * Transforms a `KtIrInitStatementElement` into an `IrStatement` by initializing it with
 * the specified IR construction logic and linking it to the source element.
 *
 * @param input The `KtIrInitStatementElement` representing the IR-backed statement to be transformed.
 * @return An `IrStatement` object resulting from the transformation, or `null` if the transformation fails.
 */
fun KtIrLocalTransformContext.transformIrInitStatement(input: KtIrInitStatementElement): IrStatement? {
    return input.initialize().linkIr(input)
}