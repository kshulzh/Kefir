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
 * A delegate property that observes changes to a nullable value and
 * manages its association with an external reference. This class is
 * designed to synchronize a nullable property in a target object (`O`)
 * with an associated managed value (`P`).
 *
 * @param value The initial value of the delegate, which might be null.
 * @param copy A function for copying the managed value when necessary.
 * @param getter A function that retrieves the associated reference (`O?`)
 *               from the managed value (`P`).
 * @param setter A function that sets the associated reference (`O?`) in
 *               the managed value (`P`).
 */
class ObserverNullableCrossDelegate<O, P>(
    var value: P?,
    val copy: P.() -> P,
    val getter: (P) -> O?,
    val setter: (P, O?) -> Unit,
) : ReadWriteProperty<O, P?> {
    /**
     * Retrieves the value currently stored in the delegate.
     *
     * @param thisRef the reference to the object using the delegate
     * @param property the property being delegated
     * @return the value currently stored in the delegate, or null if no value is set
     */
    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: O, property: KProperty<*>): P? {
        return value
    }

    /**
     * Sets the value of a property for the given owner object. If the new value is null, the current
     * value is reset and the provided setter action is triggered with a null reference. If the new
     * value is not null, a copy of it is created if it is associated with a different owner object,
     * and the setter action is triggered with the updated value and owner reference.
     *
     * @param thisRef the owner object of the property
     * @param property the property whose value is being set
     * @param value the new value to be assigned to the property, or null to reset the property
     */
    override fun setValue(thisRef: O, property: KProperty<*>, value: P?) {
        if (this.value == value) return

        if (value == null) {
            setter(this.value!!, null)
            this.value = null
            return
        }

        val value1 = if (getter(value) != thisRef) {
            value.copy()
        } else {
            value
        }

        setter(value1, thisRef)
        this.value = value1
    }
}