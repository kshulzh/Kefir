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

import io.github.kshulzh.kefir.model.api.declaration.KtDeclarationElement
import io.github.kshulzh.kefir.transform.context.KtTransformContext
import io.github.kshulzh.kefir.transform.context.local
import org.jetbrains.kotlin.fir.backend.FirMetadataSource
import org.jetbrains.kotlin.fir.declarations.DirectDeclarationsAccess
import org.jetbrains.kotlin.fir.declarations.FirDeclaration
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrDeclarationContainer
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.declarations.IrMetadataSourceOwner
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI

/**
 * Adds a new IR declaration to the current container by transforming the given Kotlin declaration
 * element within the specified transformation context. The transformed IR declaration is then added
 * to the list of declarations in the container.
 *
 * @param declaration The Kotlin declaration element to be transformed into an IR declaration.
 * @param transformContext The context used for transforming the Kotlin declaration into its IR representation.
 */
@OptIn(UnsafeDuringIrConstructionAPI::class)

fun IrDeclarationContainer.addDeclaration1(declaration: KtDeclarationElement, transformContext: KtTransformContext) =
    with(transformContext.local(this)) {
        transformContext.irTransform(declaration)!!.also {
            declarations.add(it)
        }
    }

/**
 * Adds a declaration to the current [IrClass] within the provided transformation context.
 *
 * This method facilitates the transformation of a [KtDeclarationElement] into its corresponding IR (Intermediate Representation)
 * form, integrating the transformed declaration into the class. Additionally, it updates relevant metadata associated
 * with the transformation process, ensuring the integrity of the class's metadata structure.
 *
 * @param declaration The Kotlin declaration element to be transformed and added to the class.
 * @param transformContext The transformation context used for managing and applying the transformation of the Kotlin declaration to IR.
 */
@OptIn(UnsafeDuringIrConstructionAPI::class, DirectDeclarationsAccess::class)
fun IrClass.addDeclaration(declaration: KtDeclarationElement, transformContext: KtTransformContext) =
    addDeclaration1(declaration, transformContext).also { declaration ->
        val metadataSource = metadata
        when (metadataSource) {
            is FirMetadataSource.Class -> {
                (metadataSource.fir.declarations as? ArrayList<FirDeclaration>)?.also { declarations ->
                    ((declaration as? IrMetadataSourceOwner)?.metadata as? FirMetadataSource)?.also {
                        declarations.add(it.fir)
                    }
                }
            }
        }

    }

/**
 * Adds a Kotlin declaration element to an IR file within the transformation context.
 *
 * This function facilitates the inclusion of a given `KtDeclarationElement` into the `IrFile`
 * during the intermediate representation (IR) transformation process. It uses the provided
 * transformation context to maintain consistency with the ongoing IR modifications.
 *
 * @param declaration The Kotlin declaration element to be added to the IR file.
 * @param transformContext The context used for IR transformation, providing necessary utilities
 *        and services to process and integrate the declaration into the IR structure.
 */
@OptIn(UnsafeDuringIrConstructionAPI::class)
fun IrFile.addDeclaration(declaration: KtDeclarationElement, transformContext: KtTransformContext) =
    addDeclaration1(declaration, transformContext)