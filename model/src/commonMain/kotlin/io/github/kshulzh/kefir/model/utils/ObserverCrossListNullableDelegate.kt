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

package io.github.kshulzh.kefir.model.utils

import io.github.kshulzh.kefir.model.api.utils.ObserverMutableList
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * A delegate class that manages a mutable list of nullable items, enabling observation and manipulation
 * through getter and setter functions tied to a reference object.
 *
 * This class allows integration between list manipulation and interaction with a reference object.
 * It observes changes to the list and delegates handling operations to defined getter and setter functions.
 * Additionally, the delegate ensures that elements in the list are properly synchronized with the reference.
 *
 * @param value The initial mutable list of nullable elements to be managed.
 * @param copy A function that creates a copy of an element in the list.
 * @param getter A function that defines how to retrieve a reference from a non-null element.
 * @param setter A function that defines how to update a reference for a non-null element.
 */
class ObserverCrossListNullableDelegate<O, P>(
    var value: MutableList<P?>,
    val copy: P.() -> P,
    val getter: (P) -> O?,
    val setter: (P, O?) -> Unit,
) : ReadWriteProperty<O, MutableList<P?>> {
    /**
     * Holds a reference to the current receiver object (`this`) during property delegate calls.
     *
     * Used internally to track the object associated with the property being accessed or modified.
     * This variable is updated whenever a `getValue` or `setValue` call is made to ensure it reflects
     * the latest receiver object.
     */
    var thisRef: O? = null

    /**
     * Retrieves the current value of the property as a mutable list.
     * If the reference to the owning object (`thisRef`) has changed, the list is wrapped
     * to apply additional behavior or transformations.
     *
     * @param thisRef The reference to the object owning the delegated property.
     * @param property The metadata of the property being accessed.
     * @return A mutable list containing the current values, potentially wrapped for observing or modification behavior.
     */
    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: O, property: KProperty<*>): MutableList<P?> {
        if (this.thisRef != thisRef) {
            this.value = wrap(value)
            this.thisRef = thisRef
        }

        return value
    }

    /**
     * Sets the value of the delegate's property and updates the internal state.
     *
     * This method updates the observed list by applying the setter function to each element
     * in the current list with a null reference, clears the existing list, applies the
     * transformation defined in `wrap`, and sets the new value.
     *
     * @param thisRef The object for which the property is delegated.
     * @param property The metadata for the property being delegated.
     * @param value The new mutable list of nullable elements to be set to the property.
     */
    override fun setValue(thisRef: O, property: KProperty<*>, value: MutableList<P?>) {
        if (this.value == value) return
        this.thisRef = thisRef

        this.value.forEach {
            if (it != null) setter(it, null)
        }

        this.value = wrap(value)
    }

    /**
     * Wraps a given mutable list into an `ObserverMutableList`. This enables observation
     * and customization of element addition and removal within the list.
     *
     * If the provided list is already an instance of `ObserverMutableList`, it is cloned into
     * a new `ObserverMutableList`. Otherwise, the original list is wrapped in a new `ObserverMutableList`.
     *
     * @param list the list to be wrapped into an `ObserverMutableList`
     * @return a new `ObserverMutableList` instance wrapping the given list
     */
    fun wrap(list: MutableList<P?>): MutableList<P?> {
        return if (list is ObserverMutableList<*>) {
            ObserverMutableList(
                list.toMutableList(),
                { e, _ ->
                    if (e != null) (if (getter(e) != null) e.copy() else e).also {
                        setter(
                            it,
                            thisRef
                        )
                    } else null
                },
                { e, _ -> if (e != null) setter(e, null) },
            )
        } else {
            ObserverMutableList(
                list,
                { e, _ ->
                    if (e != null) (if (getter(e) != null) e.copy() else e).also {
                        setter(
                            it,
                            thisRef
                        )
                    } else null
                },
                { e, _ -> if (e != null) setter(e, null) },
            )
        }
    }
}