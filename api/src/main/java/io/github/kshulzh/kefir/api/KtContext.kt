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
 * Represents the context in which Kotlin processing operations can occur.
 *
 * The `KtContext` class encapsulates optional external context given by a `KtPackageScope`.
 * This external scope can provide additional package-level functionality or structure
 * for Kotlin code processing tasks.
 *
 * @property external An optional external `KtPackageScope` that can be used for extended scope or operations.
 */
class KtContext(
    val external: KtPackageScope? = null
)