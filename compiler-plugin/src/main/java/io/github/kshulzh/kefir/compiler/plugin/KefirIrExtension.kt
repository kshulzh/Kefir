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

package io.github.kshulzh.kefir.compiler.plugin

import io.github.kshulzh.kefir.api.KtContext
import io.github.kshulzh.kefir.api.KtProcessor
import io.github.kshulzh.kefir.model.external.KtExternalRootPackageElement
import io.github.kshulzh.kefir.model.ir.io.KtIrRootPackageElement
import io.github.kshulzh.kefir.transform.context.KtTransformContextImpl
import io.github.kshulzh.kefir.transform.transformer.AdvancedFirTransformer
import io.github.kshulzh.kefir.transform.transformer.AdvancedIrTransformer
import io.github.kshulzh.problemgraph.v1.createProblemContext
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.fir.resolve.providers.FirCompositeCachedSymbolNamesProvider
import org.jetbrains.kotlin.fir.resolve.providers.FirSymbolNamesProvider
import org.jetbrains.kotlin.fir.resolve.providers.FirSymbolProvider
import org.jetbrains.kotlin.fir.resolve.providers.impl.FirCachingCompositeSymbolProvider
import org.jetbrains.kotlin.fir.resolve.providers.symbolProvider
import org.jetbrains.kotlin.fir.symbols.SymbolInternals
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import kotlin.time.measureTime

/**
 * An implementation of the `IrGenerationExtension` interface for performing IR transformations in a Kotlin compiler plugin.
 *
 * The `KefirIrExtension` utilizes a `KtProcessor` to process Kotlin IR and FIR elements during the IR generation phase.
 * This class is responsible for setting up the appropriate transformation context and invoking the processing logic.
 *
 * @param processor An instance of `KtProcessor` used to process the Kotlin IR and FIR structures
 * within the defined transformation scope.
 */
class KefirIrExtension(
    val processor: KtProcessor,
) : IrGenerationExtension {
    /**
     * Performs the generation process within the Kotlin IR extension.
     *
     * @param moduleFragment The IR module fragment that represents the Kotlin source code being transformed.
     * @param pluginContext The plugin context that provides access to various components in the Kotlin compiler.
     */
    @OptIn(SymbolInternals::class)
    override fun generate(
        moduleFragment: IrModuleFragment,
        pluginContext: IrPluginContext
    ) {
        measureTime {
            val ktTransformContext = KtTransformContextImpl(
                moduleFragment = moduleFragment,
                pluginContext = pluginContext,
                problemContext = createProblemContext(),
                irTransform = AdvancedIrTransformer(),
                firTransform = AdvancedFirTransformer(),
            )

            //reflection is bad, but why not?
            val firCachingCompositeSymbolProvider =
                ktTransformContext.firSession.symbolProvider as FirCachingCompositeSymbolProvider
            val symbolProvider = FirCachingCompositeSymbolProvider(
                ktTransformContext.firSession,
                firCachingCompositeSymbolProvider.providers.filter { it::class.qualifiedName?.equals("org.jetbrains.kotlin.fir.resolve.providers.impl.FirProviderImpl.SymbolProvider") == false },
            )
            (firCachingCompositeSymbolProvider.providers as ArrayList<FirSymbolProvider>).add(ktTransformContext.firStructure)
            ((firCachingCompositeSymbolProvider.symbolNamesProvider as FirCompositeCachedSymbolNamesProvider).providers as ArrayList<FirSymbolNamesProvider>).add(
                ktTransformContext.firStructure.symbolNameProvider
            )

            ktTransformContext.root = KtIrRootPackageElement(ktTransformContext, moduleFragment.files)

            ktTransformContext.externalRoot  = KtExternalRootPackageElement(ktTransformContext, symbolProvider)
            val ktContext = KtContext(ktTransformContext.externalRoot)
            with(processor) {
                ktTransformContext.root.process(ktContext)
            }

            val res = ktTransformContext.problemContext?.actionManager?.resolve()
            res?.queue?.forEach {
                it.throwable?.printStackTrace()
            }
        }

        //todo add message completed in
    }
}