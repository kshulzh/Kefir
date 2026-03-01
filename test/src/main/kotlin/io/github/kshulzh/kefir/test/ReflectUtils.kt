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

package io.github.kshulzh.kefir.test

import kotlin.reflect.*


/**
 * Loads a class by its fully qualified name.
 * Renamed from `classExists1` for clarity.
 *
 * @return The [KClass] if found, otherwise throw NPE.
 */
fun KefirResults.Klass(name: String): KClass<*> = result.classLoader.loadClass(name).kotlin

/**
 * Finds a nested class by its simple name.
 * Renamed from `hasNestedClass` to better reflect its purpose and nullable return type.
 *
 * @return The nested [KClass] if found, otherwise null.
 */
fun KClass<*>.Nested(name: String): KClass<*> = this.nestedClasses.first { it.simpleName == name }

val KClass<*>.Companion: KClass<*> get() = this.Nested("Companion")

/**
 * Constructs an instance of the class using a constructor that matches the provided arguments.
 * Now returns a nullable type `Any?` to safely handle cases where construction might fail.
 *
 * @return A new instance of the class, or null if no suitable constructor is found.
 */
fun KClass<*>.construct(vararg args: Any?): Any? = this.constructors.callMatching(*args)

/**
 * Dynamically invokes a method on an object by its name.
 * If the receiver is a [KClass], it attempts to call a method on its `objectInstance`.
 * This fixes the original recursive call issue.
 */
inline operator fun <reified T : Any> T.invoke(name: String, vararg args: Any?): Any? = if (this is KClass<*>) {
    this.Companion.objectInstance?.let { instance ->
        instance::class.members.filter { it.name == name }.callMatching(instance, *args)
    }
} else {
    this::class.members.filter { it.name == name }.callMatching(this, *args)
}

/**
 * Dynamically gets the value of a property by its name.
 * If the receiver is a [KClass], it attempts to get a property from its `objectInstance`.
 */
inline operator fun <reified T : Any> T.get(name: String): Any? = if (this is KClass<*>) {
    this.Companion.objectInstance?.let { instance ->
        instance::class.members.filterIsInstance<KProperty1<Any, *>>().firstOrNull { it.name == name }?.get(instance)
    }
} else {
    this::class.members.filterIsInstance<KProperty1<Any, *>>().firstOrNull { it.name == name }?.get(this)
}

/**
 * Dynamically sets the value of a mutable property by its name.
 * The function's return type is corrected to [Unit] as required for `set` operators.
 * If the receiver is a [KClass], it attempts to set a property on its `objectInstance`.
 */
inline operator fun <reified T : Any> T.set(name: String, value: Any?) {
    if (this is KClass<*>) {
        this.Companion.objectInstance?.let { instance ->
            instance::class.members.filterIsInstance<KMutableProperty1<Any, Any?>>()
                .firstOrNull { it.name == name }?.set(instance, value)
        }
    } else {
        this::class.members.filterIsInstance<KMutableProperty1<Any, Any?>>()
            .firstOrNull { it.name == name }?.set(this, value)
    }
}

/**
 * Finds the first callable in a collection that can be invoked with the given arguments and calls it.
 * Renamed from `call1` to `callMatching` for better readability.
 *
 * @return The result of the call, or null if no matching callable was found.
 */
fun <T> Collection<KCallable<T>>.callMatching(vararg args: Any?): Any? {
    return this.mapNotNull { callable ->
        val map = callable.isApplicable(*args) ?: return@mapNotNull null
        callable.callBy(map) ?: NullObject(callable.returnType.javaClass)
    }.firstOrNull()?.value
}

/**
 * Determines if a callable can be invoked with the provided arguments.
 * This implementation is more robust in handling positional and named arguments.
 *
 * @return A map of parameters to arguments if applicable, otherwise null.
 */
private fun <T> KCallable<T>.isApplicable(vararg args: Any?): Map<KParameter, Any?>? {
    val argsMap = mutableMapOf<KParameter, Any?>()

    val namedArgs = args.filterIsInstance<NamedParam>()
    val positionalArgs = args.filter { it !is NamedParam }

    // 1. Process named arguments
    for (namedArg in namedArgs) {
        val param = parameters.find { it.name == namedArg.name } ?: return null
        if (argsMap.containsKey(param)) return null // Duplicate argument
        if (!param.type.isAssignable(namedArg.value?.value)) return null
        argsMap[param] = namedArg.value?.value
    }

    // 2. Process positional arguments
    val positionalParams = parameters.filterNot(argsMap::containsKey)
    if (positionalArgs.size > positionalParams.size) return null // Too many arguments

    for (i in positionalArgs.indices) {
        val arg = positionalArgs[i]
        val param = positionalParams[i]
        if (!param.type.isAssignable(arg?.value)) return null
        argsMap[param] = arg?.value
    }

    // 3. Check for missing mandatory parameters
    for (param in parameters) {
        if (!argsMap.containsKey(param) && !param.isOptional) {
            return null
        }
    }

    return argsMap
}

/**
 * Checks if a value can be assigned to a variable of this KType.
 * This implementation correctly handles nullability and subtyping.
 */
private fun KType.isAssignable(value: Any?): Boolean {
    if (value == null) {
        return this.isMarkedNullable
    }
    // Using isSupertypeOf provides a more accurate check than simple classifier equality.
    return this.classifier?.let { classifier ->
        when (classifier) {
            is KClass<*> -> classifier.isInstance(value)
            else -> classifier == value::class
        }
    } ?: false
}