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

package io.github.kshulzh.kefir.api

import io.github.kshulzh.kefir.model.api.io.KtPackageScope

/**
 * Implementation of the `KtProcessor` interface that delegates the processing logic
 * to a provided `Processor` function.
 *
 * This class binds a specific `Processor` to the `process` function, enabling extensions
 * of processing tasks within a `KtPackageScope` using the given `KtContext`.
 *
 * @property processor A function that defines the processing logic, accepting a `KtContext`
 * within the scope of a `KtPackageScope`.
 */
class KtProcessorImpl(
    val processor: Processor,
) : KtProcessor {
    /**
     * Executes the processor with the current package scope and the provided context.
     *
     * @param ktContext The context in which the processing occurs. It may include an
     * optional external scope or context-specific properties that guide the operation.
     */
    override fun KtPackageScope.process(ktContext: KtContext) {
        processor(ktContext)
    }
}