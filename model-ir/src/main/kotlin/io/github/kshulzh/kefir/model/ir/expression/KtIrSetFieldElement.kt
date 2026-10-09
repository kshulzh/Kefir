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

package io.github.kshulzh.kefir.model.ir.expression

import io.github.kshulzh.kefir.model.api.KtElement
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationElement
import io.github.kshulzh.kefir.model.api.declaration.KtFieldElement
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.expression.KtSetFieldElement
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.transform.IrWrapper
import io.github.kshulzh.kefir.transform.context.KtTransformContext
import org.jetbrains.kotlin.ir.expressions.IrSetField

class KtIrSetFieldElement(
    override val irElement: IrSetField,
    var transformContext: KtTransformContext,
    override var parent: KtElement? = null,
) : KtSetFieldElement, IrWrapper<IrSetField> {
    override var field: KtFieldElement
        get() = TODO("Not yet implemented")
        set(value) {}
    override var receiver: KtExpressionElement?
        get() = TODO("Not yet implemented")
        set(value) {}
    override var value: KtExpressionElement?
        get() = TODO("Not yet implemented")
        set(value) {}
    override var type: KtTypeElement?
        get() = TODO("Not yet implemented")
        set(value) {}

    override val annotations: MutableList<KtAnnotationElement> = mutableListOf()
}