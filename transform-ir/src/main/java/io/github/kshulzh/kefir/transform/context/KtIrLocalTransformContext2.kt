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
 * Represents a specialized implementation of the [KtIrLocalTransformContext] interface, providing functionalities
 * for local IR (Intermediate Representation) transformations. This class is designed to extend and leverage the capabilities
 * provided by [KtTransformContext], enabling manipulation and execution of transformations within the IR pipeline of the Kotlin
 * compilation process.
 *
 * @property transformContext The base transformation context that provides shared state and utility methods for IR transformations.
 *                             It is delegated from the [KtTransformContext] interface.
 * @property external An optional, user-defined property for attaching additional data or state to the transformation context.
 *                    Acts as a mutable container for any type of object required during the transformation process.
 */
open class KtIrLocalTransformContext2(
    override val transformContext: KtTransformContext,
    override var external: Any? = null,
) : KtIrLocalTransformContext, KtTransformContext by transformContext {
    /**
     * Invokes the provided optional function within a try-catch block to safely execute a potentially throwable operation.
     *
     * @param optional A lambda function to be executed. The function does not require any input parameters and does not return a value.
     */
    override fun fork(optional: () -> Unit) {
        try {
            optional()
        } catch (e: Throwable) {
            //todo
        }
    }

    /**
     * Returns a new instance of a specialized FIR-based transformation context.
     * This method initializes and provides access to a [KtFirLocalTransformContext2] instance,
     * which delegates to the current transformation context.
     *
     * @return A [KtFirLocalTransformContext] object that encapsulates the current transformation context
     *         for FIR-based compilation pipelines.
     */
    override fun fir(): KtFirLocalTransformContext = KtFirLocalTransformContext2(transformContext)
}