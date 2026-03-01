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

package io.github.kshulzh.kefir.model.api.statement

import io.github.kshulzh.kefir.model.api.KtElement

/**
 * Represents a scope containing a collection of Kotlin statements in the abstract syntax tree (AST).
 *
 * This interface provides a container for organizing and managing a structured collection of
 * [KtStatementElement] instances. It models a logical grouping of statements that belong to a
 * specific scope, such as the body of a function, a block, or another compound statement.
 *
 * Its purpose is to support operations on the group of statements, enabling transformations,
 * analysis, and manipulation of the statements as a collective unit in Kotlin's intermediate
 * representations and analysis pipelines.
 */
interface KtStatementsScope : KtElement {
    /**
     * Represents a mutable list of Kotlin statement elements within a specific scope.
     *
     * This property contains a collection of [KtStatementElement], which includes various
     * types of statements modeled in the Kotlin abstract syntax tree (AST). The list
     * organizes and maintains the statements associated with the current [KtStatementsScope].
     *
     * Modifying this list (e.g., adding, removing, or transforming its elements) allows
     * operations and transformations to be applied within the current scope.
     */
    var statements: MutableList<KtStatementElement>
}