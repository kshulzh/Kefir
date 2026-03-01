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

open class KtFirLocalTransformContext1(
    override val transformContext: KtFirTransformContext,
    override val nodeStack: ArrayDeque<NodeActionImpl<*>> = ArrayDeque(),
    override var external: Any? = null
) : KtFirLocalTransformContext, KtNodeStack, KtFirTransformContext by transformContext {
    override fun fork(optional: () -> Unit) {
        nodeStack.last().optional(optional.toString()) {
            nodeStack.push(this)
            optional()
            nodeStack.pop()
        }
    }
}