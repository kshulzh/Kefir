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

package io.github.kshulzh.kefir.transform.utils

import io.github.kshulzh.kefir.ir.helper.IrInitElement
import io.github.kshulzh.kefir.transform.context.KtTransformContext

/**
 * Initializes the element by invoking its initializer function using the current transformation context.
 *
 * The initializer function is called with the IR module fragment and the plugin context available
 * in the provided transformation context.
 *
 * @return The initialized element of type [T].
 */
context(c: KtTransformContext)
fun <T> IrInitElement<T>.initialize(): T {
    return initializer(c.moduleFragment, c.pluginContext)
}