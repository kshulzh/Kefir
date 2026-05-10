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

package io.github.kshulzh.kefir.transform.utils.type

import io.github.kshulzh.kefir.model.api.arg.KtParameterElement
import io.github.kshulzh.kefir.model.api.declaration.KtFieldElement
import io.github.kshulzh.kefir.model.api.declaration.KtFunctionElement
import io.github.kshulzh.kefir.model.api.declaration.KtPropertyElement
import io.github.kshulzh.kefir.model.api.expression.KtGetFieldElement
import io.github.kshulzh.kefir.model.api.expression.KtSetFieldElement
import io.github.kshulzh.kefir.model.api.type.KtTypeElement

/**
 * Resolves the return type of the current `KtFunctionElement`.
 *
 * This method attempts to determine the type information associated with the function.
 * If the type has already been set, it is returned directly. If the type is absent,
 * it attempts to resolve the type from the function's body using a downward type resolution mechanism.
 *
 * @return The resolved `KtTypeElement` representing the function's return type, or `null` if it cannot be determined.
 */
fun KtFunctionElement.resolveType(): KtTypeElement? {
    return if (type != null) {
        type
    } else {
        type = body?.resolveTypeDown()
        type
    }
}

/**
 * Resolves the type of a parameter element within the Kotlin model.
 *
 * This function attempts to retrieve the type of the parameter by returning the existing type
 * if it is not null. If the type is null, it resolves the type from the associated value expression
 * by invoking the `resolveTypeDown` function.
 *
 * @return The resolved [KtTypeElement] representing the parameter's type, or null if the type cannot be resolved.
 */
fun KtParameterElement.resolveType(): KtTypeElement? {
    return if (type != null) {
        type
    } else {
        type = value?.resolveTypeDown()
        type
    }
}

/**
 * Resolves and retrieves the type of the field associated with the `KtFieldElement`.
 *
 * If the `type` property of the field is already initialized, it is returned directly.
 * Otherwise, the type is resolved by evaluating the associated `value` (if available)
 * using `resolveTypeDown` and then assigning the resolved type to the `type` property.
 *
 * @return The resolved type of the field as a `KtTypeElement`, or `null` if the type
 *         cannot be determined.
 */
fun KtFieldElement.resolveType(): KtTypeElement? {
    return if (type != null) {
        type
    } else {
        type = value?.resolveTypeDown()
        type
    }
}

/**
 * Resolves the type of the property element.
 *
 * This method dynamically determines the type of this property element based on
 * its associated components, such as getter, setter, or backing field. If a valid
 * type is already set, it will directly return that type. Otherwise, concrete type
 * resolution logic can be further implemented to infer the type from associated components.
 *
 * @return The resolved type of the property as a [KtTypeElement], or `null` if the type
 * cannot be determined.
 */
fun KtPropertyElement.resolveType(): KtTypeElement {
    //todo make property::type nullable and resolve dynamicaly using
//    val candidates = mutableListOf<KtTypeElement>()
//    getter?.resolveType()?.also {
//        candidates.add(it)
//    }
//    setter?.resolveType()?.also {
//        candidates.add(it)
//    }
//    field?.resolveType()?.also {
//        candidates.add(it)
//    }
//    type = candidates.firstOrNull()!!
//    type
    return type
}

/**
 * Resolves the type for this `KtGetFieldElement`.
 *
 * This function retrieves the type of the `KtGetFieldElement` by first checking
 * if the `type` property is non-null. If the `type` is already resolved, it
 * returns the cached value. Otherwise, it invokes `resolveType` on the associated
 * `field` to determine the type and caches the result for subsequent calls.
 *
 * @return The resolved `KtTypeElement` representing the type of this field element,
 *         or `null` if the type cannot be determined.
 */
fun KtGetFieldElement.resolveType(): KtTypeElement? {
    return if (type != null) {
        type
    } else {
        type = field.resolveType()
        type
    }
}

/**
 * Resolves and retrieves the type of the current `KtSetFieldElement` instance.
 *
 * This method attempts to determine the type associated with the field assignment represented by
 * the `KtSetFieldElement`. If the type is already cached, it returns the cached value. Otherwise,
 * it computes the type by resolving the type of the associated field and updates the cached type.
 *
 * @return The resolved `KtTypeElement` representing the type of the field, or `null` if the type resolution fails.
 */
fun KtSetFieldElement.resolveType(): KtTypeElement? {
    return if (type != null) {
        type
    } else {
        type = field.resolveType()
        type
    }
}