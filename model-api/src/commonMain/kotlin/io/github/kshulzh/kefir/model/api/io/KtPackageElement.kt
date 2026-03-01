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

/**
 * Represents a specific Kotlin package element within the package hierarchy.
 *
 * Combines functionality from `KtPackageScopeElement` and `KtPackageScope` to provide
 * both hierarchical navigation and detailed package management operations.
 *
 * A `KtPackageElement` can contain other `KtPackageScopeElement` instances such as
 * subpackages or file elements, forming a tree-like structure for representing
 * package contents.
 *
 * Implementations of this interface are expected to support:
 * - Identification through a name.
 * - Parent-child relationships within the package hierarchy.
 * - Creation, retrieval, and management of subpackage and file elements.
 * - Hierarchical navigation through package paths.
 */
interface KtPackageElement : KtPackageScopeElement, KtPackageScope