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

package io.github.kshulzh.kefir.model.ir.declaration

import io.github.kshulzh.kefir.model.api.KtElement
import io.github.kshulzh.kefir.model.api.declatation.KtDeclarationElement
import io.github.kshulzh.kefir.model.api.declatation.KtDeclarationsScope
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.ir.expression.KtIrBodyBlockElement
import io.github.kshulzh.kefir.model.ir.expression.wrapExpression
import io.github.kshulzh.kefir.transform.context.KtTransformContext
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.IrBlockBody
import org.jetbrains.kotlin.ir.expressions.IrBody
import org.jetbrains.kotlin.ir.expressions.IrExpressionBody

fun wrapIrDeclaration(
    irDeclaration: IrDeclaration,
    transformContext: KtTransformContext,
    declarationScope: KtDeclarationsScope? = null
): KtDeclarationElement? = when (irDeclaration) {
    is IrClass -> KtIrClassElement(irDeclaration, transformContext, declarationScope)
    is IrConstructor -> KtIrConstructorElement(irDeclaration, transformContext, declarationScope)
    is IrFunction -> KtIrFunctionElement(irDeclaration, transformContext, declarationScope)
    is IrProperty -> KtIrPropertyElement(irDeclaration, transformContext, declarationScope)
    is IrField -> KtIrFieldElement(irDeclaration, transformContext, declarationScope)
    else -> null
}

fun wrapIrDeclarations(
    declarations: List<IrDeclaration>,
    transformContext: KtTransformContext,
    declarationScope: KtDeclarationsScope? = null
): MutableList<KtDeclarationElement> {
    return declarations.mapNotNull { wrapIrDeclaration(it, transformContext, declarationScope) }.toMutableList()
}

fun wrapIrBody(irBody: IrBody, transformContext: KtTransformContext, parent: KtElement): KtExpressionElement {
    if (irBody is IrExpressionBody) {
        return wrapExpression(irBody.expression, transformContext)!!
    } else if (irBody is IrBlockBody) {
        return KtIrBodyBlockElement(irBody, transformContext, parent)
    }
    throw IllegalStateException("Unsupported body type")
}