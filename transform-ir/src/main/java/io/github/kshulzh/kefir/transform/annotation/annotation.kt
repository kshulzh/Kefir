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

package io.github.kshulzh.kefir.transform.annotation

import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationElement
import io.github.kshulzh.kefir.transform.context.KtFirTransformContext
import io.github.kshulzh.kefir.transform.context.KtTransformContext
import io.github.kshulzh.kefir.transform.utils.linkFir
import org.jetbrains.kotlin.fir.expressions.FirAnnotation
import org.jetbrains.kotlin.fir.expressions.builder.buildAnnotation
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall

/**
 * Transforms a provided annotation element into an IR constructor call representation.
 *
 * @param input The annotation element to be transformed into an IR constructor call.
 * @return The transformed IR constructor call if the transformation is successful, or null if the transformation fails.
 */
//todo implement
fun KtTransformContext.transformIrAnnotation(input: KtAnnotationElement): IrConstructorCall? {
//    val type by input.tryGet("type") {
//        irTransform.type(input.resolveType()!!)!!
//    }
//    return buildConstructorCall(
//
//    ).linkIr(input)
    return null
}

/**
 * Transforms a given Kotlin annotation element into a FIR representation.
 *
 * @param input The Kotlin annotation element to transform.
 * @return The resulting FIR annotation if the transformation is successful, or null otherwise.
 */
fun KtFirTransformContext.transformFirAnnotation(input: KtAnnotationElement): FirAnnotation? {
    //todo implement
    return buildAnnotation {

    }.linkFir(input)
}