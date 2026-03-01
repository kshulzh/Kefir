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

package io.github.kshulzh.kefir.test

import com.tschuchort.compiletesting.JvmCompilationResult

/**
 * Represents the results of a Java Virtual Machine (JVM) compilation process.
 * Provides a mechanism to assert the state or properties of the `KefirResults` object through block execution.
 *
 * @property result The compiled result associated with this `KefirResults` object.
 */
class KefirResults(
    val result: JvmCompilationResult
) {
    /**
     * Allows executing a block of assertions on a `KefirResults` instance.
     *
     * @param block The lambda that defines the assertions to execute within the context of `KefirResults`.
     */
    infix fun asserts(block: KefirResults.() -> Unit) {
        block()
    }
}