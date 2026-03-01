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
import kotlin.reflect.KMutableProperty1

/**
 * Creates a delegate that allows observing and modifying a property of type [P] associated
 * with an object of type [O]. The delegate uses a copy mechanism to ensure immutability of the
 * provided initial value and updates it using the specified property accessors.
 *
 * @param initial the initial value for the property of type [P]
 * @param property the mutable property reference that defines how to access and modify the
 * property of type [O] within the value of type [P]
 * @param copy an optional copy function, used to produce a copy of the value [P] when required.
 * Defaults to an identity function that returns the object itself
 * @return a ReadWriteProperty delegate that observes and manages the given property
 */
@Suppress("NOTHING_TO_INLINE")
inline fun <O, P> createDelegate(
    initial: P,
    property: KMutableProperty1<P, O?>,
    noinline copy: P.() -> P = { this },
): ReadWriteProperty<O, P> {
    return ObserverCrossDelegate(
        initial, copy, property.getter, property.setter
    )
}

/**
 * Creates a delegate that observes changes and manages a nullable property with a customizable copy operation.
 *
 * @param initial the initial value of the delegate
 * @param property a mutable property reference that the delegate interacts with
 * @param copy a lambda function defining how to create a copy of the value, with a default implementation returning the same value
 * @return a read-write property delegate that observes and manages the specified property
 */
@Suppress("NOTHING_TO_INLINE")
inline fun <O, P> createNullableDelegate(
    initial: P?,
    property: KMutableProperty1<P, O?>,
    noinline copy: P.() -> P = { this },
): ReadWriteProperty<O, P?> {
    return ObserverNullableCrossDelegate(
        initial, copy, property.getter, property.setter
    )
}

/**
 * Creates a delegate for managing a mutable list of items with observable properties.
 *
 * This function provides a way to wrap a mutable list so that changes to its items' properties
 * can be observed. The delegate intercepts list modifications and ensures that the observable
 * properties of each item are updated appropriately.
 *
 * @param initial the initial list of items to be managed by the delegate
 * @param property the property in each item to be observed and managed
 * @param copy a function that provides a copy of an item when necessary; defaults to returning the same item
 * @return a delegate for managing a mutable list of items with the specified observable property
 */
@Suppress("NOTHING_TO_INLINE")
inline fun <O, P> createListDelegate(
    initial: MutableList<P>,
    property: KMutableProperty1<P, O?>,
    noinline copy: P.() -> P = { this },
): ReadWriteProperty<O, MutableList<P>> {
    return ObserverCrossListDelegate(
        initial, copy, property.getter, property.setter
    )
}

/**
 * Creates a delegate for managing a mutable list of nullable elements, while providing an observer-based
 * behavior for property changes via the specified `KMutableProperty1`. Each element in the list can be
 * optionally copied using the provided `copy` function.
 *
 * @param initial the initial mutable list of nullable elements
 * @param property the mutable property used for getting and setting values in the list elements
 * @param copy a lambda function that returns a copy of the element; defaults to the element itself if not provided
 * @return a ReadWriteProperty that delegates the property access and mutation operations for the list
 */
@Suppress("NOTHING_TO_INLINE")
inline fun <O, P> createListNullableDelegate(
    initial: MutableList<P?>,
    property: KMutableProperty1<P, O?>,
    noinline copy: P.() -> P = { this },
): ReadWriteProperty<O, MutableList<P?>> {
    return ObserverCrossListNullableDelegate(
        initial, copy, property.getter, property.setter
    )
}

/**
 * Creates a delegate for managing a mutable set of elements, where each element is associated with
 * a property of a containing object. The delegate ensures that changes in the set are synchronized
 * with the target object's properties.
 *
 * @param initial the initial set of elements to be managed by the delegate
 * @param property the property of each element to bind to the target object
 * @param copy a function that creates a copy of an element when a modification occurs; defaults to returning the element itself
 * @return a ReadWriteProperty that provides delegate behavior for the given set
 */
@Suppress("NOTHING_TO_INLINE")
inline fun <O, P> createSetDelegate(
    initial: MutableSet<P>,
    property: KMutableProperty1<P, O?>,
    noinline copy: P.() -> P = { this },
): ReadWriteProperty<O, MutableSet<P>> {
    return ObserverCrossSetDelegate(
        initial, copy, property.getter, property.setter
    )
}