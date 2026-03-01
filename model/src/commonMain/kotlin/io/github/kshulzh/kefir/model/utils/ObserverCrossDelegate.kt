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

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * A delegate class that allows observing and modifying cross-referenced properties in a bidirectional manner.
 * It provides functionality to manage a property of type `P` within a parent type `O` while applying specific
 * getter and setter logic.
 *
 * This class is primarily useful when:
 * - A property `P` in an object `O` needs to maintain consistency with a related property.
 * - Custom logic is required for copying or modifying the property `P` when necessary.
 *
 * @param O The type of the object that owns the property being delegated.
 * @param P The type of the property being observed and managed.
 * @property value The current value of the property being observed.
 * @property copy A lambda function specifying how the property of type `P` should be copied.
 * @property getter A function used to retrieve the associated object (`O`) from the property (`P`).
 * @property setter A function used to update the property (`P`) with a new value and maintain its association with the object (`O`).
 */
class ObserverCrossDelegate<O, P>(
    var value: P,
    val copy: P.() -> P,
    val getter: (P) -> O?,
    val setter: (P, O?) -> Unit,
) : ReadWriteProperty<O, P> {
    /**
     * Retrieves the stored value.
     *
     * @param thisRef the reference to the object in which the property resides
     * @param property the metadata for the property being accessed
     * @return the current value of the property
     */
    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: O, property: KProperty<*>): P {
        return value
    }

    /**
     * Sets the value of the property, possibly copying and updating references based on the delegate's logic.
     * This method ensures that the setter is invoked only if the new value differs from the current one.
     *
     * @param thisRef the reference to the object for which the value is being set
     * @param property the metadata for the property being accessed or modified
     * @param value the new value to assign to the property
     */
    override fun setValue(thisRef: O, property: KProperty<*>, value: P) {
        if (this.value == value) return

        val value1 = if (getter(value) != thisRef) {
            value.copy()
        } else {
            value
        }

        this.value = value1
        setter(value1, thisRef)
    }
}