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

package io.github.kshulzh.kefir.transform.utils.type

import io.github.kshulzh.kefir.model.api.KtElement
import io.github.kshulzh.kefir.model.api.declaration.KtFunctionElement
import io.github.kshulzh.kefir.model.api.expression.KtBlockElement
import io.github.kshulzh.kefir.model.api.type.KtTypeElement

/**
 * Attempts to resolve the type of the current `KtElement` by inspecting its hierarchy.
 *
 * This function checks if the element is a `KtFunctionElement` and retrieves its type, if available.
 * For any other type of `KtElement`, it returns `null`.
 *
 * @return The resolved `KtTypeElement` if the element is a `KtFunctionElement` with a defined type,
 * or `null` otherwise.
 */
fun KtElement.resolveTypeUp(): KtTypeElement? {
    return when (this) {
        is KtFunctionElement -> type
        else -> null
    }
}

/**
 * Attempts to resolve and retrieve the type associated with the current block element.
 *
 * This method evaluates the `type` property of the block element. If the `type` is
 * already defined, it is returned directly. Otherwise, the method returns `null`.
 *
 * @return The resolved [KtTypeElement] representing the type of the block element, or `null` if the type is undefined.
 */
fun KtBlockElement.resolveTypeUp(): KtTypeElement? {
    return when (type) {
        null -> null
        else -> type
    }
}