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
import io.github.kshulzh.kefir.model.api.declatation.KtClassElement
import io.github.kshulzh.kefir.model.api.declatation.KtDeclarationElement
import io.github.kshulzh.kefir.model.api.declatation.KtDeclarationsScope
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.external.KtExternalRootPackageElement
import io.github.kshulzh.kefir.model.external.annotation.wrapExternalAnnotations
import io.github.kshulzh.kefir.transform.FirWrapper
import io.github.kshulzh.kefir.transform.IrWrapper
import org.jetbrains.kotlin.fir.backend.DelicateDeclarationStorageApi
import org.jetbrains.kotlin.fir.declarations.DirectDeclarationsAccess
import org.jetbrains.kotlin.fir.declarations.FirClass
import org.jetbrains.kotlin.ir.declarations.IrClass

class KtExternalClassElement(
    override val firElement: FirClass,
    val root: KtExternalRootPackageElement,
    override var declarationsScope: KtDeclarationsScope? = null,
    override var attributes: MutableMap<String, Any> = mutableMapOf(),
) : KtClassElement, FirWrapper<FirClass>, IrWrapper<IrClass>, KtExternalElement, KtAttributes {
    override var name: KtName = firElement.symbol.name.identifier

    @OptIn(DirectDeclarationsAccess::class)
    override val declarations: MutableSet<KtDeclarationElement> by lazy {
        wrapExternalDeclaration(firElement.declarations, root, this).toMutableSet()
    }
    override val annotations: MutableList<KtAnnotationElement> by lazy {
        wrapExternalAnnotations(firElement.annotations, this).toMutableList()
    }
    override val supertypes: MutableList<KtTypeElement>
        get() = TODO("Not yet implemented")

    @OptIn(DelicateDeclarationStorageApi::class)
    override val irElement: IrClass by lazy {
        root.transformContext.irStructure.fir2IrComponents.classifierStorage.getFir2IrLazyClass(firElement)
    }

    override fun toString() = "<CLASS> $name"
}