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

package io.github.kshulzh.kefir.model.utils

import io.github.kshulzh.kefir.model.api.arg.KtParameterElement
import io.github.kshulzh.kefir.model.api.arg.KtParametersScope
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement


/**
 * Resolves a parameter by its name within the context of the current `KtExpressionElement`.
 * This method navigates through the parent hierarchy to locate the parameter.
 *
 * @param name the name of the parameter to be resolved
 * @return the resolved `KtParameterElement` if the parameter is found, or null otherwise
 */
fun KtExpressionElement.resolveParam(name: String): KtParameterElement? {
    val parent = this.parent
    return when (parent) {
        is KtExpressionElement -> parent.resolveParam(name)
        is KtParametersScope -> parent.resolveParam(name)
        else -> null
    }
}

/**
 * Resolves a parameter by its name within the scope of `KtParametersScope`.
 *
 * This method searches the list of parameters in the current scope and returns
 * the parameter element whose name matches the provided argument.
 *
 * @param name the name of the parameter to find within the scope
 * @return the `KtParameterElement` associated with the provided name, or `null`
 * if no such parameter exists
 */
fun KtParametersScope.resolveParam(name: String): KtParameterElement? {
    return parameters.find { it.name == name }
}