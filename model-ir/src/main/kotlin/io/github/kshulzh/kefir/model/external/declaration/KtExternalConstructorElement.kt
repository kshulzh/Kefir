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

package io.github.kshulzh.kefir.model.external.declaration

import io.github.kshulzh.kefir.model.api.KtAttributes
import io.github.kshulzh.kefir.model.api.KtExternalElement
import io.github.kshulzh.kefir.model.api.KtName
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationElement
import io.github.kshulzh.kefir.model.api.declaration.KtConstructorElement
import io.github.kshulzh.kefir.model.api.declaration.KtDeclarationsScope
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.modifiers.KtModifier
import io.github.kshulzh.kefir.model.api.type.KtTypeParameterElement
import io.github.kshulzh.kefir.model.external.KtExternalRootPackageElement
import io.github.kshulzh.kefir.model.external.annotation.KtExternalAnnotationElement
import io.github.kshulzh.kefir.transform.FirWrapper
import io.github.kshulzh.kefir.transform.IrWrapper
import org.jetbrains.kotlin.fir.declarations.FirConstructor
import org.jetbrains.kotlin.ir.declarations.IrConstructor
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI

class KtExternalConstructorElement(
    override val firElement: FirConstructor,
    val root: KtExternalRootPackageElement,
    override var declarationsScope: KtDeclarationsScope? = null,
    override var attributes: MutableMap<String, Any> = mutableMapOf(),
) : KtConstructorElement, FirWrapper<FirConstructor>, IrWrapper<IrConstructor>, KtExternalElement, KtAttributes {
    override var body: KtExpressionElement? = null
    override var modifiers: MutableSet<KtModifier>
        get() = TODO("Not yet implemented")
        set(value) {}
    override var name: KtName
        get() = "<constructor>"
        set(value) {}

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    override val irElement: IrConstructor by lazy {
        root.transformContext.irStructure.fir2IrComponents.declarationStorage.getIrConstructorSymbol(
            firElement.symbol
        ).owner
    }

    override fun toString() = "<CONSTRUCTOR> $name"
    override var typeParameters: MutableList<KtTypeParameterElement>
        get() = TODO("Not yet implemented")
        set(value) {}

    override val annotations: MutableList<KtAnnotationElement> by lazy {
        firElement.annotations.map { KtExternalAnnotationElement(it, this) }.toMutableList()
    }
}