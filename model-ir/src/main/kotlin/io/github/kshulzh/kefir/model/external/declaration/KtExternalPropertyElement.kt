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
import io.github.kshulzh.kefir.model.api.declatation.KtDeclarationsScope
import io.github.kshulzh.kefir.model.api.declatation.KtFieldElement
import io.github.kshulzh.kefir.model.api.declatation.KtFunctionElement
import io.github.kshulzh.kefir.model.api.declatation.KtPropertyElement
import io.github.kshulzh.kefir.model.api.modifiers.KtModifier
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.external.KtExternalRootPackageElement
import io.github.kshulzh.kefir.model.external.annotation.wrapExternalAnnotations
import io.github.kshulzh.kefir.model.fir.type.wrapFirType
import io.github.kshulzh.kefir.transform.FirWrapper
import org.jetbrains.kotlin.fir.declarations.FirProperty

class KtExternalPropertyElement(
    override val firElement: FirProperty,
    val root: KtExternalRootPackageElement,
    override var declarationsScope: KtDeclarationsScope? = null,
) : KtPropertyElement, FirWrapper<FirProperty>, KtExternalElement {
    override var type: KtTypeElement = wrapFirType(firElement.returnTypeRef)!!
    override var field: KtFieldElement? = firElement.backingField?.let {
        KtExternalBackingFieldElement(it, declarationsScope)
    }
    override var getter: KtFunctionElement? = firElement.getter?.let {
        KtExternalFunctionElement(it, root, declarationsScope)
    }
    override var setter: KtFunctionElement? = firElement.setter?.let {
        KtExternalFunctionElement(it, root, declarationsScope)
    }
    override var name: KtName = firElement.name.identifier
    override var modifiers: MutableSet<KtModifier>
        get() = TODO("Not yet implemented")
        set(value) {}
    override val annotations: MutableList<KtAnnotationElement> by lazy {
        wrapExternalAnnotations(firElement.annotations, this).toMutableList()
    }

    override fun toString() = "<PROPERTY> $name"
}