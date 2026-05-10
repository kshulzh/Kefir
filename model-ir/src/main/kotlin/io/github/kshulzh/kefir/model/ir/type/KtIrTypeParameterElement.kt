/*
 * Copyright (c) 2026. Kirill Shulzhenko
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

package io.github.kshulzh.kefir.model.ir.type

import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationElement
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.api.type.KtTypeParameterElement
import io.github.kshulzh.kefir.model.api.type.KtTypeParameterScope
import io.github.kshulzh.kefir.model.api.type.Variance
import io.github.kshulzh.kefir.model.ir.annotation.wrapIrAnnotations
import io.github.kshulzh.kefir.model.utils.createLazyIrList2
import io.github.kshulzh.kefir.transform.IrWrapper
import io.github.kshulzh.kefir.transform.context.KtTransformContext
import org.jetbrains.kotlin.ir.declarations.IrTypeParameter

class KtIrTypeParameterElement(
    override val irElement: IrTypeParameter,
    var transformContext: KtTransformContext,
    override var typeParameterScope: KtTypeParameterScope? = null,
) : KtTypeParameterElement, IrWrapper<IrTypeParameter> {
    init {
        irElement.kefir = this
    }
    override var name: String
        get() = irElement.name.asString()
        set(value) {}
    override var supperTypes: MutableList<KtTypeElement> by createLazyIrList2(
        transformContext,
        irElement::superTypes,
        { wrapType(it, transformContext)!!},
        onAdd = { e, i ->
            val element = irTransform(e)!!
            val superTypes = irElement.superTypes
            if (superTypes is MutableList) {
                if (i > -1) {
                    superTypes.add(i, element)
                } else {
                    superTypes.add(element)
                }
            }
            null
        }

    )
    override var variance: Variance
        get() = TODO("Not yet implemented")
        set(value) {}
    override val annotations: MutableList<KtAnnotationElement> by lazy {
        wrapIrAnnotations(irElement.annotations, transformContext, this).toMutableList()
    }
}