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

package io.github.kshulzh.kefir.test.declaration

import io.github.kshulzh.kefir.builder.declaration.*
import io.github.kshulzh.kefir.builder.expression.*
import io.github.kshulzh.kefir.builder.io.Class
import io.github.kshulzh.kefir.builder.io.File
import io.github.kshulzh.kefir.builder.io.Package
import io.github.kshulzh.kefir.builder.statement.Return
import io.github.kshulzh.kefir.builder.statement.St
import io.github.kshulzh.kefir.model.api.declaration.KtConstructorElement
import io.github.kshulzh.kefir.model.api.type.KtBaseTypes
import io.github.kshulzh.kefir.test.KefirCompilation
import io.github.kshulzh.kefir.test.Klass
import io.github.kshulzh.kefir.test.construct
import io.github.kshulzh.kefir.test.get
import kotlin.test.Test

class PropertyTest {
    @Test
    fun `should create top level function`() {
        KefirCompilation {
            kotlin(
                "G.kt", """
                class G() {
                    companion object {
                        fun a() = 0
                    }
                    var s:Int = 90
                    fun foo(string: String) {
                        val a = "ssss"
                        println(string)
                    }
                }
            """
            )
            val r = process {
                File("C.kt") {
                    Class("C") {
                        Constructor {
                            Body {
                                St(
                                    CallDelegate(
                                        it.external!!.Package("kotlin")
                                            .Class("Any").declarations.filterIsInstance<KtConstructorElement>()
                                            .first()
                                    )
                                )
                            }
                        }
                        Fun("foo", mutableListOf()) {
                            Body {
                                Return(Const("Hello World!"))
                            }
                        }
                        Fun("foo2") {
                            Body {
                                Return(Const("Hello World!"))
                            }
                        }
                        Var("b", KtBaseTypes.STRING) {
                            Getter { Return(Field) }
                            Setter {
                                Field Set Variable("value")
                            }
                        }

                    }
                    Class("B")
                }
            }
            r asserts {
                val s = Klass("G").construct()!!
                s["s"]
            }
        }
    }
}