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
import io.github.kshulzh.kefir.model.api.utils.ObserverMutableSet
import io.github.kshulzh.kefir.model.utils.linkCheck
import io.github.kshulzh.kefir.transform.context.KtNodeStack
import io.github.kshulzh.kefir.transform.utils.submit
import io.github.kshulzh.problemgraph.context.ProblemContext
import org.jetbrains.kotlin.backend.common.pop
import org.jetbrains.kotlin.backend.common.push
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class LazyDelayTransformObserverCrossSetDelegate<O : KtElement, P : KtElement, C, I>(
    val transformContext: C,
    var initializer: (() -> Set<P>)? = null,
    val problemContext: ProblemContext,
    //val transformer: (I)->P?,
    val getter: (P) -> O?,
    val copy: P.() -> P,
    val setter: (P, O?) -> Unit,
    val onDelete: C.(P) -> Unit = { _ -> },
    val onAdd: C.(P) -> I?,
) : ReadWriteProperty<O, MutableSet<P>> {
    lateinit var initial: MutableSet<P>
    val lazy by lazy {
        problemContext.submit {
            val toDelete = (initial - list).toMutableSet()
            var toAdd = (list - initial).toMutableSet()
            problemContext.submit {
                if (!thisRef.linkCheck()) {
                    //unlinked
                    return@submit
                }
                val deleted = mutableSetOf<P>()
                val added = mutableSetOf<P>()

                if (transformContext is KtNodeStack) {
                    transformContext.nodeStack.push(this)
                    try {
                        //todo retry a mechanism in case when something went wrong
                        if (toDelete.isNotEmpty()) {
                            initial.forEach { t ->
                                transformContext.onDelete(t)
                                deleted.add(t)
                            }
                        }
                        toAdd.forEach { t ->
                            transformContext.onAdd(t)
                            added.add(t)
                        }

                    } finally {
                        toDelete.removeAll(deleted)
                        toAdd.removeAll(added)
                        transformContext.nodeStack.pop()
                    }
                }
            }
        }
    }
    lateinit var list: MutableSet<P>
    lateinit var thisRef: O
    override fun getValue(thisRef: O, property: KProperty<*>): MutableSet<P> {
        if (initializer != null) {
            this.thisRef = thisRef
            val value = initializer?.invoke()?.toMutableSet() ?: mutableSetOf()
            initial = value
            initializer = null
            list = wrap(value.also { element -> element.forEach { setter(it, thisRef) } }.toMutableSet(), thisRef)
        }
        return list
    }

    override fun setValue(thisRef: O, property: KProperty<*>, value: MutableSet<P>) {
        if (initializer != null) {
            this.thisRef = thisRef
            val valu = initializer?.invoke()?.toMutableSet() ?: mutableSetOf()
            initial = valu
            initializer = null
            list = wrap(value.also { element -> element.forEach { setter(it, thisRef) } }.toMutableSet(), thisRef)
        } else {
            list.forEach { setter(it, null) }
            list = wrap(value.also { element -> element.forEach { setter(it, thisRef) } }.toMutableSet(), thisRef)
        }
    }

    fun wrap(elements: MutableSet<P>, thisRef: O): MutableSet<P> {
        return ObserverMutableSet(
            elements, { p ->
                lazy
                if (getter(p) != null) {
                    p.copy().also { setter(it, thisRef) }
                } else {
                    p.also { setter(it, thisRef) }
                }
            }, { p ->
                lazy
                setter(p, null)
            }
        )
    }
}