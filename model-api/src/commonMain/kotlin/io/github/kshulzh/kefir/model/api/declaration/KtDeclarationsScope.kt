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

package io.github.kshulzh.kefir.model.api.declaration

import io.github.kshulzh.kefir.model.api.KtElement
import io.github.kshulzh.kefir.model.api.utils.KtVisitor
import kotlin.reflect.KClass

/**
 * Represents a scope for Kotlin declarations.
 *
 * This interface defines a container for managing Kotlin declaration elements.
 * It provides a collection of declarations that belong to this scope and a function
 * to search for specific declarations based on their name and type.
 */
interface KtDeclarationsScope : KtElement {
    /**
     * Represents a mutable set of declaration elements accessible within the current scope.
     *
     * This property stores instances of [KtDeclarationElement], which may include
     * various types of Kotlin declarations such as classes, fields, properties, and functions.
     * It serves as a collection of all declarations defined or referenced in the current
     * [KtDeclarationsScope].
     *
     * The set is mutable, allowing for insertion, removal, and real-time management of
     * declaration elements within the corresponding scope.
     */
    val declarations: MutableSet<KtDeclarationElement>

    /**
     * Finds all declarations within the current scope that match the specified name and belong to the specified class type.
     *
     * @param name The name of the declarations to search for.
     * @param klass The class type of the declarations to filter for.
     * @return A list of declarations matching the specified name and class type.
     */
    fun <T : KtDeclarationElement> findDeclarations(name: String, klass: KClass<T>): List<T> {
        return declarations.filter { it.name == name }.filterIsInstance(klass.java)
    }

    override fun <R, D> accept(visitor: KtVisitor<R, D>, data: D): R = visitor.visitDeclarationsScope(this, data)
}

/**
 * Finds declarations of the specified type `T` within the given `KtDeclarationsScope` by their name.
 *
 * This method provides a way to search for declarations that match the provided name
 * and are of a specific type. It uses the reified generic type to infer the target
 * declaration type at call-time.
 *
 * @param name The name of the declarations to search for.
 * @return A list of declarations of type `T` that match the specified name. If no matching declarations
 *         are found, the method returns an empty list.
 */
inline fun <reified T : KtDeclarationElement> KtDeclarationsScope.findDeclaration(name: String): List<T> {
    return findDeclarations(name, T::class)
}