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

import io.github.kshulzh.kefir.model.api.KtPath
import io.github.kshulzh.kefir.model.api.declatation.KtClassElement
import io.github.kshulzh.kefir.model.api.declatation.KtDeclarationElement
import io.github.kshulzh.kefir.model.api.io.path
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName

/**
 * Computes and returns the fully qualified `ClassId` of the `KtClassElement`.
 *
 * This method calculates the `ClassId` by extracting the file path and hierarchical
 * declaration path of the class. It utilizes `getPathToFile` to obtain the file path
 * and declaration components, and transforms them into a fully qualified
 * class name (`FqName`) and package path.
 *
 * @return The `ClassId` representing the fully qualified identifier of the class,
 *         constructed from the package path and class name.
 */
fun KtClassElement.classId(): ClassId {
    val rl = this.getPathToFile()
    val p = rl.first!!.path

    return ClassId(p.transform() ?: FqName.ROOT, rl.second.transform()!!, false)
}

/**
 * Constructs a `CallableId` for the current `KtDeclarationElement` based on its hierarchical path
 * and name within the Kotlin model structure.
 *
 * The method utilizes the path to the declaration obtained from `getPathToFile()` to determine
 * the package name and declaration path, and combines these with the declaration's name to
 * produce the `CallableId`.
 *
 * @return The `CallableId` representing the declaration's fully qualified identifier, including
 * its package name, containing path, and name. If the declaration has no hierarchical path,
 * the package name and the direct name of the declaration are used.
 */
fun KtDeclarationElement.callableId(): CallableId {
    val rl = this.getPathToFile()
    val p = rl.first!!.path
    val path = rl.second.parts.dropLast(1).toMutableList()

    return if (path.isNotEmpty()) {
        CallableId(p.transform() ?: FqName.ROOT, KtPath(path).transform()!!, this.name.transform())
    } else {
        CallableId(p.transform() ?: FqName.ROOT, this.name.transform())
    }
}