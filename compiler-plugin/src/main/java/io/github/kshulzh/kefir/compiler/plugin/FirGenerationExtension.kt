package io.github.kshulzh.kefir.compiler.plugin

import org.jetbrains.kotlin.cli.pipeline.PipelineArtifact
import org.jetbrains.kotlin.cli.pipeline.PipelineContext
import org.jetbrains.kotlin.cli.pipeline.metadata.MetadataFrontendPipelineArtifact
import org.jetbrains.kotlin.config.phaser.Action
import org.jetbrains.kotlin.config.phaser.ActionState

/**
 * An extension interface for FIR (Frontend Intermediate Representation) generation that applies
 * custom preprocessing actions during the Metadata Frontend Pipeline phase of the Kotlin compiler.
 *
 * This interface is intended to be implemented by extensions that want to interact with the FIR
 * generation process. It allows developers to define and execute preprocessing logic on FIR artifacts
 * prior to further compilation phases.
 *
 * FirGenerationExtension extends the `Action` interface to integrate seamlessly into the
 * pipeline framework utilized within the compiler.
 */
interface FirGenerationExtension : Action<PipelineArtifact, PipelineContext> {
    /**
     * Invokes the action with the provided state, artifact, and context.
     * If the artifact is of type `MetadataFrontendPipelineArtifact`, the `preProcess` method is invoked.
     *
     * @param p1 The current state of the action being executed.
     * @param p2 The pipeline artifact to be processed.
     * @param p3 The context of the pipeline that provides additional metadata and utilities.
     */
//    NamedCompilerPhase::class.java.getDeclaredField("preactions").apply {
//        isAccessible = true
//        @Suppress("UNCHECKED_CAST")
//        set(MetadataKlibSerializerPhase,(get(MetadataKlibSerializerPhase) as Set<Action<PipelineArtifact, PipelineContext>>) +
//                KefirFirExtension(KtCompositeProcessor(processors)))
//    }
    override fun invoke(
        p1: ActionState,
        p2: PipelineArtifact,
        p3: PipelineContext
    ) {
        if (p2 is MetadataFrontendPipelineArtifact) {
            preProcess(p1, p2, p3)
        }
    }

    /**
     * Performs preprocessing before executing the pipeline operations.
     *
     * @param actionState The current state of the pipeline action, representing the ongoing phase of execution.
     * @param pipelineArtifact The artifact containing metadata for the frontend pipeline, used as input for preprocessing.
     * @param pipelineContext The pipeline context providing information and utilities for the ongoing compilation or processing task.
     */
    fun preProcess(
        actionState: ActionState,
        pipelineArtifact: MetadataFrontendPipelineArtifact,
        pipelineContext: PipelineContext
    )
}