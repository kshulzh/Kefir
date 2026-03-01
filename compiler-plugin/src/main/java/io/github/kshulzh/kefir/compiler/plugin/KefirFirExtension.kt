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
import io.github.kshulzh.kefir.model.fir.io.KtFirRootPackageElement
import io.github.kshulzh.kefir.transform.context.KtFirTransformContextImpl
import io.github.kshulzh.kefir.transform.utils.KtFirStructureFir
import io.github.kshulzh.problemgraph.v1.createProblemContext
import org.jetbrains.kotlin.cli.pipeline.PipelineContext
import org.jetbrains.kotlin.cli.pipeline.metadata.MetadataFrontendPipelineArtifact
import org.jetbrains.kotlin.config.phaser.ActionState
import org.jetbrains.kotlin.fir.declarations.FirFile

/**
 * An extension of the FIR (Frontend Intermediate Representation) generation process designed to integrate
 * a Kotlin processor (`KtProcessor`) into the Metadata Frontend Pipeline phase. This class provides
 * the customization capabilities required to preprocess and transform FIR artifacts.
 *
 * @property processor The Kotlin processor responsible for processing the root package elements within the FIR pipeline.
 */
class KefirFirExtension(
    val processor: KtProcessor,
) : FirGenerationExtension {
    /**
     * Prepares and executes the pre-processing for the FIR (Front-end Intermediate Representation) pipeline.
     *
     * @param actionState The current state of the action being executed.
     * @param pipelineArtifact The artifact containing metadata and results of the frontend pipeline processing.
     * @param pipelineContext Provides contextual information needed during pipeline execution.
     */
    override fun preProcess(
        actionState: ActionState,
        pipelineArtifact: MetadataFrontendPipelineArtifact,
        pipelineContext: PipelineContext
    ) {
        val firSession = pipelineArtifact.result.outputs[0].session
        val ktFirTransformContext = KtFirTransformContextImpl(
            firSession,
            KtFirStructureFir(pipelineArtifact.result.outputs[0].fir as ArrayList<FirFile>, firSession),
            createProblemContext()
        )

        val root =
            KtFirRootPackageElement(ktFirTransformContext, pipelineArtifact.result.outputs[0].fir as ArrayList<FirFile>)
        val ktContext = KtContext()
        with(processor) {
            root.process(ktContext)
        }
        val res = ktFirTransformContext.problemContext?.actionManager?.resolve()
        res?.queue?.forEach {
            it.throwable?.printStackTrace()
        }
    }
}