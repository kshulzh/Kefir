/*
 * Copyright (c) 2025. Kirill Shulzhenko
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

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ObserverDelegate<T, V>(
    val transformContext: V,
    var initializer: (() -> T)? = null,
    var onSet: (V.(T) -> Unit)? = null
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
            onSet?.invoke(transformContext, value as T)
            field = value
            if (initializer != null) {
                initializer = null
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