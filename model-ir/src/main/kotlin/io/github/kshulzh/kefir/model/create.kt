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

package io.github.kshulzh.kefir.model

import io.github.kshulzh.kefir.model.api.utils.ObserverMutableList
import io.github.kshulzh.kefir.model.ir.utils.ObserverDelegate
import io.github.kshulzh.kefir.transform.context.KtIrLocalTransformContext
import io.github.kshulzh.kefir.transform.context.KtIrLocalTransformContext1
import io.github.kshulzh.kefir.transform.context.KtIrLocalTransformContext2
import io.github.kshulzh.kefir.transform.context.KtTransformContext
import io.github.kshulzh.kefir.transform.utils.submit
import org.jetbrains.kotlin.backend.common.pop
import org.jetbrains.kotlin.backend.common.push
import kotlin.properties.ReadWriteProperty

fun <T> createLazy(
    initializer: () -> T,
    onSet: (T) -> Unit,
): ReadWriteProperty<Any?, T> {
    return if (true) {
        //todo
        ObserverDelegate({ Any() }, initializer) {
            onSet(it)
        }
    } else {
        ObserverDelegate({ Any() }, initializer) {
            onSet(it)
        }
    }
}

fun <T> createLazyIrList(
    transformContext: KtTransformContext,
    initializer: () -> List<T>,
    onAdd: KtIrLocalTransformContext.(T, Int) -> Unit,
    onDelete: KtIrLocalTransformContext.(T, Int) -> Unit,
): ReadWriteProperty<Any?, MutableList<T>> {
    val problemContext = transformContext.problemContext
    return if (problemContext != null) {
        //todo
        val initializer1 = initializer()
        var initial = initializer1.toMutableList()
        var isOld = true
        val lazy by lazy {
            var added = 0
            problemContext.submit {
                val tt = KtIrLocalTransformContext1(transformContext)
                tt.nodeStack.push(this)
                try {
                    //todo retry a mechanism in case when something went wrong
                    if (isOld) {
                        initializer1.forEach { t -> tt.onDelete(t, 0) }
                        isOld = false
                    }
                    for (i in added..<initial.size) {
                        initial[i].let { tt.onAdd(it, -1) }
                        added++
                    }

                } finally {
                    tt.nodeStack.pop()
                }
            }
        }

        val observerMutableList = ObserverMutableList(initial, onAdd = { e, i ->
            lazy
            null
        }, onDelete = { e, i ->
            lazy
        })
        ObserverDelegate(KtIrLocalTransformContext2(transformContext), { observerMutableList }) {
            initial = it
            lazy
        }
    } else {
        val transformContext1 = KtIrLocalTransformContext2(transformContext)
        val initial = initializer().toMutableList()
        val observerMutableList = ObserverMutableList(initial, onAdd = { e, i ->
            transformContext1.onAdd(e, i)
            null
        }, onDelete = { e, i ->
            transformContext1.onDelete(e, i)
        })
        ObserverDelegate(transformContext1, {
            observerMutableList
        }) {
            observerMutableList.clear()
            observerMutableList.addAll(it)
        }
    }
}