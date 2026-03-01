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

package io.github.kshulzh.kefir.test.declaration

import io.github.kshulzh.kefir.builder.arg.As
import io.github.kshulzh.kefir.builder.declaration.Body
import io.github.kshulzh.kefir.builder.declaration.Class
import io.github.kshulzh.kefir.builder.declaration.Fun
import io.github.kshulzh.kefir.builder.expression.Const
import io.github.kshulzh.kefir.builder.io.File
import io.github.kshulzh.kefir.builder.statement.Return
import io.github.kshulzh.kefir.model.api.expression.KtBlockElement
import io.github.kshulzh.kefir.model.api.type.KtBaseTypes
import io.github.kshulzh.kefir.test.KefirCompilation
import io.github.kshulzh.kefir.test.Klass
import io.github.kshulzh.kefir.test.construct
import io.github.kshulzh.kefir.test.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class FunctionTest {
    @Test
    fun `should create top level function`() {
        KefirCompilation {
            kotlin(
                "B.kt", """
                class B {
                    fun foo(string: String) {
                        val a = "ssss"
                    }
                }
            """
            )
        } process {
            File("A.kt") {
                Fun(
                    "foo1", mutableListOf(
                        "string" As KtBaseTypes.STRING
                    )
                ) {
                    Body {
                        Return(Const("Hello World!"))
                    }
                }

            }
        } asserts {
            //Klass("A")("foo1","123")
        }
    }

    @Test
    fun `should create inner function`() {
        KefirCompilation {
            kotlin(
                "B.kt", """
                class B {
                    fun foo(string: String) {
                        val a = "ssss"
                    }
                }
            """
            )
        } process {
            File("B.kt") {
                Class("B") {
                    Fun(
                        "foo1", mutableListOf(
                            "string" As KtBaseTypes.STRING
                        )
                    ) {
                        Body {
                            Return(Const("Hello World!"))
                        }
                    }
                }

            }
        } asserts {
            assertEquals("Hello World!", Klass("B").construct()!!("foo1", "sss"))
        }
    }

    @Test
    fun `should call external function`() {
        KefirCompilation {
            kotlin(
                "B.kt", """
                class B {
                    fun foo(string: String) : String? {
                        return null
                    }
                }
            """
            )
        } process {
            File("B.kt") {
                Class("B") {
                    Fun(
                        "foo", mutableListOf(
                            "string" As KtBaseTypes.STRING
                        )
                    ) {
                        body
                        body = Const("Hello World!123")
                        body
                        body = Const("Hello World!")
                    }
                }

            }
        } asserts {
            val klass = Klass("B")
            assertEquals("Hello World!", klass.construct()!!.invoke("foo", "sss"))
        }
    }

    @Test
    fun `should change function`() {
        KefirCompilation {
            kotlin(
                "B.kt", """
                class B {
                    fun foo(string: String) : String? {
                        println("123")
                        return null
                    }
                }
            """
            )
        } process {
            File("B.kt") {
                Class("B") {
                    Fun(
                        "foo", mutableListOf(
                            "string" As KtBaseTypes.STRING
                        )
                    ) {
                        val b = body as? KtBlockElement
                        b?.statements?.removeLast()
                        b?.Return(Const("Hello World!11"))
                        body = Const("Hello World!1")
                        println()
                    }
                }

            }
        } asserts {
            val klass = Klass("B")
            assertEquals("Hello World!1", klass.construct()!!.invoke("foo", "sss"))
        }
    }
}