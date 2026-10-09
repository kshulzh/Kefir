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

package io.github.kshulzh.kefir.transform.utils.target

import io.github.kshulzh.kefir.model.api.KtElement
import io.github.kshulzh.kefir.model.api.declaration.KtFunctionElement
import io.github.kshulzh.kefir.model.api.expression.KtBlockElement
import io.github.kshulzh.kefir.model.api.statement.KtReturnStatementElement

/**
 * Resolves the target element associated with this Kotlin element.
 *
 * Based on the type of the current element, this method attempts to resolve
 * the specific target element:
 * - If the element is of type `KtFunctionElement`, it directly represents the target.
 * - If the element is a `KtReturnStatementElement`, the target is resolved recursively.
 * - If the element is a `KtBlockElement`, the target is resolved further up the parent chain.
 * - For other types of elements, the target cannot be resolved and the method returns `null`.
 *
 * @return The resolved target as a `KtElement` if it exists, or `null` if the target cannot be resolved.
 */
fun KtElement.resolveTarget(): KtElement? {
    return when (this) {
        is KtFunctionElement -> this
        is KtReturnStatementElement -> resolveTarget()
        is KtBlockElement -> resolveTarget()
        else -> null
    }
}

/**
 * Resolves the target element associated with this return statement.
 *
 * If the `target` property is not null, it is returned. Otherwise, the method recursively
 * resolves the target through the enclosing statements scope, if available.
 *
 * @return The resolved target element as a [KtElement], or `null` if no target can be resolved.
 */
fun KtReturnStatementElement.resolveTarget(): KtElement? {
    return if (target != null) {
        target
    } else {
        parent?.resolveTarget()
    }
}

/**
 * Resolves the target [KtElement] associated with the current [KtBlockElement].
 *
 * This method traverses the parent hierarchy of the current [KtBlockElement],
 * delegating the resolution to the parent's `resolveTarget` method until a concrete
 * target element is resolved or the hierarchy is exhausted. If no target can be
 * resolved, it returns `null`.
 *
 * @return The resolved [KtElement] if a valid target is found in the hierarchy, or `null` otherwise.
 */
fun KtBlockElement.resolveTarget(): KtElement? {
    return parent?.resolveTarget()
}

/**
 * Resolves the target element for this function element.
 *
 * This method retrieves the current function element (`KtFunctionElement`) as its resolved target,
 * representing the function in its current context within the Kotlin model structure.
 *
 * @return The resolved `KtElement` corresponding to this function element, or `null` if the resolution fails.
 */
fun KtFunctionElement.resolveTarget(): KtElement {
    return this
}