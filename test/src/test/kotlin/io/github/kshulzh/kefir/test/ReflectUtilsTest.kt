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

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ReflectUtilsTest {

    class Sample(val id: Int?, val name: String) {
        fun greet(who: String) = "Hello, $who from $name"
        fun maybe(flag: Boolean): String? = if (flag) "OK" else null

        companion object {
            var counter: Int = 0
            fun tick() = ++counter
        }

        class Nested
    }

    @Test
    fun `construct supports positional and nullable args`() {
        val k = Sample::class
        val instance = k.construct(null, "Planet")
        assertNotNull(instance)
        assertEquals("Hello, World from Planet", instance.invoke("greet", "World"))
    }

    @Test
    fun `construct supports named args`() {
        val inst = Sample::class.construct(NamedParam("name", "Mars"), NamedParam("id", null))
        assertNotNull(inst)
        assertEquals("Hello, X from Mars", inst.invoke("greet", "X"))
    }

    @Test
    fun `invoke returns null for nullable function results`() {
        val k = Sample::class
        val instance = k.construct(1, "Earth")
        assertNotNull(instance)
        val res1 = instance.invoke("maybe", false)
        assertNull(res1)
        val res2 = instance.invoke("maybe", true)
        assertEquals("OK", res2)
    }

    @Test
    fun `get and set work on companion properties via KClass receiver`() {
        val k = Sample::class
        k["counter"] = 41
        assertEquals(41, k["counter"])
        k.invoke("tick")
        assertEquals(42, k["counter"])
    }

    @Test
    fun `Nested finds nested class by simple name`() {
        val nested = Sample::class.Nested("Nested")
        assertEquals("Nested", nested.simpleName)
    }
}
