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
 * Defines a base element representing a statement in the Kotlin abstract syntax tree (AST).
 *
 * This interface extends [KtElement] and serves as a foundation for all specific statement types
 * in Kotlin. It models the shared structure and behaviors of statements, making it possible to
 * handle diverse statement forms in a uniform manner.
 */
interface KtStatementElement : KtElement {
    /**
     * Defines the scope of statements associated with this element.
     *
     * This property refers to an optional [KtStatementsScope], representing a container that
     * organizes and manages a collection of statements ([KtStatementElement]) in a specific
     * scope or context. It allows for operations and transformations involving the grouped
     * statements as a whole, enabling structured handling in both intermediate and final representations.
     *
     * The property is nullable, indicating that the statement element may not always have
     * an associated scope or that the scope may not be initialized.
     */
    var statementsScope: KtStatementsScope?
}