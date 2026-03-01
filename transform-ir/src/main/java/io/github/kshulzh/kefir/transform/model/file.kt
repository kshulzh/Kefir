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

package io.github.kshulzh.kefir.transform.model

import io.github.kshulzh.kefir.model.api.io.KtFileElement
import io.github.kshulzh.kefir.transform.context.KtTransformContext
import io.github.kshulzh.kefir.transform.context.local
import org.jetbrains.kotlin.ir.ObsoleteDescriptorBasedAPI
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment

/**
 * Adds a Kotlin file as an `IrFile` to the `IrModuleFragment`. This method processes the given
 * `KtFileElement` using the provided transformation context and integrates it into the module's
 * collection of IR files based on package and declaration hierarchy.
 *
 * @param file Represents a Kotlin file element within a package scope, used as the input
 *             for transformation into an intermediate representation file.
 * @param transformContext The transformation context containing utilities and settings
 *                         for converting the provided file element into an IR representation.
 * @return The transformed IR file that was added to the module's files collection.
 */
@OptIn(ObsoleteDescriptorBasedAPI::class)
fun IrModuleFragment.addFile(file: KtFileElement, transformContext: KtTransformContext): IrFile {
    return with(transformContext.local()) {
        transformContext.irTransform(file)!!.also {
            for ((index, file) in files.withIndex()) {
                if (file.packageFqName.asString() >= it.packageFqName.asString()) {
                    files.add(index, it)
                    return@also
                }
                if (it.symbol.descriptor.containingDeclaration != file.symbol.descriptor.containingDeclaration) {
                    files.add(index, it)
                    return@also
                }
            }
            files.add(it)
        }
    }
}