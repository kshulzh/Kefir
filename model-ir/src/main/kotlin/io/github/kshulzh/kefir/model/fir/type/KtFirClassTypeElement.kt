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

package io.github.kshulzh.kefir.model.fir.type

import io.github.kshulzh.kefir.model.api.KtPath
import io.github.kshulzh.kefir.model.api.type.KtClassTypeElement
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.transform.FirWrapper
import org.jetbrains.kotlin.fir.types.ConeClassLikeType

class KtFirClassTypeElement(
    override val firElement: ConeClassLikeType,
) : KtClassTypeElement, FirWrapper<ConeClassLikeType> {
    val classId = firElement.lookupTag.classId
    override val ktPackage: KtPath = KtPath(classId.packageFqName.asString())
    override val ktClass: KtPath = KtPath(classId.relativeClassName.asString())
    override val isNullable: Boolean = firElement.isMarkedNullable
    override val typeArguments: MutableList<KtTypeElement>
        get() = TODO("Not yet implemented")
}