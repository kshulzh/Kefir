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

package io.github.kshulzh.kefir.model.api.modifiers

/**
 * Represents a marker interface for Kotlin modifiers used in the abstract modeling of
 * Kotlin's language features, such as visibility, modality, and other special behavior.
 *
 * A modifier is a keyword or annotation that influences the characteristics or behavior of
 * a code element, like classes, functions, or properties. Implementations of this interface
 * define specific types of modifiers, such as visibility (`public`, `private`) or
 * modality (`abstract`, `final`, `open`), among others.
 *
 * Classes or enums implementing this interface provide concrete definitions of specific types
 * of modifiers, making them applicable in the context of scoped modifier management systems.
 *
 * This interface itself does not define behavior but serves as a common type system
 * abstraction, allowing polymorphic operations across different modifier types in Kotlin.
 */
interface KtModifier