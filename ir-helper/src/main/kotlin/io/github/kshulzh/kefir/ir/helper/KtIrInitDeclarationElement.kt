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

package io.github.kshulzh.kefir.ir.helper

import io.github.kshulzh.kefir.model.api.KtAttributes
import io.github.kshulzh.kefir.model.api.KtName
import io.github.kshulzh.kefir.model.api.declaration.KtDeclarationElement
import io.github.kshulzh.kefir.model.api.declaration.KtDeclarationsScope
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrDeclaration
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment

/**
 * Represents an IR (Intermediate Representation) Initialization Declaration Element.
 *
 * This class implements [KtDeclarationElement], [IrInitElement], and [KtAttributes].
 * It is used to define and manage a declaration element that is initialized using a specific initializer function.
 * The element supports attributes for custom metadata and is scoped within a declarations scope.
 *
 * @property initializer A function that initializes an [IrDeclaration] within a given IR module context.
 * This function accepts an [IrModuleFragment] and [IrPluginContext] to perform the initialization logic.
 * @property declarationsScope The scope in which the declaration resides, represented as a [KtDeclarationsScope].
 * This defines the hierarchical structure and visibility of declarations.
 * @property attributes A mutable map of key-value pairs, allowing custom metadata to be associated with this element.
 */
class KtIrInitDeclarationElement(
    override val initializer: (IrModuleFragment, IrPluginContext) -> IrDeclaration,
    override var declarationsScope: KtDeclarationsScope? = null,
    override var attributes: MutableMap<String, Any> = mutableMapOf(),
) : KtDeclarationElement, IrInitElement<IrDeclaration>, KtAttributes {
    /**
     * Represents the name of the declaration element.
     *
     * This property is used to identify the declaration within its scope and
     * may be utilized in various operations such as retrieval, referencing, or
     * identifying the element in contextual hierarchies.
     *
     * This value can be both retrieved and updated. The process of fetching
     * or assigning the name should align with the underlying scope and
     * declaration context to maintain consistency in the representation.
     */
    override var name: KtName
        get() = TODO("Not yet implemented")
        set(value) {}
}