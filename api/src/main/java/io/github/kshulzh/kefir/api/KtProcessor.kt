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
 * Represents a processor capable of operating within a specific Kotlin package scope (`KtPackageScope`)
 * and context (`KtContext`).
 *
 * The `KtProcessor` interface defines the contract for processing logic to be implemented,
 * providing extensibility for various processing tasks related to Kotlin package elements.
 */
interface KtProcessor {
    /**
     * Processes the current package scope within the given context. This method can be used to perform
     * operations or modifications on the package scope, utilizing the context provided.
     *
     * @param ktContext The context in which the processing occurs. This includes the optional external
     * scope or other context-specific properties that influence or assist the operation.
     */
    fun KtPackageScope.process(ktContext: KtContext)
}