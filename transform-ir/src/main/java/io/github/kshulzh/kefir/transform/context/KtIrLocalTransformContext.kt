/*
 * Copyright (c) 2026. Kirill Shulzhenko
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


/**
 * Represents a context specific to local IR (Intermediate Representation) transformation tasks.
 * Extends the functionalities provided by the [KtTransformContext] to include capabilities
 * for managing local transformations within the IR pipeline.
 */
interface KtIrLocalTransformContext : KtTransformContext {
    /**
     * Represents the transformational context for a multi-phase analysis or IR (Intermediate Representation)
     * processing pipeline within Kotlin compilation or code transformation infrastructure.
     *
     * This context is used to carry shared state and essential utilities across various transformations,
     * including data related to the current module (`IrModuleFragment`), the IR transformations (`IrTransformer`),
     * the plugin infrastructure (`IrPluginContext`), and the structural representation (`KtIrStructure`).
     *
     * Typically, this property provides delegated access to the broader transformation context.
     */
    val transformContext: KtTransformContext

    /**
     * Represents an optional external property that can be associated with the transformation
     * context to provide additional, user-defined data.
     *
     * This property is mutable and can store any type of value or be `null` if no external data
     * is needed.
     *
     * Often employed to pass auxiliary information or to maintain a customizable state during
     * the transformation process.
     */
    var external: Any?

    /**
     * Creates a fork of the current transformation context, allowing an optional block of code to execute within the forked context.
     *
     * This method is typically used for splitting or isolating parts of the transformation process, enabling independent
     * processing of different branches.
     *
     * @param optional A lambda function representing an optional block of code to execute within the forked context. Defaults to an empty block.
     */
    fun fork(optional: () -> Unit = {})

    /**
     * Retrieves the FIR-based local transformation context associated with the current transformation.
     *
     * This method provides access to a specialized `KtFirLocalTransformContext` that extends the
     * general `KtFirTransformContext` functionality, offering additional FIR-specific capabilities
     * and properties tailored for local transformation scenarios.
     *
     * The returned context encapsulates the state and tools required for performing transformations
     * within the Frontend Intermediate Representation (FIR) pipeline. It ensures access to necessary
     * components such as the FIR transformation session, attributes, and structure management, aiding
     * in the application of modifications or adaptations during the transformation process.
     *
     * @return A `KtFirLocalTransformContext` representing the FIR-based local transformation context.
     */
    fun fir(): KtFirLocalTransformContext
}