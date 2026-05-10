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
import io.github.kshulzh.kefir.model.api.statement.KtStatementElement
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment

/**
 * Represents an IR-backed statement element in the Kotlin tree. This class implements multiple
 * interfaces to support IR initialization, statement properties, and maintaining mutable attributes.
 *
 * The primary purpose of this class is to facilitate the initialization and representation of
 * Kotlin statements within the context of intermediate representation (IR).
 *
 * @constructor Creates an instance of `KtIrInitStatementElement` with a specified initializer lambda,
 *              an optional statement scope, and a mutable map of attributes.
 *
 * @property initializer A lambda function that initializes the IR representation of this statement.
 *                       It accepts an `IrModuleFragment` and `IrPluginContext` as parameters and returns
 *                       an `IrStatement`. This serves as the entry point for any statement initialization.
 *
 *
 * @property attributes A mutable map storing arbitrary attributes for this element. This allows attaching
 *                      additional metadata or runtime configuration to the element.
 */
class KtIrInitStatementElement(
    override val initializer: (IrModuleFragment, IrPluginContext) -> IrStatement,
    override var parent: KtElement? = null,
    override val annotations: MutableList<KtAnnotationElement> = mutableListOf(),
    override var attributes: MutableMap<String, Any> = mutableMapOf(),
) : KtStatementElement, IrInitElement<IrStatement>, KtAttributes