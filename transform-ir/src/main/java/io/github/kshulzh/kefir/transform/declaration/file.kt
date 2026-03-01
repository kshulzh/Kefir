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

import io.github.kshulzh.kefir.model.api.io.KtFileElement
import io.github.kshulzh.kefir.model.api.io.path
import io.github.kshulzh.kefir.transform.context.KtFirLocalTransformContext
import io.github.kshulzh.kefir.transform.context.KtIrLocalTransformContext
import io.github.kshulzh.kefir.transform.utils.createSource
import io.github.kshulzh.kefir.transform.utils.linkFir
import io.github.kshulzh.kefir.transform.utils.linkIr
import io.github.kshulzh.kefir.transform.utils.transform
import org.jetbrains.kotlin.KtSourceFile
import org.jetbrains.kotlin.descriptors.impl.EmptyPackageFragmentDescriptor
import org.jetbrains.kotlin.fir.backend.FirMetadataSource
import org.jetbrains.kotlin.fir.builder.buildPackageDirective
import org.jetbrains.kotlin.fir.declarations.*
import org.jetbrains.kotlin.fir.declarations.builder.buildFile
import org.jetbrains.kotlin.fir.moduleData
import org.jetbrains.kotlin.fir.symbols.impl.FirFileSymbol
import org.jetbrains.kotlin.ir.ObsoleteDescriptorBasedAPI
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.declarations.impl.IrFileImpl
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.symbols.impl.IrFileSymbolImpl
import org.jetbrains.kotlin.ir.util.NaiveSourceBasedFileEntryImpl
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.resolve.descriptorUtil.module
import java.io.InputStream


/**
 * Transforms a given Kotlin file element (`KtFileElement`) into an IR (Intermediate Representation) file (`IrFile`).
 * This method performs source-based IR file creation, linking the IR elements with their corresponding source elements,
 * and applies specific transformation contexts using the associated FIR and IR pipelines.
 *
 * @param input The source Kotlin file element to be transformed into an IR file.
 * @return The resulting IR file (`IrFile`) after the transformation is performed, or `null` if the transformation fails.
 */
@OptIn(UnsafeDuringIrConstructionAPI::class, ObsoleteDescriptorBasedAPI::class)
fun KtIrLocalTransformContext.transformIrFile(input: KtFileElement): IrFile? {
    val fileEntry = NaiveSourceBasedFileEntryImpl(input.getFilePath(), intArrayOf(0, 10, 25))

    val fileSymbol = IrFileSymbolImpl(
        EmptyPackageFragmentDescriptor(
            pluginContext.moduleDescriptor.module,
            input.path.transform()
                ?: FqName.Companion.ROOT
        )
    )
    // Create an IR file
    val irFile = IrFileImpl(
        fileEntry = fileEntry,
        symbol = fileSymbol,
        packageFqName = input.path.transform()
            ?: FqName.Companion.ROOT,
    ).linkIr(input)


    return irFile.apply {
        fork {
            metadata = FirMetadataSource.File(with(this@transformIrFile.fir()) { firTransform(input)!! })
        }

        fork {
            input.declarations.forEach {
                declarations.add(irTransform(it)!!)
            }
        }
        module = moduleFragment
    }
}

/**
 * Transforms a given [KtFileElement] into a corresponding FIR (Frontend Intermediate Representation) [FirFile].
 * This method creates a FIR representation of the Kotlin source file for further processing during the
 * compilation pipeline.
 *
 * @param input the Kotlin file element to be transformed into a FIR file representation.
 * @return the resulting [FirFile] representation if successful, or `null` if the transformation fails.
 */
@OptIn(DirectDeclarationsAccess::class)
fun KtFirLocalTransformContext.transformFirFile(input: KtFileElement): FirFile? {
    return buildFile {
        sourceFile = KtKefirSourceFile(input)
        resolvePhase = FirResolvePhase.BODY_RESOLVE
        source = createSource()
        //annotations = mutableListOf()
        moduleData = transformContext.firSession.moduleData
        origin = FirDeclarationOrigin.Source
        attributes = FirDeclarationAttributes()
        packageDirective = buildPackageDirective {
            source = createSource()
            input.path.also {
                packageFqName = it.transform() ?: FqName.ROOT
            }

        }
        name = input.name
        //imports = mutableListOf()
        symbol = FirFileSymbol()
        sourceFileLinesMapping = null

        //todo add
    }.linkFir(input).also {
        val declarations = it.declarations as MutableList<FirDeclaration>
        fork {
            input.declarations.forEach { declaration ->
                declarations.add(firTransform(declaration)!!)
            }
        }
        firStructure.addFile(it)
    }
}

/**
 * A representation of a Kotlin source file within the compiler framework, specifically wrapping a [KtFileElement].
 *
 * This class provides access to the file's name, path, and its contents as a stream.
 */
class KtKefirSourceFile(val file: KtFileElement) : KtSourceFile {
    /**
     * Represents the name of the underlying file associated with this source file.
     * The value is derived from the `name` property of the `file` object.
     */
    override val name: String
        get() = file.name

    /**
     * Represents the file path of the source file.
     *
     * The path is derived from the associated `KtFileElement` instance
     * and is constructed by transforming the file's path, replacing "."
     * with "/", and appending the file name.
     *
     * This property may return null if the file path cannot be determined.
     */
    override val path: String?
        get() = file.getFilePath()

    /**
     * Provides the contents of the source file as an InputStream.
     *
     * @return an InputStream representing the contents of the source file
     */
    override fun getContentsAsStream(): InputStream {
        TODO("Not yet implemented")
    }
}

/**
 * Constructs the file path for the current `KtFileElement`.
 *
 * This method transforms the `path` property of the `KtFileElement` into a fully qualified string
 * representation, replaces all occurrences of "." with "/", and appends the file's `name` at the end.
 *
 * @receiver The `KtFileElement` for which the file path is to be generated.
 * @return A string representing the file path, combining the transformed path and the file name.
 */
fun KtFileElement.getFilePath() = (path.transform()?.asString()?.replace(".", "/") ?: "") + ("/$name")