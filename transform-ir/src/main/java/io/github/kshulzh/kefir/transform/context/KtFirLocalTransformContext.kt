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
 * Represents a specialized context for FIR-based local transformations.
 * Extends the general [KtFirTransformContext] with additional capabilities
 * and properties specific to local transformation use cases.
 */
interface KtFirLocalTransformContext : KtFirTransformContext {
    /**
     * Represents the current Fir transformation context. Provides access to various transformation-related components
     * such as the Fir transformer, Fir session, and structural or attribute-related data necessary for transformations.
     *
     * This context is essential for the transformation process within the FIR (Frontend Intermediate Representation)
     * pipeline, encapsulating state and behavior required for managing and applying modifications during compilation.
     */
    val transformContext: KtFirTransformContext

    /**
     * Represents an external firElement that can be associated with this implementation
     * of the transform context. This property is mutable and can hold any type of value,
     * allowing it to be adapted to various use cases within the transformation process.
     */
    var external: Any?

    /**
     * Executes a new forked task with a current context.
     *
     * @param optional A lambda function to configure the forked context. Defaults to an empty lambda.
     */
    fun fork(optional: () -> Unit = {})
}