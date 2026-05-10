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
import io.github.kshulzh.kefir.model.api.declaration.KtDeclarationsScope
import io.github.kshulzh.kefir.model.api.declaration.KtFieldElement
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.modifiers.KtModifier
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.transform.FirWrapper
import io.github.kshulzh.kefir.transform.context.KtFirTransformContext
import org.jetbrains.kotlin.fir.declarations.FirField

class KtFirFieldElement(
    override val firElement: FirField,
    var transformFirContext: KtFirTransformContext,
    override var declarationsScope: KtDeclarationsScope? = null,
) : KtFieldElement, FirWrapper<FirField> {
    override var type: KtTypeElement?
        get() = TODO("Not yet implemented")
        set(value) {}
    override var value: KtExpressionElement?
        get() = TODO("Not yet implemented")
        set(value) {}
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