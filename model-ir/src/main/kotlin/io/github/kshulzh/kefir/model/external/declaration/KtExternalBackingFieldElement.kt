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
import io.github.kshulzh.kefir.model.api.declaration.KtDeclarationsScope
import io.github.kshulzh.kefir.model.api.declaration.KtFieldElement
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.modifiers.KtModifier
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.external.annotation.wrapExternalAnnotations
import io.github.kshulzh.kefir.model.fir.type.wrapFirType
import io.github.kshulzh.kefir.transform.FirWrapper
import org.jetbrains.kotlin.fir.declarations.FirBackingField

class KtExternalBackingFieldElement(
    override val firElement: FirBackingField,
    override var declarationsScope: KtDeclarationsScope? = null,
    override var attributes: MutableMap<String, Any> = mutableMapOf(),
) : KtFieldElement, FirWrapper<FirBackingField>, KtExternalElement, KtAttributes {
    override var type: KtTypeElement? = wrapFirType(firElement.returnTypeRef)
    override var value: KtExpressionElement?
        get() = TODO("Not yet implemented")
        set(value) {}
    override var name: KtName = firElement.name.identifier
    override var modifiers: MutableSet<KtModifier>
        get() = TODO("Not yet implemented")
        set(value) {}
    override val annotations: MutableList<KtAnnotationElement> by lazy {
        wrapExternalAnnotations(firElement.annotations, this).toMutableList()
    }

    override fun toString() = "<FIELD> $name"
}