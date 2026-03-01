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

import io.github.kshulzh.kefir.model.api.declatation.KtDeclarationElement
import io.github.kshulzh.kefir.model.api.declatation.KtDeclarationsScope
import io.github.kshulzh.kefir.model.external.KtExternalRootPackageElement
import org.jetbrains.kotlin.fir.declarations.*

fun wrapExternalDeclaration(
    firDeclaration: FirDeclaration,
    root: KtExternalRootPackageElement,
    declarationScope: KtDeclarationsScope? = null
): KtDeclarationElement? = when (firDeclaration) {
    is FirClass -> KtExternalClassElement(firDeclaration, root, declarationScope)
    is FirConstructor -> KtExternalConstructorElement(firDeclaration, root, declarationScope)
    is FirFunction -> KtExternalFunctionElement(firDeclaration, root, declarationScope)
    is FirProperty -> KtExternalPropertyElement(firDeclaration, root, declarationScope)
    is FirField -> KtExternalFieldElement(firDeclaration, declarationScope)
    else -> null
}

fun wrapExternalDeclaration(
    declarations: List<FirDeclaration>,
    root: KtExternalRootPackageElement,
    declarationScope: KtDeclarationsScope? = null
): MutableList<KtDeclarationElement> {
    return declarations.mapNotNull { wrapExternalDeclaration(it, root, declarationScope) }.toMutableList()
}