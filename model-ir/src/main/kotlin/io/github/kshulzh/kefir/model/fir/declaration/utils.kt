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

import io.github.kshulzh.kefir.model.api.declaration.KtDeclarationElement
import io.github.kshulzh.kefir.model.api.declaration.KtDeclarationsScope
import io.github.kshulzh.kefir.transform.context.KtTransformContext
import org.jetbrains.kotlin.fir.declarations.*

fun wrapFirDeclaration(
    firDeclaration: FirDeclaration,
    transformContext: KtTransformContext,
    declarationScope: KtDeclarationsScope? = null
): KtDeclarationElement? = when (firDeclaration) {
    is FirClass -> KtFirClassElement(firDeclaration, transformContext, declarationScope)
    is FirConstructor -> KtFirConstructorElement(firDeclaration, transformContext, declarationScope)
    is FirFunction -> KtFirFunctionElement(firDeclaration, transformContext, declarationScope)
    is FirProperty -> KtFirPropertyElement(firDeclaration, transformContext, declarationScope)
    is FirField -> KtFirFieldElement(firDeclaration, transformContext, declarationScope)
    else -> null
}

fun wrapFirDeclarations(
    declarations: List<FirDeclaration>,
    transformContext: KtTransformContext,
    declarationScope: KtDeclarationsScope? = null
): MutableList<KtDeclarationElement> {
    return declarations.mapNotNull { wrapFirDeclaration(it, transformContext, declarationScope) }.toMutableList()
}