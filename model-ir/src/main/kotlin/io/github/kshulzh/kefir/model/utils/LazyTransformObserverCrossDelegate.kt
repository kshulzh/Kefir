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

import io.github.kshulzh.kefir.model.api.KtElement
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class LazyTransformObserverCrossDelegate<O, P : KtElement, C, I>(
    val transformContext: C,
    val transformer: (I) -> P?,
    var initializer: (() -> P?)? = null,
    val copy: P.() -> P,
    val getter: (P) -> O?,
    val setter: (P, O?) -> Unit,
    var onSet: (C.(P?) -> I?)? = null,
) : ReadWriteProperty<O, P?> {
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

    override fun setValue(thisRef: O, property: KProperty<*>, value: P?) {
        if (this.value === value) {
            return
        }

        @Suppress("UNCHECKED_CAST")
        val oldValue = this.value as? P?
        oldValue?.let {
            if (getter(it) === thisRef) {
                setter(it, null)
            }
        }
        value?.also { value1 ->
            val prevOwner = getter(value1)
            val v = if (prevOwner !== null) {
                value1.copy()
            } else {
                value1
            }
            setter(v, thisRef)

            this.value = onSet?.invoke(transformContext, v)?.let(transformer)?.also { setter(it, thisRef) } ?: v
        }
    }

    override fun getValue(thisRef: O, property: KProperty<*>): P? {
        @Suppress("UNCHECKED_CAST")
        return value as? P?
    }
}