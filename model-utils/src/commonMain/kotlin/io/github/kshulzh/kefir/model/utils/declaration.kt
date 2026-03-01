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

import io.github.kshulzh.kefir.model.api.declatation.KtClassElement
import io.github.kshulzh.kefir.model.api.declatation.KtDeclarationElement

/**
 * Retrieves all declarations within the current class element, including those
 * nested within any inner classes or nested classes.
 *
 * This method flattens the structure of declarations by recursively traversing
 * any [KtClassElement] instances within the current class element, combining
 * their declarations into a single list.
 *
 * @return A list of [KtDeclarationElement] instances representing all declarations
 *         within the current class element and any nested class elements.
 */
fun KtClassElement.getDeclarations(): List<KtDeclarationElement> {
    return declarations.flatMap {
        if (it is KtClassElement) {
            it.getDeclarations()
        } else {
            listOf(it)
        }
    }
}