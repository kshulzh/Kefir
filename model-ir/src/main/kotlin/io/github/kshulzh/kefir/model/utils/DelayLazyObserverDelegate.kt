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

import io.github.kshulzh.kefir.transform.context.KtNodeStack
import io.github.kshulzh.kefir.transform.utils.submit
import io.github.kshulzh.problemgraph.context.ProblemContext
import org.jetbrains.kotlin.backend.common.pop
import org.jetbrains.kotlin.backend.common.push
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class DelayLazyObserverDelegate<O, P, C, I>(
    val problemContext: ProblemContext,
    val transformContext: C,
    var initializer: (() -> P?)? = null,
    var onSet: (C.(P?) -> I?)? = null,
) : ReadWriteProperty<O, P?> {
    val lazy by lazy {
        problemContext.submit {
            if (transformContext is KtNodeStack) {
                transformContext.nodeStack.push(this)
                try {
                    @Suppress("UNCHECKED_CAST")
                    onSet?.invoke(transformContext, this@DelayLazyObserverDelegate.value as P)
                } finally {
                    transformContext.nodeStack.pop()
                }
            } else {
                @Suppress("UNCHECKED_CAST")
                onSet?.invoke(transformContext, value as P)
            }
        }
    }

    object UNDEF

    @Suppress("UNCHECKED_CAST")
    var value: Any? = UNDEF
        get() = if (field == UNDEF) {
            field = initializer?.invoke()
            initializer = null
            field
        } else {
            field
        }
        set(value) {
            field = value

            if (initializer != null) {
                initializer = null
            }
        }


    override fun getValue(thisRef: O, property: KProperty<*>): P? {
        @Suppress("UNCHECKED_CAST")
        return value as P?
    }

    override fun setValue(thisRef: O, property: KProperty<*>, value: P?) {
        this.value = value
        lazy
    }
}