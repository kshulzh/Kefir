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

package io.github.kshulzh.kefir.model.external.type

import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.external.KtExternalRootPackageElement
import org.jetbrains.kotlin.fir.declarations.FirTypeParameter
import org.jetbrains.kotlin.fir.types.ConeClassLikeType
import org.jetbrains.kotlin.fir.types.ConeTypeParameterType

val typeParams: MutableMap<FirTypeParameter, KtExternalTypeParameterElement> = mutableMapOf()

fun wrapExternalType(any: Any, root: KtExternalRootPackageElement) : KtTypeElement? {
    return when (any) {
        is ConeClassLikeType -> KtExternalClassTypeElement(any, root)
        is ConeTypeParameterType -> KtExternalParameterTypeElement(any, root)
        else -> null
    }
}