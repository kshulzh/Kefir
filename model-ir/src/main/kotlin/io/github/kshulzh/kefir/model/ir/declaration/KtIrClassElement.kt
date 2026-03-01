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
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationElement
import io.github.kshulzh.kefir.model.api.declatation.KtClassElement
import io.github.kshulzh.kefir.model.api.declatation.KtDeclarationElement
import io.github.kshulzh.kefir.model.api.declatation.KtDeclarationsScope
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.ir.annotation.wrapIrAnnotations
import io.github.kshulzh.kefir.model.utils.createLazyIrSet2
import io.github.kshulzh.kefir.transform.FirWrapper
import io.github.kshulzh.kefir.transform.IrWrapper
import io.github.kshulzh.kefir.transform.context.KtTransformContext
import io.github.kshulzh.kefir.transform.model.addDeclaration
import org.jetbrains.kotlin.fir.backend.FirMetadataSource
import org.jetbrains.kotlin.fir.declarations.FirClass
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.utils.addToStdlib.UnsafeCastFunction
import org.jetbrains.kotlin.utils.addToStdlib.cast

class KtIrClassElement(
    override var irElement: IrClass,
    var transformContext: KtTransformContext,
    override var declarationsScope: KtDeclarationsScope? = null
) : KtClassElement, IrWrapper<IrClass>, FirWrapper<FirClass> {
    override var name: KtName
        get() = irElement.name.identifier
        set(value) {}

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    override val declarations: MutableSet<KtDeclarationElement> by createLazyIrSet2(
        transformContext = transformContext,
        initializer = { irElement.declarations.toMutableSet() },
        transformer = { wrapIrDeclaration(it, transformContext, this@KtIrClassElement) },
        property = KtDeclarationElement::declarationsScope,
        onAdd = {
            irElement.addDeclaration(it, this)
        },
        onDelete = {
            //TODO()
        }
    )
    override var annotations: MutableList<KtAnnotationElement> =
        wrapIrAnnotations(irElement.annotations, transformContext, this)
        set(value) {}


    override fun toString() = "<CLASS> $name"

    @OptIn(UnsafeCastFunction::class)
    override val firElement: FirClass = irElement.metadata?.cast<FirMetadataSource>()?.fir?.cast<FirClass>()!!
    override val supertypes: MutableList<KtTypeElement>
        get() = TODO("Not yet implemented")

}