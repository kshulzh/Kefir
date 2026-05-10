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

package io.github.kshulzh.kefir.transform.context

import io.github.kshulzh.kefir.model.api.io.KtPackageScope
import io.github.kshulzh.kefir.transform.transformer.FirTransformer
import io.github.kshulzh.kefir.transform.transformer.IrTransformer
import io.github.kshulzh.kefir.transform.utils.KtFirStructureImpl
import io.github.kshulzh.kefir.transform.utils.KtIrStructure
import io.github.kshulzh.problemgraph.context.ProblemContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.backend.Fir2IrComponentsStorage
import org.jetbrains.kotlin.fir.backend.Fir2IrPluginContext
import org.jetbrains.kotlin.fir.declarations.FirClass
import org.jetbrains.kotlin.fir.declarations.FirFile
import org.jetbrains.kotlin.fir.descriptors.FirModuleDescriptor
import org.jetbrains.kotlin.fir.resolve.providers.FirCompositeCachedSymbolNamesProvider
import org.jetbrains.kotlin.fir.resolve.providers.symbolProvider
import org.jetbrains.kotlin.fir.scopes.impl.FirClassDeclaredMemberScopeImpl
import org.jetbrains.kotlin.fir.scopes.impl.FirDeclaredMemberScopeProvider
import org.jetbrains.kotlin.fir.scopes.impl.declaredMemberScopeProvider
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment

/**
 * Implementation of the [KtTransformContext] interface. This class provides the transformation
 * context necessary for processing Kotlin FIR (Frontend IR) and IR (Intermediate Representation)
 * constructs within the compiler pipeline.
 *
 * @property moduleFragment The fragment of the IR module being processed.
 * @property pluginContext The plugin-specific context used during the transformation process.
 * @property problemContext Optional context for error handling and reporting issues during transformations.
 * @property attributes Implementation of attributes that can be used to pass and modify metadata.
 * @property firTransform Instance of [FirTransformer], which supports transforming various FIR elements.
 * @property irTransform Instance of [IrTransformer], which facilitates transformation within the IR phase.
 * @property irStructure Provides details of the structure for IR-based transformations.
 * @property firSession The session object associated with FIR transformations, providing access to FIR-related components.
 * @property firStructure Contains information about the structure of FIR elements and their relationships.
 */
class KtTransformContextImpl(
    override val moduleFragment: IrModuleFragment,
    override val pluginContext: IrPluginContext,
    override val problemContext: ProblemContext?,
    override var attributes: MutableMap<String, Any> = mutableMapOf(),
    override val firTransform: FirTransformer = FirTransformer(),
    override val irTransform: IrTransformer = IrTransformer(),
) : KtTransformContext {
    /**
     * Represents a structure used to manage and store IR (Intermediate Representation)
     * elements and metadata within the transformation context of Kotlin compilation.
     *
     * This property serves as a utility for organizing and maintaining various IR-related
     * components and mappings, particularly in the process of converting FIR (Frontend Intermediate
     * Representation) elements to their IR counterparts. Through this structure, IR classifiers and
     * other related entities are handled and stored efficiently.
     *
     * The `irStructure` is integral to the transformation pipeline, aiding in operations that
     * involve interaction with or modification of IR elements during the compilation or plugin
     * development process.
     */
    override val irStructure: KtIrStructure

    /**
     * Represents the session associated with the FIR (Frontend Intermediate Representation) processing
     * within the transformation context.
     *
     * The `firSession` property provides access to the session's contextual information and resources
     * required for analyzing and transforming the source code at the FIR stage. It allows interaction
     * with various features and utilities within the Kotlin compilation pipeline, such as symbol resolution,
     * type analysis, and other FIR-specific operations.
     *
     * Typically, this session is tied to a specific module and is initialized using the descriptor of the module
     * being transformed.
     */
    override val firSession: FirSession = (moduleFragment.descriptor as FirModuleDescriptor).session

    /**
     * Represents an implementation of the KtFirStructure within the transformation context.
     * This structure encapsulates various aspects of FIR (Frontend Intermediate Representation)
     * and its interaction with the Kotlin compiler frontend.
     *
     * This property is overridden to provide an instance of `KtFirStructureImpl`, which handles
     * files, classes, callables, and other related entities within the FIR-based structure
     * of the Kotlin compiler.
     */
    override val firStructure: KtFirStructureImpl
    override lateinit var root: KtPackageScope
    override lateinit var externalRoot: KtPackageScope

    init {
        val fir2IrComponents = Fir2IrPluginContext::class.java.getDeclaredField("c").let {
            it.isAccessible = true
            it.get(pluginContext)
        } as Fir2IrComponentsStorage

        val files = (fir2IrComponents.fir as? ArrayList<FirFile>)

        val firClassDeclaredMemberScopes =
            FirDeclaredMemberScopeProvider::class.java.getDeclaredField("declaredMemberCache").let {
                it.isAccessible = true
                val declaredMemberScopeProvider = it.get(firSession.declaredMemberScopeProvider)
                val declaredMemberScopeProviderClass = declaredMemberScopeProvider.javaClass
                declaredMemberScopeProviderClass.getDeclaredField("map").let { it2 ->
                    it2.isAccessible = true
                    @Suppress("UNCHECKED_CAST")
                    it2.get(declaredMemberScopeProvider) as MutableMap<FirClass, FirClassDeclaredMemberScopeImpl>
                }
            }

        val nestedClasses = FirDeclaredMemberScopeProvider::class.java.getDeclaredField("nestedClassifierCache").let {
            it.isAccessible = true
            val f = it.get(firSession.declaredMemberScopeProvider)
            val c = f.javaClass
            c.getDeclaredField("map").let { it2 ->
                it2.isAccessible = true
                @Suppress("UNCHECKED_CAST")
                it2.get(f) as MutableMap<FirClass, Any?>
            }
        }

        val firCompositeCachedSymbolNamesProvider =
            firSession.symbolProvider.symbolNamesProvider as FirCompositeCachedSymbolNamesProvider
        firStructure = KtFirStructureImpl(
            files = files!!,
            session = firSession,
            fir2IrComponents = fir2IrComponents,
            firClassDeclaredMemberScopes = firClassDeclaredMemberScopes,
            nestedClasses = nestedClasses,
            firCompositeCachedSymbolNamesProvider = firCompositeCachedSymbolNamesProvider
        )
        irStructure = KtIrStructure(fir2IrComponents)
    }
}