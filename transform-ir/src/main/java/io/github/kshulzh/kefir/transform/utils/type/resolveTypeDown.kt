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

import io.github.kshulzh.kefir.model.api.expression.KtBlockElement
import io.github.kshulzh.kefir.model.api.expression.KtConstElement
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.statement.KtReturnStatementElement
import io.github.kshulzh.kefir.model.api.type.KtTypeElement

/**
 * Resolves the type of the current block element either from its direct `type` property
 * or by analyzing the return statements within its block.
 *
 * This method attempts to determine the type in the following order:
 * 1. If the block element has a non-null `type`, that type is returned.
 * 2. If the `type` is null, it iterates through the statements within the block.
 *    If a statement is a return statement, it recursively resolves the type of the
 *    expression associated with the return statement.
 * 3. The first resolved type from the return statements is assigned to the block element's
 *    `type` and returned. If no return statements have a resolvable type, null is returned.
 *
 * @return The resolved type of the block element as a [KtTypeElement], or `null` if the type cannot be determined.
 */
fun KtBlockElement.resolveTypeDown(): KtTypeElement? {
    return if (type != null) {
        type
    } else {
        val candidates = mutableListOf<KtTypeElement>()
        statements.forEach {
            if (it is KtReturnStatementElement) {
                it.expression?.resolveTypeDown()?.let { type -> candidates.add(type) }
            }
        }
        type = candidates.firstOrNull()
        type
    }
}

/**
 * Resolves the type of the current [KtExpressionElement] by traversing downward,
 * leveraging specific implementations for block and constant elements.
 *
 * For a [KtBlockElement], it uses its specialized `resolveTypeDown` logic to determine the type.
 * For a [KtConstElement], it directly retrieves the associated type property.
 * Otherwise, it returns `null` if the type cannot be resolved.
 *
 * @return The resolved [KtTypeElement] if a type can be determined, or `null` if no resolvable type exists.
 */
fun KtExpressionElement.resolveTypeDown(): KtTypeElement? {
    return when (this) {
        is KtBlockElement -> resolveTypeDown()
        is KtConstElement<*> -> this.type
        else -> null
    }
}