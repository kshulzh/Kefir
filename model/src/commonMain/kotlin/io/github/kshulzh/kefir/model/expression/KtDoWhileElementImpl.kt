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

package io.github.kshulzh.kefir.model.expression

import io.github.kshulzh.kefir.model.api.KtAttributes
import io.github.kshulzh.kefir.model.api.KtElement
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationElement
import io.github.kshulzh.kefir.model.api.expression.KtDoWhileElement
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.type.KtBaseTypes
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.utils.createDelegate
import io.github.kshulzh.kefir.model.utils.createNullableDelegate

/**
 * Implementation of [KtDoWhileElement]; [condition] and [body] are re-parented on construction and on assignment.
 */
class KtDoWhileElementImpl(
    condition: KtExpressionElement,
    body: KtExpressionElement? = null,
    override var label: String? = null,
    override var type: KtTypeElement? = KtBaseTypes.UNIT,
    override val annotations: MutableList<KtAnnotationElement> = mutableListOf(),
    override var parent: KtElement? = null,
    override var attributes: MutableMap<String, Any> = mutableMapOf()
) : KtDoWhileElement, KtAttributes {
    init {
        condition.parent = this
        body?.parent = this
    }

    override var condition: KtExpressionElement by createDelegate(condition, KtExpressionElement::parent)

    override var body: KtExpressionElement? by createNullableDelegate(body, KtExpressionElement::parent)
}
