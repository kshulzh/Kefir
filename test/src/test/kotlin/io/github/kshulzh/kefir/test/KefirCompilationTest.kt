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
import kotlin.test.assertNotNull

class KefirCompilationTest {

    @Test
    fun `kotlin sources compile and can be reflected`() {
        KefirCompilation {
            kotlin(
                "Foo.kt",
                """
                class Foo(val p: String) {
                    fun greet(name: String) = "Hello, " + name + ", from " + p
                    class Bar
                    companion object {
                        var x: Int = 0
                        fun singleton() = "SINGLETON"
                    }
                }
                """
            )
        } process {} asserts {
            assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)

            val fooKlass = Klass("Foo")
            assertEquals("Foo", fooKlass.simpleName)

            val instance = fooKlass.construct("planet")
            assertNotNull(instance)

            // call instance method
            val greet = instance.invoke("greet", "World")
            assertEquals("Hello, World, from planet", greet)

            // nested class lookup
            val barKlass = fooKlass.Nested("Bar")
            assertEquals("Bar", barKlass.simpleName)

            // companion property get/set
            fooKlass["x"] = 42
            val xVal = fooKlass["x"]
            assertEquals(42, xVal)

            // companion function invoke via KClass receiver
            val singleton = fooKlass.invoke("singleton")
            assertEquals("SINGLETON", singleton)
        }
    }

    @Test
    fun `java sources are included and accessible from kotlin`() {
        KefirCompilation {
            java(
                "A.java",
                """
                public class A { public String say() { return "hi"; } }
                """
            )
            kotlin(
                "UseA.kt",
                """
                class UseA { fun call() = A().say() }
                """
            )
        } process {} asserts {
            assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)
            val useA = Klass("UseA").construct()!!
            assertEquals("hi", useA.invoke("call"))
        }
    }
}
