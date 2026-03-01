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

import io.github.kshulzh.kefir.model.api.utils.ObserverMutableSet
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * A delegate class to manage a mutable set of elements with the capability to observe and
 * synchronize changes between the set and its reference object.
 *
 * @param O The type of the reference object that owns this delegate.
 * @param P The type of elements in the mutable set.
 *
 * @property value The initial mutable set of elements managed by this delegate.
 * @property copy A function that creates a copy of an element. Used when the element is already associated with another reference object.
 * @property getter A function to retrieve the reference object (of type [O]) associated with a given element.
 * @property setter A function to associate or dissociate the reference object with an element.
 */
class ObserverCrossSetDelegate<O, P>(
    var value: MutableSet<P>,
    val copy: P.() -> P,
    val getter: (P) -> O?,
    val setter: (P, O?) -> Unit,
) : ReadWriteProperty<O, MutableSet<P>> {
    /**
     * A reference to the current instance of the type `O` where the property delegate is being used.
     * This variable helps maintain and update state with respect to the associated object.
     *
     * It is updated whenever the property is accessed or modified through the delegate, ensuring
     * the delegate remains aware of the associated object (`thisRef`).
     *
     * The reference is set to `null` initially and gets assigned the enclosing object during
     * property access or modification.
     */
    var thisRef: O? = null

    /**
     * Retrieves the current value associated with the given property and reference, ensuring
     * the internal value is wrapped and properly associated with the provided reference if needed.
     *
     * @param thisRef the reference to the object for which the value is being retrieved
     * @param property the metadata for the property being accessed
     * @return the current wrapped value of the property, ensuring its consistency with the reference
     */
    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: O, property: KProperty<*>): MutableSet<P> {
        if (this.thisRef != thisRef) {
            this.value = wrap(value)
            this.thisRef = thisRef
        }

        return value
    }

    /**
     * Updates the value of the property and manages the side effects for each element in the set.
     *
     * @param thisRef The instance of the owner class where the property is delegated.
     * @param property The metadata of the property being set.
     * @param value The new value to set for the property, represented as a mutable set.
     */
    override fun setValue(thisRef: O, property: KProperty<*>, value: MutableSet<P>) {
        if (this.value == value) return
        this.thisRef = thisRef

        this.value.forEach {
            setter(it, null)
        }

        this.value = wrap(value)
    }

    /**
     * Creates a wrapped version of the given `MutableSet`. The wrapping involves creating an
     * `ObserverMutableSet` that adds behavior for observing and modifying elements during
     * additions and deletions through provided callbacks.
     *
     * @param list The original `MutableSet` to be wrapped. If it is already an instance of `ObserverMutableSet`,
     *             the method ensures its internal state is properly managed and wrapped again.
     * @return A newly created `ObserverMutableSet` that wraps the original set with the appropriate
     *         callbacks applied.
     */
    fun wrap(list: MutableSet<P>): MutableSet<P> {
        return if (list is ObserverMutableSet<*>) {
            ObserverMutableSet(
                list.toMutableSet(),
                { e -> (if (getter(e) != null) e.copy() else e).also { setter(it, thisRef) } },
                { e -> setter(e, null) },
            )
        } else {
            ObserverMutableSet(
                list,
                { e -> (if (getter(e) != null) e.copy() else e).also { setter(it, thisRef) } },
                { e -> setter(e, null) },
            )
        }
    }
}