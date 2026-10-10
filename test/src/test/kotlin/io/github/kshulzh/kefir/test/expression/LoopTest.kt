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

package io.github.kshulzh.kefir.test.expression

import io.github.kshulzh.kefir.builder.arg.As
import io.github.kshulzh.kefir.builder.declaration.Body
import io.github.kshulzh.kefir.builder.declaration.Fun
import io.github.kshulzh.kefir.builder.expression.Const
import io.github.kshulzh.kefir.builder.expression.DoWhile
import io.github.kshulzh.kefir.builder.expression.False
import io.github.kshulzh.kefir.builder.expression.While
import io.github.kshulzh.kefir.builder.io.Class
import io.github.kshulzh.kefir.builder.io.File
import io.github.kshulzh.kefir.builder.statement.Return
import io.github.kshulzh.kefir.builder.statement.St
import io.github.kshulzh.kefir.model.api.expression.KtDoWhileElement
import io.github.kshulzh.kefir.model.api.expression.KtWhileElement
import io.github.kshulzh.kefir.model.api.type.KtBaseTypes
import io.github.kshulzh.kefir.model.api.utils.KtChildVisitor
import io.github.kshulzh.kefir.test.KefirCompilation
import io.github.kshulzh.kefir.test.Klass
import io.github.kshulzh.kefir.test.construct
import io.github.kshulzh.kefir.test.invoke
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertSame

class LoopTest {
    private val source = """
        class B {
            fun foo(string: String) {
                val a = "ssss"
            }
        }
    """

    @Test
    fun `while with false condition never runs its body`() {
        KefirCompilation { kotlin("B.kt", source) } process {
            File("B.kt") {
                Class("B") {
                    Fun("foo1", mutableListOf("string" As KtBaseTypes.STRING)) {
                        Body {
                            St(While(False()) { Return(Const("body")) })
                            Return(Const("after"))
                        }
                    }
                }
            }
        } asserts {
            assertEquals("after", Klass("B").construct()!!("foo1", "x"))
        }
    }

    @Test
    fun `do while with false condition runs its body once`() {
        KefirCompilation { kotlin("B.kt", source) } process {
            File("B.kt") {
                Class("B") {
                    Fun("foo1", mutableListOf("string" As KtBaseTypes.STRING)) {
                        Body {
                            St(DoWhile(False()) { Return(Const("body")) })
                            Return(Const("after"))
                        }
                    }
                }
            }
        } asserts {
            assertEquals("body", Klass("B").construct()!!("foo1", "x"))
        }
    }

    @Test
    fun `labeled loops compile`() {
        KefirCompilation { kotlin("B.kt", source) } process {
            File("B.kt") {
                Class("B") {
                    Fun("foo1", mutableListOf("string" As KtBaseTypes.STRING)) {
                        Body {
                            St(While(False(), label = "outer") { })
                            St(DoWhile(False(), label = "inner") { })
                            Return(Const("done"))
                        }
                    }
                }
            }
        } asserts {
            assertEquals("done", Klass("B").construct()!!("foo1", "x"))
        }
    }

    @Test
    fun `dsl sets label type and parents`() {
        val cond = False()
        val w = While(cond, label = "l") { }
        assertEquals("l", w.label)
        assertEquals(KtBaseTypes.UNIT, w.type)
        assertSame(w, cond.parent)
        assertNotNull(w.body)
        assertSame(w, w.body!!.parent)
    }

    @Test
    fun `visitor dispatches to while and do while`() {
        val visited = mutableListOf<String>()
        val visitor = object : KtChildVisitor<Unit> {
            override fun visitWhile(element: KtWhileElement, data: Unit) {
                visited += "while"
                super.visitWhile(element, data)
            }

            override fun visitDoWhile(element: KtDoWhileElement, data: Unit) {
                visited += "doWhile"
                super.visitDoWhile(element, data)
            }
        }
        While(False()) { }.accept(visitor, Unit)
        DoWhile(False()) { }.accept(visitor, Unit)
        assertEquals(listOf("while", "doWhile"), visited)
    }
}
