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

package io.github.kshulzh.kefir.test.annotation

import io.github.kshulzh.kefir.builder.expression.Const
import io.github.kshulzh.kefir.builder.io.Class
import io.github.kshulzh.kefir.builder.io.File
import io.github.kshulzh.kefir.builder.statement.Return
import io.github.kshulzh.kefir.builder.type.Type
import io.github.kshulzh.kefir.model.api.declaration.KtFunctionElement
import io.github.kshulzh.kefir.model.api.expression.KtBlockElement
import io.github.kshulzh.kefir.model.utils.findAnnotated
import io.github.kshulzh.kefir.test.KefirCompilation
import io.github.kshulzh.kefir.test.Klass
import io.github.kshulzh.kefir.test.construct
import io.github.kshulzh.kefir.test.get
import io.github.kshulzh.kefir.test.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class SearchAnnotationTest {
    @Test
    fun `should do `() {
        KefirCompilation {
            kotlin(
                "B.kt", """
                annotation class AA
                interface A <T, V>
                interface B <V> : A<Int, V> {}
                class C : B<String> {
                    @AA
                    fun foo(string: String) : String {
                        val a = "fff"
                        return "string"
                    }
                }
                fun a(@AA c: A<Int, String>, b : B<String>) : String { 
                return "String"}
                fun a(c: A<String, String>) : Boolean { return true }
                fun a(c: C) : Boolean { return true }
            
            """.trimIndent()
            )
        } process {
            val s = findAnnotated(Type("", "AA"), AnnotationTarget.FUNCTION)
            s.forEach {
                if (it is KtFunctionElement) {
                    val body = it.body
                    if (body is KtBlockElement) {
                        body.statements.removeLast()
                        body.Return(Const("Hello World!"))
                    }
                }
            }
        } asserts {
            val klass = Klass("C")
            val c = klass.construct()!!
            val res = c("foo", "aaaa")
            assertEquals("Hello World!", res)

        }
    }
}