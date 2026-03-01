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

package io.github.kshulzh.kefir.model.ir.declaration

import io.github.kshulzh.kefir.model.api.KtName
import io.github.kshulzh.kefir.model.api.declatation.KtDeclarationElement
import io.github.kshulzh.kefir.model.api.io.KtFileElement
import io.github.kshulzh.kefir.model.api.io.KtPackageScope
import io.github.kshulzh.kefir.model.utils.createLazyIrSet2
import io.github.kshulzh.kefir.transform.IrWrapper
import io.github.kshulzh.kefir.transform.context.KtTransformContext
import io.github.kshulzh.kefir.transform.model.addDeclaration
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.declarations.name
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI

class KtIrFileElement(
    override var irElement: IrFile,
    var transformContext: KtTransformContext,
    override var parent: KtPackageScope? = null
) : KtFileElement, IrWrapper<IrFile> {
    @OptIn(UnsafeDuringIrConstructionAPI::class)
    override val declarations: MutableSet<KtDeclarationElement> by
    createLazyIrSet2(
        transformContext = transformContext,
        initializer = { irElement.declarations.toMutableSet() },
        transformer = { wrapIrDeclaration(it, transformContext, this@KtIrFileElement) },
        property = KtDeclarationElement::declarationsScope,
        onAdd = {
            irElement.addDeclaration(it, this)
        },
        onDelete = {
            TODO()
        }
    )

    override var name: KtName
        get() = irElement.name
        set(value) {}

    override fun toString() = "<FILE> $name"
}