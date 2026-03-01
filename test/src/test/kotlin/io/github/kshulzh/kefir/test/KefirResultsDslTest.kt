/*
 * Copyright (c) 2026. Kirill Shulzhenko
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

import com.tschuchort.compiletesting.KotlinCompilation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class KefirResultsDslTest {

    @Test
    fun `asserts infix executes in KefirResults scope`() {
        var executed = false
        KefirCompilation {
            kotlin("Empty.kt", "class Empty")
        } process {} asserts {
            executed = true
            assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)
        }
        assertTrue(executed, "asserts block should be executed")
    }
}
