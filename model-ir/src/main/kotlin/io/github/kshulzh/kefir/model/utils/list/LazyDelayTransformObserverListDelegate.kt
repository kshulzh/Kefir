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
import io.github.kshulzh.kefir.model.api.utils.ObserverMutableList
import io.github.kshulzh.kefir.model.utils.linkCheck
import io.github.kshulzh.kefir.transform.context.KtNodeStack
import io.github.kshulzh.kefir.transform.utils.submit
import io.github.kshulzh.problemgraph.context.ProblemContext
import org.jetbrains.kotlin.backend.common.pop
import org.jetbrains.kotlin.backend.common.push
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * A delegate implementation that provides delayed initialization of a `MutableList` property,
 * with support for transformation, observation, and custom behaviors during element addition
 * and deletion. This delegate allows for the lazy transformation and observation of a list
 * while managing its lifecycle and associated observers.
 *
 * @param O The type of the containing object for the delegate property.
 * @param P The type of the elements in the observed list.
 * @param C A context type used for handling transformations and observer callbacks.
 * @param I The type returned by the `onAdd` callback during element additions.
 *
 * @property transformContext The context used during transformation-related operations,
 * typically providing transformation logic or mechanisms for handling nodes.
 * @property initializer A lambda function for initializing the `MutableList` lazily.
 * @property problemContext The context used to handle asynchronous tasks effectively.
 * @property copy A function that allows copying of elements of type `P`.
 * @property onDelete A callback invoked during the deletion of elements from the list,
 * with access to the context and the item's index.
 * @property onAdd A callback invoked during the addition of elements to the list,
 * with access to the context and the item's index.
 */
class LazyDelayTransformObserverListDelegate<O : KtElement, P : KtElement, C, I>(
    val transformContext: C,
    var initializer: (() -> List<P>)? = null,
    val problemContext: ProblemContext,
    val copy: P.() -> P,
    val onDelete: C.(P, Int) -> Unit = { _, _ -> },
    val onAdd: C.(P, Int) -> I?,
) : ReadWriteProperty<O, MutableList<P>> {
    /**
     * Holds a mutable list of elements of type `P` that is initialized lazily.
     * This property is used within the context of a delegate to manage a list of elements
     * which can be dynamically transformed or observed based on specific operations.
     * The actual initialization of this list is dependent on the logic within the containing delegate.
     */
    lateinit var initial: MutableList<P>
    /**
     * A delegated property that initializes and manages a lazy collection transformation within the given context.
     *
     * This property uses Kotlin's `lazy` delegate to ensure deferred initialization. It schedules a task
     * with the `problemContext` that performs the following:
     * - Verifies the `thisRef` through a `linkCheck` to ensure the context is linked and proceeds only if valid.
     * - If the `transformContext` is of type `KtNodeStack`, it pushes the current transformation onto the node stack.
     * - Manages the state of the transformation by:
     *     - Cleaning up old references using `onDelete` if this is the initial transformation.
     *     - Adding new elements from a list to the transformation context using `onAdd`.
     * - Maintains a count of processed elements to avoid redundant operations in subsequent runs.
     * - Ensures the stack state is restored by popping the transformation context when processing is completed.
     *
     * The property is intentionally structured to support contexts that involve delayed or transactional
     * modifications to an underlying collection.
     */
    val lazy by lazy {
        var added = 0
        var isOld = true
        problemContext.submit {
            if (!thisRef.linkCheck()) {
                //unlinked
                return@submit
            }
            if (transformContext is KtNodeStack) {
                transformContext.nodeStack.push(this)
                try {
                    //todo retry a mechanism in case when something went wrong
                    if (isOld) {
                        initial.forEach { t -> transformContext.onDelete(t, 0) }
                        isOld = false
                    }
                    for (i in added..<list.size) {
                        list[i].let { transformContext.onAdd(it, -1) }
                        added++
                    }

                } finally {
                    transformContext.nodeStack.pop()
                }
            }
        }
    }
    /**
     * A lazily-initialized mutable list of type `P` that is used to manage delayed transformations
     * and observe changes within the context of a `LazyDelayTransformObserverListDelegate`.
     *
     * This variable is expected to be initialized on-demand and wrapped using a specific `wrap` logic,
     * which ensures that elements are handled using the prescribed transformation and observation behaviors
     * within the containing class.
     *
     * Modifications to the list may trigger the configured `onAdd` and `onDelete` operations in the
     * associated transform context.
     */
    lateinit var list: MutableList<P>
    /**
     * Holds a reference to the object that owns this delegated property. This variable is dynamically
     * initialized when the `getValue` or `setValue` methods are accessed, allowing access to the
     * owning object for operations that require its context.
     *
     * This variable is marked as `lateinit` to defer its initialization until an appropriate context
     * is available, such as during the first access of the delegated property. Its use ensures that
     * methods requiring the owning reference can function correctly.
     *
     * Intended for internal use within the `LazyDelayTransformObserverListDelegate` class to
     * support dynamic interactions with the owner object.
     */
    lateinit var thisRef: O
    /**
     * Retrieves the mutable list of type `P`, initializing and wrapping it if necessary.
     *
     * @param thisRef The reference to the object owning this delegate.
     * @param property Metadata for the property being accessed.
     * @return The mutable list of type `P`, possibly transformed through an initializer or wrapper function.
     */
    override fun getValue(thisRef: O, property: KProperty<*>): MutableList<P> {
        if (initializer != null) {
            this.thisRef = thisRef
            val value = initializer?.invoke()?.toMutableList() ?: mutableListOf()
            initial = value
            initializer = null
            list = wrap(value.toMutableList(), thisRef)
        }
        return list
    }

    /**
     * Sets the value of the delegated property. If the initializer is provided, it will be invoked
     * to initialize the state. Otherwise, the provided value will replace the current list after
     * wrapping it with additional behavior defined by the `wrap` method.
     *
     * @param thisRef The reference to the instance of the object containing the delegated property.
     * @param property The property metadata for the delegated property.
     * @param value The new value to be assigned to the delegated property.
     */
    override fun setValue(thisRef: O, property: KProperty<*>, value: MutableList<P>) {
        if (initializer != null) {
            this.thisRef = thisRef
            val valu = initializer?.invoke()?.toMutableList() ?: mutableListOf()
            initial = valu
            initializer = null
            list = wrap(value.toMutableList(), thisRef)
        } else {
            list = wrap(value.toMutableList(), thisRef)
        }
    }

    /**
     * Wraps a mutable list of elements into an `ObserverMutableList`, enabling observation and potential
     * transformation of elements during addition and deletion operations.
     *
     * @param elements The original mutable list of elements to be wrapped.
     * @param thisRef A reference object used to associate the wrapped list with a specific owner or context.
     * @return The wrapped mutable list as an instance of `ObserverMutableList`.
     */
    fun wrap(elements: MutableList<P>, thisRef: O): MutableList<P> {
        return ObserverMutableList(
            elements, { p, i ->
                lazy
                p.copy()
            }, { p, i ->
                lazy
            }
        )
    }
}