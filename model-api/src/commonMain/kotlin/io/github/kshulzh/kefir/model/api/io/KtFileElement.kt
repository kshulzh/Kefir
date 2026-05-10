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

package io.github.kshulzh.kefir.model.api.io

import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationsScope
import io.github.kshulzh.kefir.model.api.declaration.KtDeclarationsScope
import io.github.kshulzh.kefir.model.api.utils.KtVisitor

/**
 * Represents a Kotlin file element within a package scope.
 *
 * Combines functionality for managing Kotlin declarations and acting as
 * a part of the package hierarchy. This interface allows access to Kotlin
 * declarations defined in the file and supports hierarchical navigation
 * through package scopes.
 *
 * Inherits features from:
 * - [KtDeclarationsScope]: Provides declaration management within the
 *   file, including searching for specific declarations by name and type.
 * - [KtPackageScopeElement]: Integrates the file as part of a package,
 *   enabling hierarchical package navigation and contextual scope resolution.
 */
interface KtFileElement : KtDeclarationsScope, KtPackageScopeElement, KtAnnotationsScope {
    override fun <R, D> accept(visitor: KtVisitor<R, D>, data: D): R = visitor.visitFile(this, data)
}