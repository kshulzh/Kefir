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

package io.github.kshulzh.kefir.model.ir.utils

import io.github.kshulzh.kefir.transform.context.KtNodeStack
import io.github.kshulzh.kefir.transform.utils.submit
import io.github.kshulzh.problemgraph.context.ProblemContext
import org.jetbrains.kotlin.backend.common.pop
import org.jetbrains.kotlin.backend.common.push
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class DelayObserverListDelegate<T, V>(
    val problemContext: ProblemContext,
    val transformContext: V,
    var initializer: (() -> T)? = null,
    var onSet: (V.(T) -> Unit)? = null,
) : ReadWriteProperty<Any?, T> {

    object UNDEF

    @Suppress("UNCHECKED_CAST")
    var value: Any? = UNDEF
        get() = if (initializer != null) {
            field = initializer!!()
            initializer = null
            field
        } else {
            field
        }
        set(value) {
            field = value
            if (initializer != null) {
                initializer = null
                problemContext.submit {
                    if (transformContext is KtNodeStack) {
                        transformContext.nodeStack.push(this)
                        try {
                            onSet?.invoke(transformContext, this@DelayObserverListDelegate.value as T)
                        } finally {
                            transformContext.nodeStack.pop()
                        }
                    } else {
                        onSet?.invoke(transformContext, value as T)
                    }
                }
            }
        }

    @Suppress("UNCHECKED_CAST")
    override operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return if (initializer != null) {
            value = initializer!!()
            initializer = null
            value as T
        } else value as T
    }

    override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }
}