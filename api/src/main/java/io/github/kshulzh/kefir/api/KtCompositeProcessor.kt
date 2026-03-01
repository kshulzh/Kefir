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
 * A composite implementation of the `KtProcessor` interface that delegates processing
 * operations to a list of individual `KtProcessor` instances.
 *
 * This class is used to aggregate multiple processors into a single processor,
 * enabling batch execution of `process` calls on all contained processors.
 *
 * @property processors A list of `KtProcessor` instances that will participate in processing.
 */
class KtCompositeProcessor(
    val processors: List<KtProcessor>,
) : KtProcessor {
    /**
     * Processes the current package scope by delegating processing to all contained processors.
     *
     * @param ktContext The context within which the processing occurs. It may include optional
     * external scope or additional context-related properties.
     */
    override fun KtPackageScope.process(ktContext: KtContext) {
        processors.forEach {
            it.apply {
                process(ktContext)
            }
        }
    }
}