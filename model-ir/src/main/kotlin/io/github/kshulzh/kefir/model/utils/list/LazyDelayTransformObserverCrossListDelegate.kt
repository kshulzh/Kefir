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

package io.github.kshulzh.kefir.model.utils.list

import io.github.kshulzh.kefir.model.api.KtElement
import io.github.kshulzh.kefir.model.api.utils.ObserverMutableList
import io.github.kshulzh.kefir.model.utils.linkCheck
import io.github.kshulzh.kefir.transform.context.KtNodeStack
import io.github.kshulzh.kefir.transform.utils.submit
import io.github.kshulzh.problemgraph.context.ProblemContext
import org.jetbrains.kotlin.backend.common.pop
import org.jetbrains.kotlin.backend.common.push
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class LazyDelayTransformObserverCrossListDelegate<O : KtElement, P : KtElement, C, I>(
    val transformContext: C,
    var initializer: (() -> List<P>)? = null,
    val problemContext: ProblemContext,
    //val transformer: (I)->P?,
    val getter: (P) -> O?,
    val copy: P.() -> P,
    val setter: (P, O?) -> Unit,
    val onDelete: C.(P, Int) -> Unit = { _, _ -> },
    val onAdd: C.(P, Int) -> I?,
) : ReadWriteProperty<O, MutableList<P>> {
    lateinit var initial: MutableList<P>
    val lazy by lazy {
        problemContext.submit {
            var added = 0
            var isOld = true
            problemContext.submit {
                if (!thisRef.linkCheck()) {
                    //unlinked
                    return@submit
                }
                if (transformContext is KtNodeStack) {
                    transformContext.nodeStack.push(this)
                    try {
                        //todo retry a mechanism in case when something went wrong
                        if (isOld) {
                            initial.forEach { t -> transformContext.onDelete(t, 0) }
                            isOld = false
                        }
                        for (i in added..<initial.size) {
                            initial[i].let { transformContext.onAdd(it, -1) }
                            added++
                        }

                    } finally {
                        transformContext.nodeStack.pop()
                    }
                }
            }
        }
    }
    lateinit var list: MutableList<P>
    lateinit var thisRef: O
    override fun getValue(thisRef: O, property: KProperty<*>): MutableList<P> {
        if (initializer != null) {
            this.thisRef = thisRef
            val value = initializer?.invoke()?.toMutableList() ?: mutableListOf()
            initial = value
            initializer = null
            list = wrap(value.also { element -> element.forEach { setter(it, thisRef) } }.toMutableList(), thisRef)
        }
        return list
    }

    override fun setValue(thisRef: O, property: KProperty<*>, value: MutableList<P>) {
        if (initializer != null) {
            this.thisRef = thisRef
            val valu = initializer?.invoke()?.toMutableList() ?: mutableListOf()
            initial = valu
            initializer = null
            list = wrap(value.also { element -> element.forEach { setter(it, thisRef) } }.toMutableList(), thisRef)
        } else {
            list.forEach { setter(it, null) }
            list = wrap(value.also { element -> element.forEach { setter(it, thisRef) } }.toMutableList(), thisRef)
        }
    }

    fun wrap(elements: MutableList<P>, thisRef: O): MutableList<P> {
        return ObserverMutableList(
            elements, { p, i ->
                lazy
                if (getter(p) != null) {
                    p.copy().also { setter(it, thisRef) }
                } else {
                    p.also { setter(it, thisRef) }
                }
            }, { p, i ->
                lazy
                setter(p, null)
            }
        )
    }
}