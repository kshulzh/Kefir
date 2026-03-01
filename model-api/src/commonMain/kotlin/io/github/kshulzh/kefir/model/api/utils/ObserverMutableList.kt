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
 * A specialized implementation of a mutable list that allows observing and modifying elements during
 * addition or deletion operations.
 *
 * This class delegates its operations to an internal mutable list while applying `onAdd` and
 * `onDelete` callbacks during addition or deletion of elements respectively.
 *
 * @param T The type of elements contained in the list.
 * @property initial The backing `MutableList` instance that holds the data.
 * @property onAdd A callback function invoked whenever an element is added to the list. It receives
 * the element being added and its position, and allows modification of the added element. Returning
 * `null` from this callback prevents modifications to the element.
 * @property onDelete A callback function invoked whenever an element is removed from the list. It
 * receives the element being removed and its position.
 */
@Suppress("JavaDefaultMethodsNotOverriddenByDelegation")
class ObserverMutableList<T>(
    val initial: MutableList<T> = mutableListOf(),
    val onAdd: (T, Int) -> T? = { _, _ -> null },
    val onDelete: (T, Int) -> Unit = { _, _ -> }
) : MutableList<T> by initial {
    /**
     * Adds the specified element to this list, invoking the `onAdd` callback before the addition.
     *
     * @param element the element to be added to the list
     * @return `true` if the list is modified as a result of this operation; `false` otherwise
     */
    override fun add(element: T): Boolean {
        onAdd(element, -1)
        return initial.add(element)
    }

    /**
     * Inserts the specified element at the specified position in the list.
     * The position index can be overridden with the provided `onAdd` function, which may transform or replace the element.
     * If `onAdd` returns null, the original element is added.
     *
     * @param index The position at which to insert the specified element.
     * @param element The element to be inserted into the list.
     */
    override fun add(index: Int, element: T) {
        initial.add(index, onAdd(element, index) ?: element)
    }

    /**
     * Adds all elements from the specified collection to this list. Each element is passed through the `onAdd` callback
     * before being added. If the callback returns a non-null value, the returned value is added instead of the original element.
     *
     * @param elements The collection of elements to be added to the list.
     * @return `true` if the list was modified as a result of the operation.
     */
    override fun addAll(elements: Collection<T>): Boolean {
        return initial.addAll(elements.mapIndexed { i, e -> onAdd(e, -1) ?: e })
    }

    /**
     * Adds all elements from the specified collection at the specified index in the list.
     * Each element is optionally transformed by the `onAdd` function before being added.
     *
     * @param index The index at which elements should be inserted.
     * @param elements The collection of elements to be added to the list.
     * @return `true` if the list was modified as a result of this operation, otherwise `false`.
     */
    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        return initial.addAll(index, elements.mapIndexed { i, e -> onAdd(e, index) ?: e })
    }

    /**
     * Removes all elements from the list, invoking the `onDelete` callback for each element before
     * clearing the internal list.
     *
     * The `onDelete` callback receives the element being removed and an index of `-1`.
     */
    override fun clear() {
        initial.forEach { onDelete(it, -1) }
        initial.clear()
    }

    /**
     * Removes the specified element from the list and performs the `onDelete` operation.
     *
     * @param element The element to be removed from the list.
     * @return `true` if the element was successfully removed, `false` otherwise.
     */
    override fun remove(element: T): Boolean {
        onDelete(element, -2)
        return initial.remove(element)
    }

    /**
     * Removes all elements in the specified collection from this list and invokes the delete callback for each removed element.
     *
     * @param elements the collection of elements to be removed from this list
     * @return `true` if the list was modified as a result of this operation, `false` otherwise
     */
    override fun removeAll(elements: Collection<T>): Boolean {
        elements.forEach { onDelete(it, -2) }
        return initial.removeAll(elements)
    }

    /**
     * Removes the element at the specified position in the list and triggers the `onDelete` callback
     * with the removed element and its index.
     *
     * @param index The position of the element to be removed.
     * @return The element that was removed from the list.
     */
    override fun removeAt(index: Int): T {
        return initial.removeAt(index).also { onDelete(it, index) }
    }

//    /**
//     * Adds the specified element at the beginning of the list after applying the `onAdd` transformation.
//     *
//     * @param e the element to be added at the beginning of the list
//     */
//    override fun addFirst(e: T) {
//        super.addFirst(onAdd(e, 0) ?: e)
//    }
//
//    /**
//     * Adds the specified element to the end of this list. If a transformation is applied
//     * by the `onAdd` function and it returns a non-null result, the transformed element
//     * will be added instead. Otherwise, the original element is added.
//     *
//     * @param e the element to be added to the end of the list
//     */
//    override fun addLast(e: T) {
//        super.addLast(onAdd(e, -1) ?: e)
//    }
//
//    /**
//     * Removes the first element from the list and invokes the onDelete callback with the removed element.
//     *
//     * @return The first element that was removed from the list.
//     */
//    override fun removeFirst(): T {
//        onDelete(initial.first(), 0)
//        return super.removeFirst()
//    }
//
//    /**
//     * Removes the last element from the list and triggers the `onDelete` callback with the element and its position.
//     *
//     * @return The last element that was removed.
//     */
//    override fun removeLast(): T {
//        onDelete(initial.last(), -1)
//        return super.removeLast()
//    }
}