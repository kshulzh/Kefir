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
 * A delegate class that synchronizes a mutable list with its observed object reference.
 * This class provides the ability to wrap the list with observer functionality, allowing
 * for controlled changes to the list elements and their association with a reference object.
 *
 * @param O The type of the observed object reference.
 * @param P The type of the list elements.
 * @property value The initial list to be synchronized and observed.
 * @property copy A lambda function that creates a copy of an individual list element.
 * @property getter A lambda function that retrieves the associated observed object from a list element.
 * @property setter A lambda function that sets the observed object reference in a list element.
 */
class ObserverCrossListDelegate<O, P>(
    var value: MutableList<P>,
    val copy: P.() -> P,
    val getter: (P) -> O?,
    val setter: (P, O?) -> Unit,
) : ReadWriteProperty<O, MutableList<P>> {
    /**
     * Holds a reference to the object (`thisRef`) that uses the delegate. This property is
     * used internally to manage state and interactions between the delegate and its owning object.
     *
     * It includes logic to ensure the reference is updated and synchronized whenever the associated
     * delegate function (`getValue` or `setValue`) is invoked. The reference is updated only when
     * it differs from the current value.
     *
     * This property is nullable, to account for initialization or cases where the reference
     * might not be set.
     */
    var thisRef: O? = null

    /**
     * Retrieves the value of the observed property, ensuring it is properly wrapped and associated
     * with the provided `thisRef` object. If the reference object changes, the current value list is
     * re-wrapped.
     *
     * @param thisRef The reference object that owns the property.
     * @param property Metadata about the property being accessed.
     * @return The wrapped mutable list of type `P`.
     */
    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: O, property: KProperty<*>): MutableList<P> {
        if (this.thisRef != thisRef) {
            this.value = wrap(value)
            this.thisRef = thisRef
        }

        return value
    }

    /**
     * Sets the value of the observed mutable list property and updates its state.
     * If the new value is the same as the current value, no changes are made.
     * Otherwise, all elements in the current list have their associated state cleared
     * through the specified `setter` function, and the new list is wrapped for observation.
     *
     * @param thisRef The reference to the owner of the property.
     * @param property The property being delegated.
     * @param value The new value to be set for the delegated property.
     */
    override fun setValue(thisRef: O, property: KProperty<*>, value: MutableList<P>) {
        if (this.value == value) return
        this.thisRef = thisRef

        this.value.forEach {
            setter(it, null)
        }

        this.value = wrap(value)
    }

    /**
     * Produces a wrapped instance of a mutable list, either by copying the provided list or directly
     * creating an observable `ObserverMutableList`. The wrapping allows observing and modifying list
     * elements during addition or deletion operations, with transformations applied based on certain
     * conditions.
     *
     * @param list the mutable list to be wrapped
     * @return a new wrapped `MutableList` instance, which is an `ObserverMutableList` that applies
     * transformations and observer callbacks for added and removed elements
     */
    fun wrap(list: MutableList<P>): MutableList<P> {
        return if (list is ObserverMutableList<*>) {
            ObserverMutableList(
                list.toMutableList(),
                { e, _ -> (if (getter(e) != null) e.copy() else e).also { setter(it, thisRef) } },
                { e, _ -> setter(e, null) },
            )
        } else {
            ObserverMutableList(
                list,
                { e, _ -> (if (getter(e) != null) e.copy() else e).also { setter(it, thisRef) } },
                { e, _ -> setter(e, null) },
            )
        }
    }
}