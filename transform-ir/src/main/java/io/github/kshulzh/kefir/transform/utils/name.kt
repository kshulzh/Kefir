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

package io.github.kshulzh.kefir.transform.utils

import io.github.kshulzh.kefir.model.api.KtName
import io.github.kshulzh.kefir.model.api.KtPath
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

/**
 * Transforms the current `KtName` instance into a `Name` instance by determining
 * a suitable representation using the first character of the `KtName`.
 *
 * @return A `Name` object determined based on the first character of the `KtName`.
 */
fun KtName.transform(): Name {
    return Name.guessByFirstCharacter(this)
}

/**
 * Transforms the current `KtPath` instance into a fully qualified name (`FqName`).
 *
 * If the path contains no parts, the result is `null`. Otherwise, the parts are
 * combined into a dot-separated string to form the `FqName`.
 *
 * @return The resulting fully qualified name (`FqName`) if the path is not empty, or `null` if the path has no parts.
 */
fun KtPath.transform(): FqName? {
    return if (parts.isEmpty()) null else FqName(parts.reduce { acc, s -> "${acc}.$s" })
}