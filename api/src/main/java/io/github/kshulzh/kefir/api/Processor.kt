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
 * Represents a type alias for a function that processes a `KtContext` within the scope of a `KtPackageScope`.
 *
 * This alias defines the structure of a processing logic function, allowing operations or modifications
 * to be performed on a `KtPackageScope` while utilizing contextual information from a `KtContext`.
 *
 * The `Processor` function is expected to be invoked with the instance of a `KtPackageScope` as the receiver
 * and a `KtContext` as a parameter. Implementations can leverage the package scope's properties and
 * functionality alongside the given context to perform specific processing tasks.
 */
typealias Processor = KtPackageScope.(KtContext) -> Unit