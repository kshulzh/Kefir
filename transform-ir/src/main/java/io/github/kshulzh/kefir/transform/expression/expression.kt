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

package io.github.kshulzh.kefir.transform.expression

import io.github.kshulzh.kefir.ir.helper.KtIrInitExpressionElement
import io.github.kshulzh.kefir.model.api.expression.*
import io.github.kshulzh.kefir.transform.context.KtFirLocalTransformContext
import io.github.kshulzh.kefir.transform.context.KtIrLocalTransformContext
import org.jetbrains.kotlin.fir.expressions.FirExpression
import org.jetbrains.kotlin.ir.expressions.IrExpression

/**
 * Transforms a given Kotlin expression element into an equivalent IR (Intermediate Representation) expression.
 * The transformation is based on the specific type of the input expression element.
 *
 * @param input The Kotlin expression element to be transformed. Can be one of several types,
 *              such as block elements, constant elements, field access elements, value access elements,
 *              or constructor call elements.
 * @return The transformed IR expression if the input element type is supported; otherwise, returns null.
 */
fun KtIrLocalTransformContext.transformIrExpression(input: KtExpressionElement): IrExpression? {
    return when (input) {
        is KtBlockElement -> irTransform(input)
        is KtConstElement<*> -> irTransform(input)
        is KtGetFieldElement -> irTransform(input)
        is KtGetValueElement -> irTransform(input)
        is KtSetFieldElement -> irTransform(input)
        is KtConstructorCallElement -> irTransform(input)
        is KtDelegatingConstructorCallElement -> irTransform(input)
        is KtIrInitExpressionElement -> irTransform(input)
        else -> null
    }
}

/**
 * Transforms a given FIR-based expression element into a corresponding FIR expression.
 *
 * The transformation process depends on the type of the input element. If the element
 * is a block or a constant expression, it is transformed using specific handlers.
 * Otherwise, no transformation is performed, and `null` is returned.
 *
 * @param input The input expression element to be transformed. It can be a block element,
 *              a constant element, or another type of expression.
 * @return The transformed FIR expression if applicable; otherwise, returns `null`.
 */
fun KtFirLocalTransformContext.transformFirExpression(input: KtExpressionElement): FirExpression? {
    return when (input) {
        is KtBlockElement -> firTransform(input)
        is KtConstElement<*> -> firTransform(input)
        else -> null
    }
}