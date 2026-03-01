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

package io.github.kshulzh.kefir.model.fir.declaration

import io.github.kshulzh.kefir.model.api.KtName
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationElement
import io.github.kshulzh.kefir.model.api.declatation.KtDeclarationsScope
import io.github.kshulzh.kefir.model.api.declatation.KtFieldElement
import io.github.kshulzh.kefir.model.api.declatation.KtFunctionElement
import io.github.kshulzh.kefir.model.api.declatation.KtPropertyElement
import io.github.kshulzh.kefir.model.api.modifiers.KtModifier
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.transform.FirWrapper
import io.github.kshulzh.kefir.transform.context.KtFirTransformContext
import org.jetbrains.kotlin.fir.declarations.FirProperty

class KtFirPropertyElement(
    override val firElement: FirProperty,
    var transformFirContext: KtFirTransformContext,
    override var declarationsScope: KtDeclarationsScope? = null
) : KtPropertyElement, FirWrapper<FirProperty> {
    override var type: KtTypeElement
        get() = TODO("Not yet implemented")
        set(value) {}
    override var field: KtFieldElement?
        get() = TODO("Not yet implemented")
        set(value) {}
    override var getter: KtFunctionElement?
        get() = TODO("Not yet implemented")
        set(value) {}
    override var setter: KtFunctionElement?
        get() = TODO("Not yet implemented")
        set(value) {

        }
    override var name: KtName
        get() = TODO("Not yet implemented")
        set(value) {}
    override var modifiers: MutableSet<KtModifier>
        get() = TODO("Not yet implemented")
        set(value) {}
    override var annotations: MutableList<KtAnnotationElement>
        get() = TODO("Not yet implemented")
        set(value) {}
}