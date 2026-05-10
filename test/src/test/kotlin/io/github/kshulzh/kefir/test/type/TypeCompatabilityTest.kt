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

package io.github.kshulzh.kefir.test.type

import io.github.kshulzh.kefir.builder.type.Type
import io.github.kshulzh.kefir.builder.type.isAssignableFrom
import io.github.kshulzh.kefir.model.api.KtPath
import io.github.kshulzh.kefir.model.declatation.KtClassElementImpl
import io.github.kshulzh.kefir.model.type.KtClassTypeElementImpl
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TypeCompatabilityTest {

    // --- Direct class type matching ---

    @Test
    fun `same type is assignable to itself`() {
        val stringType = Type("kotlin", "String")
        assertTrue(stringType.isAssignableFrom(stringType))
    }

    @Test
    fun `same class in same package is assignable`() {
        val a = Type("kotlin", "String")
        val b = Type("kotlin", "String")
        assertTrue(a.isAssignableFrom(b))
    }

    @Test
    fun `different class names are not assignable`() {
        val stringType = Type("kotlin", "String")
        val intType = Type("kotlin", "Int")
        assertFalse(stringType.isAssignableFrom(intType))
    }

    @Test
    fun `different packages are not assignable`() {
        val a = Type("kotlin", "String")
        val b = Type("java.lang", "String")
        assertFalse(a.isAssignableFrom(b))
    }

    @Test
    fun `empty package types are assignable when class matches`() {
        val a = Type("", "MyClass")
        val b = Type("", "MyClass")
        assertTrue(a.isAssignableFrom(b))
    }

    @Test
    fun `empty package does not match non-empty package`() {
        val a = Type("", "String")
        val b = Type("kotlin", "String")
        assertFalse(a.isAssignableFrom(b))
    }

    // --- Nullability in type creation (isAssignableFrom does not check nullability) ---

    @Test
    fun `nullable and non-nullable same type are structurally assignable`() {
        val nonNullable = Type("kotlin", "String", isNullable = false)
        val nullable = Type("kotlin", "String", isNullable = true)
        // isAssignableFrom checks package/class/typeArgs only, not nullability
        assertTrue(nonNullable.isAssignableFrom(nullable))
        assertTrue(nullable.isAssignableFrom(nonNullable))
    }

    // --- Generic type argument matching ---

    @Test
    fun `generic type is assignable when type argument matches`() {
        val listOfString = Type("kotlin.collections", "List", typeArguments = mutableListOf(Type("kotlin", "String")))
        val listOfString2 = Type("kotlin.collections", "List", typeArguments = mutableListOf(Type("kotlin", "String")))
        assertTrue(listOfString.isAssignableFrom(listOfString2))
    }

    @Test
    fun `generic type is not assignable when type argument differs`() {
        val listOfString = Type("kotlin.collections", "List", typeArguments = mutableListOf(Type("kotlin", "String")))
        val listOfInt = Type("kotlin.collections", "List", typeArguments = mutableListOf(Type("kotlin", "Int")))
        assertFalse(listOfString.isAssignableFrom(listOfInt))
    }

    @Test
    fun `map type is assignable when both type arguments match`() {
        val mapSI = Type(
            "kotlin.collections", "Map",
            typeArguments = mutableListOf(Type("kotlin", "String"), Type("kotlin", "Int"))
        )
        val mapSI2 = Type(
            "kotlin.collections", "Map",
            typeArguments = mutableListOf(Type("kotlin", "String"), Type("kotlin", "Int"))
        )
        assertTrue(mapSI.isAssignableFrom(mapSI2))
    }

    @Test
    fun `map type is not assignable when second type argument differs`() {
        val mapSI = Type(
            "kotlin.collections", "Map",
            typeArguments = mutableListOf(Type("kotlin", "String"), Type("kotlin", "Int"))
        )
        val mapSS = Type(
            "kotlin.collections", "Map",
            typeArguments = mutableListOf(Type("kotlin", "String"), Type("kotlin", "String"))
        )
        assertFalse(mapSI.isAssignableFrom(mapSS))
    }

    @Test
    fun `nested generic types are assignable when all match`() {
        val inner = Type("kotlin.collections", "List", typeArguments = mutableListOf(Type("kotlin", "Int")))
        val outer = Type("kotlin.collections", "List", typeArguments = mutableListOf(inner))
        val inner2 = Type("kotlin.collections", "List", typeArguments = mutableListOf(Type("kotlin", "Int")))
        val outer2 = Type("kotlin.collections", "List", typeArguments = mutableListOf(inner2))
        assertTrue(outer.isAssignableFrom(outer2))
    }

    @Test
    fun `nested generic types are not assignable when inner arg differs`() {
        val innerInt = Type("kotlin.collections", "List", typeArguments = mutableListOf(Type("kotlin", "Int")))
        val outerInt = Type("kotlin.collections", "List", typeArguments = mutableListOf(innerInt))
        val innerString = Type("kotlin.collections", "List", typeArguments = mutableListOf(Type("kotlin", "String")))
        val outerString = Type("kotlin.collections", "List", typeArguments = mutableListOf(innerString))
        assertFalse(outerInt.isAssignableFrom(outerString))
    }

    // --- Supertype hierarchy via KtClassElementImpl ---

    @Test
    fun `supertype is assignable from subtype via explicit supertype list`() {
        // class B extends A — modelled manually
        val aClass = KtClassElementImpl(name = "A")
        val bClass = KtClassElementImpl(name = "B")

        val aType = KtClassTypeElementImpl(KtPath(), KtPath("A"), false, mutableListOf(), aClass)
        val bType = KtClassTypeElementImpl(KtPath(), KtPath("B"), false, mutableListOf(), bClass)

        bClass.supertypes.add(aType)

        // A should be assignable from B (B is a subtype of A)
        assertTrue(aType.isAssignableFrom(bType))
    }

    @Test
    fun `type is not assignable from its supertype (supertype is not a subtype)`() {
        val aClass = KtClassElementImpl(name = "A")
        val bClass = KtClassElementImpl(name = "B")

        val aType = KtClassTypeElementImpl(KtPath(), KtPath("A"), false, mutableListOf(), aClass)
        val bType = KtClassTypeElementImpl(KtPath(), KtPath("B"), false, mutableListOf(), bClass)

        bClass.supertypes.add(aType)

        // B should NOT be assignable from A (A is not a subtype of B)
        assertFalse(bType.isAssignableFrom(aType))
    }

    @Test
    fun `transitive supertype is assignable from deep subtype`() {
        // C extends B extends A
        val aClass = KtClassElementImpl(name = "A")
        val bClass = KtClassElementImpl(name = "B")
        val cClass = KtClassElementImpl(name = "C")

        val aType = KtClassTypeElementImpl(KtPath(), KtPath("A"), false, mutableListOf(), aClass)
        val bType = KtClassTypeElementImpl(KtPath(), KtPath("B"), false, mutableListOf(), bClass)
        val cType = KtClassTypeElementImpl(KtPath(), KtPath("C"), false, mutableListOf(), cClass)

        bClass.supertypes.add(aType)
        cClass.supertypes.add(bType)

        // A should be assignable from C transitively
        assertTrue(aType.isAssignableFrom(cType))
        // B should be assignable from C
        assertTrue(bType.isAssignableFrom(cType))
        // A should NOT be assignable from B's parent (A is not a subtype of C)
        assertFalse(cType.isAssignableFrom(aType))
    }

    @Test
    fun `unrelated types are not assignable`() {
        val xClass = KtClassElementImpl(name = "X")
        val yClass = KtClassElementImpl(name = "Y")

        val xType = KtClassTypeElementImpl(KtPath(), KtPath("X"), false, mutableListOf(), xClass)
        val yType = KtClassTypeElementImpl(KtPath(), KtPath("Y"), false, mutableListOf(), yClass)

        assertFalse(xType.isAssignableFrom(yType))
        assertFalse(yType.isAssignableFrom(xType))
    }

    @Test
    fun `type with no klass reference falls back to structural check only`() {
        // Without klass, supertype check cannot be done, structural mismatch returns false
        val a = Type("", "A")
        val b = Type("", "B")
        assertFalse(a.isAssignableFrom(b))
    }
}
