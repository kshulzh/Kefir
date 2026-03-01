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

package io.github.kshulzh.kefir.model.api.utils

/**
 * A specialized implementation of a mutable set that allows observing and modifying elements during
 * addition or deletion operations. This class delegates its operations to an internal mutable set
 * while applying `onAdd` and `onDelete` callbacks during modifications.
 *
 * @param T The type of elements contained in the set.
 * @property initial The backing `MutableSet` instance that holds the data.
 * @property onAdd A callback function invoked whenever an element is added to the set. It receives
 * the element being added and allows modification of the added element. Returning `null` from this
 * callback prevents modifications to the element.
 * @property onDelete A callback function invoked whenever an element is removed from the set. It
 * receives the element being removed.
 */
@Suppress("JavaDefaultMethodsNotOverriddenByDelegation")
class ObserverMutableSet<T>(
    val initial: MutableSet<T> = mutableSetOf(),
    val onAdd: (T) -> T? = { null },
    val onDelete: (T) -> Unit = {}
) : MutableSet<T> by initial {
    /**
     * Adds the specified element to this set after applying the `onAdd` transformation.
     * If the transformation returns a non-null result, the transformed element is added instead.
     * Otherwise, the original element is added.
     *
     * @param element the element to be added to the set
     * @return `true` if the set is modified as a result of this operation; `false` otherwise
     */
    override fun add(element: T): Boolean {
        return initial.add(onAdd(element) ?: element)
    }

    /**
     * Adds all elements from the specified collection to this set. Each element is processed
     * through the `onAdd` function before being added. If `onAdd` returns a non-null value,
     * that value is added to the set instead of the original element. If `onAdd` returns `null`,
     * the original element is added.
     *
     * @param elements the collection of elements to be added to the set
     * @return `true` if the set was modified as a result of this operation, `false` otherwise
     */
    override fun addAll(elements: Collection<T>): Boolean {
        return initial.addAll(elements.map { onAdd(it) ?: it })
    }


    /**
     * Removes all elements from the set and invokes the `onDelete` callback for each element before clearing.
     *
     * The `onDelete` callback is triggered for every element currently in the set, allowing actions to be performed
     * before the set is cleared. Once all elements have been processed by the callback, the backing set is completely emptied.
     */
    override fun clear() {
        initial.forEach { onDelete(it) }
        initial.clear()
    }

    /**
     * Removes the specified element from this set, triggering the `onDelete` callback before
     * removing the element from the underlying set.
     *
     * @param element The element to be removed from the set.
     * @return `true` if the element was successfully removed from the set, `false` otherwise.
     */
    override fun remove(element: T): Boolean {
        onDelete(element)
        return initial.remove(element)
    }

    /**
     * Removes all elements in the specified collection from this set and invokes the `onDelete` callback for each removed element.
     *
     * @param elements the collection of elements to be removed from this set
     * @return `true` if the set was modified as a result of this operation, `false` otherwise
     */
    override fun removeAll(elements: Collection<T>): Boolean {
        elements.forEach { onDelete(it) }
        return initial.removeAll(elements)
    }
}