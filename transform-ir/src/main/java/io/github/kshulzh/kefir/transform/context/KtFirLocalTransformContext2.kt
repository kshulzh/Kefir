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
 * Represents a specialized implementation of the [KtFirLocalTransformContext] interface, extending its functionality
 * while delegating to an instance of [KtFirTransformContext]. This class is designed for enabling local transformations
 * within the FIR-based compilation pipeline.
 *
 * @constructor Creates an instance of [KtFirLocalTransformContext2].
 * @param transformContext The base transformation context to delegate to.
 * @param external An external FIR element or data associated with the transformation. Defaults to null.
 */
open class KtFirLocalTransformContext2(
    override val transformContext: KtFirTransformContext,
    override var external: Any? = null
) : KtFirLocalTransformContext, KtFirTransformContext by transformContext {
    /**
     * Executes the provided optional block of code and handles any thrown exceptions.
     *
     * @param optional A lambda function to be executed. It is expected to contain the code that might require
     *                 transformation or execution in a specific context.
     */
    override fun fork(optional: () -> Unit) {
        try {
            optional()
        } catch (e: Throwable) {
            //todo
        }
    }
}