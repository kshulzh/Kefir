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

package io.github.kshulzh.kefir.model.api

/**
 * Represents an external Kotlin element within the model.
 *
 * This interface extends [KtElement] to signify elements that originate outside the primary Kotlin source,
 * such as those introduced via external libraries or not directly defined within the primary source code.
 *
 * Instances of this type often include dynamic or generated elements, possibly interacting with
 * external contexts, APIs, or tooling, as exemplified by its usage in various external package scopes.
 *
 * Provides a foundation for defining additional behavior or properties specifically for externally defined elements.
 */
interface KtExternalElement : KtElement