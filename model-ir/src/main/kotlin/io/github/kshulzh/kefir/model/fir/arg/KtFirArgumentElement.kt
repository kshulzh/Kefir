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

package io.github.kshulzh.kefir.model.fir.arg

import io.github.kshulzh.kefir.model.api.KtName
import io.github.kshulzh.kefir.model.api.arg.KtArgumentElement
import io.github.kshulzh.kefir.model.api.arg.KtArgumentsScope
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.transform.FirWrapper
import io.github.kshulzh.kefir.transform.context.KtFirTransformContext
import org.jetbrains.kotlin.fir.expressions.FirExpression

class KtFirArgumentElement(
    override val firElement: FirExpression,
    override var name: KtName?,
    var transformFirContext: KtFirTransformContext,
    override var position: Int = -1,
    override var argumentsScope: KtArgumentsScope? = null,
) : KtArgumentElement, FirWrapper<FirExpression> {
    override var value: KtExpressionElement?
        get() = TODO("Not yet implemented")
        set(value) {}
}