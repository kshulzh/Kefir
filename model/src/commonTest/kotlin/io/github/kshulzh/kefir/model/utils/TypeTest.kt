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

package io.github.kshulzh.kefir.model.utils

import io.github.kshulzh.kefir.model.api.KtPath
import io.github.kshulzh.kefir.model.type.KtClassTypeElementImpl
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TypeTest {

    @Test
    fun `should parse simple type without package`() {
        val result = typeOf("String")
        assertTrue(result is KtClassTypeElementImpl)
        assertEquals(KtPath(mutableListOf()), result.ktPackage)
        assertEquals(KtPath(mutableListOf("String")), result.ktClass)
        assertEquals(false, result.isNullable)
        assertEquals(emptyList(), result.typeArguments)
    }

    @Test
    fun `should parse simple type with package`() {
        val result = typeOf("kotlin.String")
        assertTrue(result is KtClassTypeElementImpl)
        assertEquals(KtPath(mutableListOf("kotlin")), result.ktPackage)
        assertEquals(KtPath(mutableListOf("String")), result.ktClass)
        assertEquals(false, result.isNullable)
        assertEquals(emptyList(), result.typeArguments)
    }

    @Test
    fun `should parse nullable type`() {
        val result = typeOf("String?")
        assertTrue(result is KtClassTypeElementImpl)
        assertEquals(KtPath(mutableListOf()), result.ktPackage)
        assertEquals(KtPath(mutableListOf("String")), result.ktClass)
        assertEquals(true, result.isNullable)
        assertEquals(emptyList(), result.typeArguments)
    }

    @Test
    fun `should parse generic type`() {
        val result = typeOf("List<String>")
        assertTrue(result is KtClassTypeElementImpl)
        assertEquals(KtPath(mutableListOf()), result.ktPackage)
        assertEquals(KtPath(mutableListOf("List")), result.ktClass)
        assertEquals(false, result.isNullable)
        assertEquals(1, result.typeArguments.size)
        val innerType = result.typeArguments.first()
        assertTrue(innerType is KtClassTypeElementImpl)
        assertEquals(KtPath(mutableListOf()), innerType.ktPackage)
        assertEquals(KtPath(mutableListOf("String")), innerType.ktClass)
        assertEquals(false, innerType.isNullable)
    }

    @Test
    fun `should parse generic type with multiple arguments`() {
        val result = typeOf("Map<String,Int>")
        assertTrue(result is KtClassTypeElementImpl)
        assertEquals(KtPath(mutableListOf()), result.ktPackage)
        assertEquals(KtPath(mutableListOf("Map")), result.ktClass)
        assertEquals(false, result.isNullable)
        assertEquals(2, result.typeArguments.size)

        val firstArg = result.typeArguments[0]
        assertTrue(firstArg is KtClassTypeElementImpl)
        assertEquals(KtPath(mutableListOf()), firstArg.ktPackage)
        assertEquals(KtPath(mutableListOf("String")), firstArg.ktClass)

        val secondArg = result.typeArguments[1]
        assertTrue(secondArg is KtClassTypeElementImpl)
        assertEquals(KtPath(mutableListOf()), secondArg.ktPackage)
        assertEquals(KtPath(mutableListOf("Int")), secondArg.ktClass)
    }

    @Test
    fun `should parse nested generic types`() {
        val result = typeOf("Map<String,List<Int>>")
        assertTrue(result is KtClassTypeElementImpl)
        assertEquals(KtPath(mutableListOf()), result.ktPackage)
        assertEquals(KtPath(mutableListOf("Map")), result.ktClass)
        assertEquals(false, result.isNullable)
        assertEquals(2, result.typeArguments.size)

        val secondArg = result.typeArguments[1]
        assertTrue(secondArg is KtClassTypeElementImpl)
        assertEquals(KtPath(mutableListOf()), secondArg.ktPackage)
        assertEquals(KtPath(mutableListOf("List")), secondArg.ktClass)
        assertEquals(1, secondArg.typeArguments.size)

        val nestedArg = secondArg.typeArguments[0]
        assertTrue(nestedArg is KtClassTypeElementImpl)
        assertEquals(KtPath(mutableListOf()), nestedArg.ktPackage)
        assertEquals(KtPath(mutableListOf("Int")), nestedArg.ktClass)
    }

    @Test
    fun `should parse type with package and generic arguments`() {
        val result = typeOf("kotlin.collections.Map<String,kotlin.Int>")
        assertTrue(result is KtClassTypeElementImpl)
        assertEquals(KtPath(mutableListOf("kotlin", "collections")), result.ktPackage)
        assertEquals(KtPath(mutableListOf("Map")), result.ktClass)
        assertEquals(false, result.isNullable)
        assertEquals(2, result.typeArguments.size)

        val firstArg = result.typeArguments[0]
        assertTrue(firstArg is KtClassTypeElementImpl)
        assertEquals(KtPath(mutableListOf()), firstArg.ktPackage)
        assertEquals(KtPath(mutableListOf("String")), firstArg.ktClass)

        val secondArg = result.typeArguments[1]
        assertTrue(secondArg is KtClassTypeElementImpl)
        assertEquals(KtPath(mutableListOf("kotlin")), secondArg.ktPackage)
        assertEquals(KtPath(mutableListOf("Int")), secondArg.ktClass)
    }
}