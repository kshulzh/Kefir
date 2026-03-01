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

import io.github.kshulzh.kefir.model.api.KtExternalElement
import io.github.kshulzh.kefir.model.api.KtName
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationElement
import io.github.kshulzh.kefir.model.api.arg.KtParameterElement
import io.github.kshulzh.kefir.model.api.declatation.KtDeclarationsScope
import io.github.kshulzh.kefir.model.api.declatation.KtFunctionElement
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.modifiers.KtModifier
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.external.KtExternalRootPackageElement
import io.github.kshulzh.kefir.model.external.annotation.wrapExternalAnnotations
import io.github.kshulzh.kefir.model.fir.type.wrapFirType
import io.github.kshulzh.kefir.transform.FirWrapper
import io.github.kshulzh.kefir.transform.IrWrapper
import org.jetbrains.kotlin.fir.declarations.FirFunction
import org.jetbrains.kotlin.fir.declarations.utils.nameOrSpecialName
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI

class KtExternalFunctionElement(
    override val firElement: FirFunction,
    val root: KtExternalRootPackageElement,
    override var declarationsScope: KtDeclarationsScope? = null,
) : KtFunctionElement, KtExternalElement, FirWrapper<FirFunction>, IrWrapper<IrFunction> {
    override var body: KtExpressionElement? = null
    override var type: KtTypeElement? = wrapFirType(firElement.returnTypeRef)
    override var name: KtName = firElement.nameOrSpecialName.identifier
    override var parameters: MutableList<KtParameterElement>
        get() = TODO("Not yet implemented")
        set(value) {
        }
    override var modifiers: MutableSet<KtModifier>
        get() = TODO("Not yet implemented")
        set(value) {
            firElement.status
        }
    override val annotations: MutableList<KtAnnotationElement> by lazy {
        wrapExternalAnnotations(firElement.annotations, this).toMutableList()
    }

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    override val irElement: IrFunction by lazy {
        root.transformContext.irStructure.fir2IrComponents.declarationStorage.getIrFunctionSymbol(
            firElement.symbol
        ).owner
    }

    override fun toString() = "<FUNCTION> $name"
}