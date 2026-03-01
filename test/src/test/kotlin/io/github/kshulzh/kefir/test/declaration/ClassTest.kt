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
import io.github.kshulzh.kefir.builder.declaration.Constructor
import io.github.kshulzh.kefir.builder.declaration.Fun
import io.github.kshulzh.kefir.builder.expression.CallDelegate
import io.github.kshulzh.kefir.builder.expression.Const
import io.github.kshulzh.kefir.builder.expression.Null
import io.github.kshulzh.kefir.builder.io.Class
import io.github.kshulzh.kefir.builder.io.File
import io.github.kshulzh.kefir.builder.io.Package
import io.github.kshulzh.kefir.builder.statement.Return
import io.github.kshulzh.kefir.builder.statement.St
import io.github.kshulzh.kefir.builder.type.Type
import io.github.kshulzh.kefir.model.api.declatation.KtConstructorElement
import io.github.kshulzh.kefir.model.api.type.KtBaseTypes
import io.github.kshulzh.kefir.test.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ClassTest {
    @Test
    fun `should create top level class`() {
        KefirCompilation {
            kotlin(
                "B.kt", """
                open class A1
                open class A2 : A1()
                open class A3(val a:Int) : A2()
                class B {
                    fun ii() {
                        Any()
                        foo(123)
                    }
                    fun foo(int:Int) {
                        val a = "ssss"
                    }
                    fun foo(string: String) : String?{
                        return null
                    }
                }
            """
            )
        } process {
            File("A.kt") {
                Fun("B") {
                    type = Type("", "A", true)
                    Body {
                        Return(Null(type!!))
                    }
                }
                Class("A") {
                    Fun(
                        "foo", mutableListOf(
                            "string" As KtBaseTypes.STRING
                        )
                    ) {
                        Body {
                            Return(Const("Hello World!"))
                        }
                    }
                    Constructor {
                        Body {
                            St(
                                CallDelegate(
                                    it.external!!.Package("kotlin")
                                        .Class("Any")!!.declarations.filterIsInstance<KtConstructorElement>().first()
                                )
                            )
                        }
                    }
                }
            }
        } asserts {
//            val topLevel = Klass("AKt")
//            assertEquals(null, topLevel("B"))

            val klass = Klass("A")
            val a = klass.construct()
            assertEquals("Hello World!", a!!("foo", "sss"))
        }
    }

    @Test
    fun `should create inner classes`() {
        KefirCompilation {
            kotlin(
                "B.kt", """
                class B {
                    fun foo(string: String) {
                        val a = "fff"
                    }
                }
            """
            )

        } process {
            File("B.kt") {
                Class("B") {
                    Class("A") {
                        Class("C")
                    }
                }
            }
        } asserts {
            Klass("B")
                .Nested("A")
                .Nested("C")
        }
    }

}