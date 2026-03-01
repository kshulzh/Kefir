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
import io.github.kshulzh.kefir.model.api.arg.KtParameterElement
import io.github.kshulzh.kefir.model.api.declatation.KtDeclarationsScope
import io.github.kshulzh.kefir.model.api.declatation.KtFunctionElement
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.modifiers.KtModifier
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.ir.annotation.wrapIrAnnotations
import io.github.kshulzh.kefir.model.utils.createLazyIr2
import io.github.kshulzh.kefir.transform.IrWrapper
import io.github.kshulzh.kefir.transform.context.KtTransformContext
import io.github.kshulzh.kefir.transform.model.replaceBody
import org.jetbrains.kotlin.ir.declarations.IrFunction

class KtIrFunctionElement(
    override var irElement: IrFunction,
    var transformContext: KtTransformContext,
    override var declarationsScope: KtDeclarationsScope? = null
) : KtFunctionElement, IrWrapper<IrFunction> {
    override var body: KtExpressionElement? by
    createLazyIr2(
        transformContext,
        irElement::body,
        { it?.let { wrapIrBody(it, transformContext, this) } },
        property = KtExpressionElement::parent
    ) {
        it?.let { irElement.replaceBody(it) }
    }
    override var type: KtTypeElement?
        get() = TODO("Not yet implemented")
        set(value) {}
    override var modifiers: MutableSet<KtModifier>
        get() = TODO("Not yet implemented")
        set(value) {}
    override var name: KtName
        get() = irElement.name.asString()
        set(value) {}
    override var annotations: MutableList<KtAnnotationElement> =
        wrapIrAnnotations(irElement.annotations, transformContext, this)
        set(value) {}
    override var parameters: MutableList<KtParameterElement>
        get() = TODO("Not yet implemented")
        set(value) {}

    override fun toString() = "<FUN> $name"
}