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

@file:Suppress("FunctionName")

package io.github.kshulzh.kefir.builder.expression

import io.github.kshulzh.kefir.model.api.expression.KtBlockElement
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.statement.KtStatementsScope
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.expression.KtGetValueElementImpl
import io.github.kshulzh.kefir.model.utils.resolveParam

/**
 * Declares or retrieves a variable within the current scope.
 *
 * @param name the name of the variable to declare or find
 * @param type the type of the variable, if specified; defaults to null
 * @param value the initial value of the variable, if specified; defaults to null
 * @return a `KtGetValueElementImpl` instance representing the variable if it is found or declared successfully
 * @throws RuntimeException if the variable cannot be found or declared within the current scope
 */
fun KtStatementsScope.Variable(
    name: String,
    type: KtTypeElement? = null,
    value: KtExpressionElement? = null,
): KtGetValueElementImpl {
    if (type == null && value == null) {
        val block = this as? KtBlockElement
        block?.resolveParam(name)?.let { return KtGetValueElementImpl(it, type, parent = this) }
    }
    //todo declare or find variable
    throw RuntimeException("Can't find parameter $name")
}



