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

package io.github.kshulzh.kefir.transform.utils

import io.github.kshulzh.kefir.model.api.KtElement
import io.github.kshulzh.kefir.model.api.attributes
import io.github.kshulzh.kefir.transform.context.KtFirLocalTransformContext
import io.github.kshulzh.kefir.transform.context.KtIrLocalTransformContext
import io.github.kshulzh.problemgraph.action.NodeActionImpl
import io.github.kshulzh.problemgraph.context.ProblemContext

/**
 * Retrieves or computes a `NodeActionImpl` instance associated with the current `KtElement`.
 * If the associated `NodeActionImpl` is not already present, it initializes it using the provided
 * `transformer` function and associates it with the current element.
 *
 * @param transformer A lambda function that defines the transformation logic for the given `KtElement`.
 *                     The function is invoked on the `NodeActionImpl` instance, and it takes the current
 *                     `KtElement` as a parameter.
 * @return A `NodeActionImpl` instance containing the result of the transformation, retrieved or computed
 *         for the current `KtElement`.
 */
context(c: KtFirLocalTransformContext)
fun <T : KtElement, I> T.getNodeFir(transformer: NodeActionImpl<I>.(T) -> I): NodeActionImpl<I> {
    @Suppress("UNCHECKED_CAST")
    return this.attributes.getOrPut("nodeFir")
    { NodeActionImpl(c.problemContext!!) { transformer(this@getNodeFir)!! } } as NodeActionImpl<I>
}

/**
 * Retrieves or creates a `NodeActionImpl` instance associated with the current `KtElement`.
 * Uses the provided transformer function to initialize the `NodeActionImpl` if it does not already exist.
 *
 * @param transformer A transformation function to compute the `NodeActionImpl` instance.
 *                    This function operates on the current `KtElement` receiver and provides
 *                    the necessary implementation details for the returned `NodeActionImpl`.
 * @return An instance of `NodeActionImpl` that is either retrieved from the attributes of the
 *         current `KtElement` or computed using the provided transformer function.
 */
context(c: KtIrLocalTransformContext)
fun <T : KtElement, I> T.getNodeIr(transformer: NodeActionImpl<I>.(T) -> I): NodeActionImpl<I> {
    @Suppress("UNCHECKED_CAST")
    return this.attributes.getOrPut("nodeIr")
    { NodeActionImpl(c.problemContext!!) { transformer(this@getNodeIr)!! } } as NodeActionImpl<I>
}

/**
 * Submits a `NodeActionImpl` task to the `actionManager` with the specified action.
 *
 * @param action The action to be performed within the context of the `NodeActionImpl`.
 *               This action is invoked with the `NodeActionImpl` receiver.
 */
fun ProblemContext.submit(action: NodeActionImpl<*>.() -> Unit) {
    actionManager.submit(NodeActionImpl(this) {
        action()
    })
}