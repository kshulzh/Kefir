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

package io.github.kshulzh.kefir.model.api.expression

import io.github.kshulzh.kefir.model.api.statement.KtStatementsScope

/**
 * Represents a block element within the Kotlin abstract syntax tree (AST).
 * A block element typically encapsulates a sequence of statements and expressions
 * that are evaluated in a specific sequence.
 *
 * This interface combines the functionality of a statements scope and an expression element,
 * enabling it to store statements and optionally have a type or parent context.
 *
 * It inherits properties and behavior from:
 * - [KtStatementsScope]: allowing the storage and management of contained statements.
 * - [KtExpressionElement]: providing type information and hierarchical positioning within the AST.
 */
interface KtBlockElement : KtStatementsScope, KtExpressionElement