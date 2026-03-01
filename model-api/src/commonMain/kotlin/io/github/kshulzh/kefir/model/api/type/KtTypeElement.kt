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

package io.github.kshulzh.kefir.model.api.type

import io.github.kshulzh.kefir.model.api.KtElement

/**
 * Represents the base abstraction for type-related elements in the Kotlin type system.
 *
 * This interface extends [KtElement], defining a common hierarchy for various
 * type representations such as primitive types, class types, and other user-defined
 * or system-defined type elements. It serves as a unified contract for processing,
 * transformation, and analysis involving Kotlin types in intermediate and frontend
 * representations.
 *
 * Implementers of this interface can represent distinct types or type hierarchies,
 * providing essential information needed in type modeling, validation, or transformation contexts.
 */
interface KtTypeElement : KtElement