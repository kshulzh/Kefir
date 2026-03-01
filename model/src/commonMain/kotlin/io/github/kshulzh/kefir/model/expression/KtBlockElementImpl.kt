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

package io.github.kshulzh.kefir.model.expression

import io.github.kshulzh.kefir.model.api.KtAttributes
import io.github.kshulzh.kefir.model.api.KtElement
import io.github.kshulzh.kefir.model.api.expression.KtBlockElement
import io.github.kshulzh.kefir.model.api.statement.KtStatementElement
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.utils.createListDelegate

/**
 * Implementation of the [KtBlockElement] interface, representing a block of statements
 * in the Kotlin abstract syntax tree (AST).
 *
 * A block element encapsulates a collection of statements and provides type information,
 * contextual hierarchy, and extensibility through attributes. This implementation leverages
 * delegation to manage the storage and behavior of its contained statements.
 *
 * @property statements A mutable list of [KtStatementElement], representing the statements
 * in the block. This property uses a delegated mechanism for observing and managing changes
 * in the statement list.
 * @property type The type information associated with the block element, modeled as an
 * optional [KtTypeElement]. It provides metadata about the expected return type of the block
 * and can be null if the type is not explicitly defined.
 * @property parent The parent element within the AST hierarchy. This optional [KtElement]
 * reference enables contextual positioning of the block within a structural tree.
 * @property attributes A mutable map for storing additional metadata or properties. This
 * extensible feature allows for custom annotations or attributes that enrich the block's
 * semantics or functionality.
 */
class KtBlockElementImpl(
    statements: MutableList<KtStatementElement> = mutableListOf(),
    override var type: KtTypeElement? = null,
    override var parent: KtElement? = null,
    override var attributes: MutableMap<String, Any> = mutableMapOf()
) : KtBlockElement, KtAttributes {
    /**
     * Represents the list of statement elements contained within this block element.
     *
     * This property is delegated to a custom list delegate, ensuring that changes to the list
     * trigger necessary updates related to its containing scope. The delegate leverages the
     * `createListDelegate` utility to manage the relationship between the collection and the
     * associated elements.
     *
     * Each element in the list is of type [KtStatementElement], and its scope is defined
     * by the `statementsScope` property specific to each statement element.
     */
    override var statements: MutableList<KtStatementElement> by createListDelegate(
        statements,
        KtStatementElement::statementsScope
    )
}