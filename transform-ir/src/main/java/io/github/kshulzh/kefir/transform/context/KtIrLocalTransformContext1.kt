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

import io.github.kshulzh.problemgraph.action.NodeActionImpl
import io.github.kshulzh.problemgraph.v1.optional
import org.jetbrains.kotlin.backend.common.pop
import org.jetbrains.kotlin.backend.common.push

/**
 * Represents a specialized implementation of the [KtIrLocalTransformContext] for managing local
 * transformations in the Kotlin Intermediate Representation (IR) pipeline.
 *
 * This class extends the basic functionality provided by [KtIrLocalTransformContext] by implementing
 * shared behavior for managing node actions via [KtNodeStack] and delegating to a broader transformation
 * context through [KtTransformContext].
 *
 * @constructor Creates an instance of [KtIrLocalTransformContext1].
 * @param transformContext The transformation context to be delegated to, providing shared state and utilities
 *                         for the transformation process.
 * @param nodeStack A stack used to record actions associated with nodes during the transformation process.
 *                  Defaults to an empty [ArrayDeque].
 * @param external An optional external value for storing user-specific data. Default is `null`.
 */
open class KtIrLocalTransformContext1(
    override val transformContext: KtTransformContext,
    override val nodeStack: ArrayDeque<NodeActionImpl<*>> = ArrayDeque(),
    override var external: Any? = null,
) : KtIrLocalTransformContext, KtNodeStack, KtTransformContext by transformContext {
    /**
     * Executes a given block of code within the context of the last node in the node stack.
     * The method ensures that the newly added node is correctly pushed and popped from the stack,
     * maintaining structural integrity even in the presence of exceptions.
     *
     * @param optional A block of code to execute within the context of the current node.
     *                 This function is responsible for applying transformations or actions
     *                 specific to the node.
     */
    override fun fork(optional: () -> Unit) {
        nodeStack.last().optional(optional.toString()) {
            nodeStack.push(this)
            try {
                optional()
            } finally {
                nodeStack.pop()
            }
        }
    }

    /**
     * Creates and returns a new instance of `KtFirLocalTransformContext1` using the current
     * transformation context and node stack. This method is used to transition between
     * different phases or components within the FIR-based transformation context.
     *
     * @return A new instance of `KtFirLocalTransformContext` specifically implemented as `KtFirLocalTransformContext1`.
     */
    override fun fir(): KtFirLocalTransformContext = KtFirLocalTransformContext1(transformContext, nodeStack)
}