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
import io.github.kshulzh.kefir.model.api.KtElement
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationElement
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.expressions.IrExpression

/**
 * Represents an IR-backed expression element in the Kotlin tree. This class implements multiple
 * interfaces to align with IR initialization, expression properties, and mutable attributes.
 *
 * The primary purpose of this class is to support the initialization and representation of
 * Kotlin expressions within the context of intermediate representation (IR).
 *
 * @constructor Creates an instance of `KtIrInitExpressionElement` with a specified initializer lambda,
 *              a parent element, and a mutable map of attributes.
 *
 * @property initializer A lambda function that initializes the IR representation of this expression.
 *                       It accepts an `IrModuleFragment` and `IrPluginContext` as parameters and returns
 *                       an `IrExpression`. This lambda serves as the entry point for any expression initialization.
 *
 * @property parent A reference to the parent `KtElement` within the Kotlin tree structure. Represents
 *                  the hierarchical relationship of this expression within the tree.
 *
 * @property attributes A mutable map storing arbitrary attributes for this element. This allows for
 *                      attaching additional metadata or configuration at runtime.
 */
class KtIrInitExpressionElement(
    override val initializer: (IrModuleFragment, IrPluginContext) -> IrExpression,
    override var parent: KtElement? = null,
    override val annotations: MutableList<KtAnnotationElement> = mutableListOf(),
    override var attributes: MutableMap<String, Any> = mutableMapOf(),
) : KtExpressionElement, IrInitElement<IrExpression>, KtAttributes {
    /**
     * Represents the type of this element in the Kotlin Intermediate Representation (IR) model.
     *
     * This property defines the associated [KtTypeElement], which encapsulates the
     * type-related metadata and behavior for the represented element.
     *
     * It can be utilized to retrieve or update the type information tied to this element.
     * The actual type assignment may vary depending on the context and structure of the element.
     */
    override var type: KtTypeElement?
        get() = TODO("Not yet implemented")
        set(value) {}
}