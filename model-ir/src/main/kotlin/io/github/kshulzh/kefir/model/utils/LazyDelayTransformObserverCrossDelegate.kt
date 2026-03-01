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
import io.github.kshulzh.kefir.transform.context.KtNodeStack
import io.github.kshulzh.kefir.transform.utils.submit
import io.github.kshulzh.problemgraph.context.ProblemContext
import org.jetbrains.kotlin.backend.common.pop
import org.jetbrains.kotlin.backend.common.push
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class LazyDelayTransformObserverCrossDelegate<O : KtElement, P : KtElement, C, I>(
    val transformContext: C,
    val problemContext: ProblemContext,
    val getter: (P) -> O?,
    val setter: (P, O?) -> Unit,
    val copy: P.() -> P,
    var initializer: (() -> P?)? = null,
    var onSet: (C.(P?) -> I?)? = null,
) : ReadWriteProperty<O, P?> {
    val lazy by lazy {
        @Suppress("UNCHECKED_CAST")
        problemContext.submit {
            if (!thisRef.linkCheck()) {
                //unlinked
                return@submit
            }
            if (transformContext is KtNodeStack) {
                transformContext.nodeStack.push(this)
                try {
                    onSet?.invoke(transformContext, this@LazyDelayTransformObserverCrossDelegate.value as P)
                } finally {
                    transformContext.nodeStack.pop()
                }
            } else {
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

    lateinit var thisRef: O
    override fun setValue(thisRef: O, property: KProperty<*>, value: P?) {
        if (this.value === value) {
            return
        }
        this.thisRef = thisRef

        @Suppress("UNCHECKED_CAST")
        val oldValue = this.value as? P?
        oldValue?.let {
            if (getter(it) === thisRef) {
                setter(it, null)
            }
        }
        value?.also {
            val prevOwner = getter(it)
            val v = if (prevOwner !== null) {
                it.copy()
            } else {
                it
            }
            this.value = v
            setter(v, thisRef)
        }

        lazy
    }

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: O, property: KProperty<*>): P? {
        this.thisRef = thisRef
        return value as? P?
    }
}