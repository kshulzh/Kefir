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

import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement

/**
 * Represents a Kotlin expression as a statement.
 *
 * This interface is a specialized type of [KtStatementElement], designed to model
 * statements that consist of a single expression. It serves as a bridge between
 * expressions and statements, allowing an expression to act as a standalone
 * statement in Kotlin's syntax.
 */
interface KtExpressionStatement : KtStatementElement {
    /**
     * Represents an expression element within a statement.
     *
     * This property refers to a [KtExpressionElement], which serves as a foundational type
     * for expressions. It allows statements to
     * encapsulate and manipulate expression data, supporting the transformation and evaluation
     * processes in both FIR and IR contexts.
     *
     * The property is nullable, indicating that the expression may not always be present
     * or initialized in the enclosing statement.
     */
    val expression: KtExpressionElement?
}