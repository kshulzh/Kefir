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

package io.github.kshulzh.kefir.model.ir.utils


@Suppress("JavaDefaultMethodsNotOverriddenByDelegation")
class DifMutableSet<T>(
    val initial: MutableSet<T> = mutableSetOf(),
) : MutableSet<T> by initial {

    val removed = mutableSetOf<T>()
    val added = mutableSetOf<T>()

    override fun add(element: T): Boolean {
        addElement(element)
        return initial.add(element)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        elements.forEach {
            addElement(it)
        }
        return initial.addAll(elements)
    }

    override fun clear() {
        initial.forEach {
            removeElement(it)
        }
        initial.clear()
    }

    override fun remove(element: T): Boolean {
        removeElement(element)
        return initial.remove(element)
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        elements.forEach { removeElement(it) }
        return initial.removeAll(elements)
    }


    fun addElement(element: T) {
        added.add(element)
        removed.remove(element)
    }

    fun removeElement(element: T) {
        added.remove(element)
        removed.add(element)
    }
}