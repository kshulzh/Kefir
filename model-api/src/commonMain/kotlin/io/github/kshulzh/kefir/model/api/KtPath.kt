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
 * Represents a path in the Kotlin model structure composed of a list of `KtName` parts.
 * Used to define and navigate hierarchical elements or structures within the model.
 *
 * This class is implemented as a value class to enforce immutability for its usage.
 * Paths are typically expressed as dot-separated strings.
 *
 * @property parts A mutable list of `KtName` components representing the path segments.
 */
@JvmInline
value class KtPath(val parts: MutableList<KtName>) {
    /**
     * Secondary constructor for creating a [KtPath] instance using a variable number of [KtName] arguments.
     *
     * Converts the provided arguments into a mutable list and initializes the underlying parts list of the [KtPath].
     *
     * @param parts A variable number of Kotlin name strings represented as [KtName].
     */
    constructor(vararg parts: KtName) : this(parts.toMutableList())

    /**
     * Secondary constructor for the `KtPath` class, allowing initialization via a raw string.
     * The raw string is split into parts based on the period (`.`) delimiter to form
     * the list of components representing the path.
     *
     * @param raw The raw string representation of the path, where components are separated by periods (`.`).
     */
    constructor(raw: String) : this(raw.split(".").toMutableList())

    /**
     * Converts the parts of the path into a string representation, with each part
     * separated by a dot (".").
     *
     * @return A string representation of the path, constructed by joining all parts with dots.
     */
    override fun toString(): String {
        return parts.joinToString(".") { it }
    }
}