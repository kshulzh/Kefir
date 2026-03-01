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

package io.github.kshulzh.kefir.model.declatation

import io.github.kshulzh.kefir.model.api.KtAttributes
import io.github.kshulzh.kefir.model.api.KtName
import io.github.kshulzh.kefir.model.api.declatation.KtDeclarationElement
import io.github.kshulzh.kefir.model.api.io.KtFileElement
import io.github.kshulzh.kefir.model.api.io.KtPackageScope

/**
 * Implementation of the [KtFileElement] interface that represents a Kotlin file element
 * within a package scope. It serves as a container for Kotlin declarations and attributes,
 * integrating hierarchical navigation through package scopes and declaration management.
 *
 * This class also implements [KtAttributes], providing mechanisms to manage custom
 * metadata or properties dynamically via a mutable attributes map.
 *
 * @property name The name of the file element in the Kotlin model, identifying
 * the file uniquely within its package scope.
 * @property parent The parent package scope to which this file belongs, enabling
 * hierarchical navigation. Can be null if no parent scope exists.
 * @property declarations A mutable set containing all declarations (e.g., classes,
 * properties, functions) defined within the file.
 * @property attributes A mutable map for storing custom attributes or metadata
 * associated with this file element.
 */
class KtFileElementImpl(
    override var name: KtName,
    override var parent: KtPackageScope? = null,
    override var declarations: MutableSet<KtDeclarationElement> = mutableSetOf(),
    override var attributes: MutableMap<String, Any> = mutableMapOf(),
) : KtFileElement, KtAttributes {
    override fun toString() = "<FILE> $name"
}