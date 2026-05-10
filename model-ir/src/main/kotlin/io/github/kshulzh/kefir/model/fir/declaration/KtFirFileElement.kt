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

package io.github.kshulzh.kefir.model.fir.declaration

import io.github.kshulzh.kefir.model.api.KtName
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationElement
import io.github.kshulzh.kefir.model.api.declaration.KtDeclarationElement
import io.github.kshulzh.kefir.model.api.io.KtFileElement
import io.github.kshulzh.kefir.model.api.io.KtPackageScope
import io.github.kshulzh.kefir.transform.FirWrapper
import io.github.kshulzh.kefir.transform.context.KtFirTransformContext
import org.jetbrains.kotlin.fir.declarations.FirFile
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI

class KtFirFileElement(
    override val firElement: FirFile,
    var transformFirContext: KtFirTransformContext,
    override var parent: KtPackageScope? = null,
) : KtFileElement, FirWrapper<FirFile> {
    @OptIn(UnsafeDuringIrConstructionAPI::class)
    override val declarations: MutableSet<KtDeclarationElement> by lazy {
        TODO()
    }

    override var name: KtName
        get() = firElement.name
        set(value) {}

    override fun toString() = name
    override val annotations: MutableList<KtAnnotationElement>
        get() = TODO("Not yet implemented")

}