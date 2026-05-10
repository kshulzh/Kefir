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
 * A delegate class designed to manage a lazy-evaluated, mutable list of elements
 * while also providing transformation and observation capabilities in a Kotlin compiler context.
 *
 * This class serves as a mechanism to delay the initialization and transformation of a list
 * of elements, enabling fine-grained control over when and how these transformations occur.
 * It also ensures that the modifications to the list (additions or deletions) are observed
 * and handled appropriately using custom callbacks.
 *
 * @param O The type of the owner class that accesses this delegate.
 * @param P The type of the elements in the list being managed by this delegate.
 * @param C The context in which transformations are performed.
 * @param I The type of the items being added to the list, linked with transformation operations.
 * @property transformContext The context object used to manage transformation-related operations.
 * @property initializer A lambda that initializes the list of elements lazily when accessed for the first time.
 * @property problemContext The context in which the delegated transformation problems are processed and managed.
 * @property getter A lambda that retrieves the owner (`O`) linked to an element (`P`).
 * @property copy A lambda that creates a deep copy of an element of type `P`.
 * @property setter A lambda that sets the association between an element (`P`) and its owner (`O`).
 * @property onDelete An optional lambda called when an element is deleted, allowing for custom handling.
 * @property onAdd An optional lambda called when an element is added, supporting custom transformation of new elements.
 */
class LazyDelayTransformObserverCrossListDelegate<O : KtElement, P : KtElement, C, I>(
    val transformContext: C,
    var initializer: (() -> List<P>)? = null,
    val problemContext: ProblemContext,
    //val transformer: (I)->P?,
    val getter: (P) -> O?,
    val copy: P.() -> P,
    val setter: (P, O?) -> Unit,
    val onDelete: C.(P, Int) -> Unit = { _, _ -> },
    val onAdd: C.(P, Int) -> I?,
) : ReadWriteProperty<O, MutableList<P>> {
    /**
     * A mutable list that is lazily initialized and designed to observe transformation events.
     *
     * The `initial` property holds the underlying list of elements used during the observation
     * of transformations. It is initialized upon the first access through the getter, using a
     * provided initializer function, if available, or manually set through the setter method.
     *
     * Changes to the elements in this list are tracked and processed in conjunction with the
     * transformation context and problem context associated with the containing delegate class.
     * This allows for handling of specific behavior such as on-add or on-delete operations
     * applied to the elements during runtime.
     *
     * Initialization is deferred until needed and relies on the `initializer` property to generate
     * the initial list if not already assigned explicitly. Multiple operations may involve wrapping
     * or copying of elements as part of its usage.
     */
    lateinit var initial: MutableList<P>
    /**
     * A lazily initialized delegate that triggers delayed transformation operations when accessed.
     * The transformations are tied to the lifecycle of a Kotlin compiler context and interact with
     * a `ProblemContext` and a `KtNodeStack` for managing node-based transformations and actions.
     *
     * Behavior:
     * - Lazily initializes and processes the elements of a list, submitting node-based actions to a
     *   `ProblemContext` for deferred execution.
     * - Ensures transformations only proceed if the supervising context (`thisRef`) passes a link validity check (`linkCheck`).
     * - Tracks newly added elements in the list and invokes the `onAdd` handler for each. If the list was previously operated
     *   as "old" (`isOld` is true), it invokes the `onDelete` handler for initial elements before processing additions.
     * - Covers exception safety for stack operations via a `try-finally` block, ensuring proper push/pop behavior in node management.
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
     * A lazily initialized mutable list of type `P` used for delayed transformation and observation of list elements.
     * The list is initialized through the provided `initializer` function when accessed for the first time.
     * Observations on additions and deletions of elements within the list are managed via the associated context and callbacks.
     *
     * This variable serves as the core storage for elements, with its behavior controlled by the containing class.
     */
    lateinit var list: MutableList<P>
    /**
     * Holds a reference to the owner object (`thisRef`) to which the delegate is attached.
     * This property is used to maintain a connection between the delegate and the owning object,
     * allowing the delegate to access or modify the associated object's state as necessary.
     *
     * The reference is initialized lazily when the delegate's `getValue` or `setValue` method
     * is invoked for the first time. It ensures that the owning object can be effectively
     * utilized in deferred operations or context-sensitive transformations managed within the delegate.
     */
    lateinit var thisRef: O
    /**
     * Retrieves the value of the delegate property. If the initializer has not been invoked yet,
     * this method initializes the property, wraps the resulting list with an observer-based
     * mutable list, and stores the result for future calls.
     *
     * @param thisRef The object for which the delegate is being used.
     * @param property The metadata of the property to which the delegate is assigned.
     * @return A mutable list of type [P] representing the current value of the property.
     */
    override fun getValue(thisRef: O, property: KProperty<*>): MutableList<P> {
        if (initializer != null) {
            this.thisRef = thisRef
            val value = initializer?.invoke()?.toMutableList() ?: mutableListOf()
            initial = value
            initializer = null
            list = wrap(value.also { element -> element.forEach { setter(it, thisRef) } }.toMutableList(), thisRef)
        }
        return list
    }

    /**
     * Overrides the setValue method to set a new value for a delegated property.
     *
     * @param thisRef The reference to the object for which this delegate is defined.
     * @param property The metadata for the property being set.
     * @param value The new value to assign to the property.
     */
    override fun setValue(thisRef: O, property: KProperty<*>, value: MutableList<P>) {
        if (initializer != null) {
            this.thisRef = thisRef
            val valu = initializer?.invoke()?.toMutableList() ?: mutableListOf()
            initial = valu
            initializer = null
            list = wrap(value.also { element -> element.forEach { setter(it, thisRef) } }.toMutableList(), thisRef)
        } else {
            list.forEach { setter(it, null) }
            list = wrap(value.also { element -> element.forEach { setter(it, thisRef) } }.toMutableList(), thisRef)
        }
    }

    /**
     * Wraps the given mutable list of type `P` with an `ObserverMutableList`, enabling transformation
     * and tracking of elements during modification operations.
     *
     * @param elements The initial mutable list of elements of type `P` to be wrapped.
     * @param thisRef The reference of type `O` used for updating or clearing associations in the wrapped list elements.
     * @return A new `ObserverMutableList` wrapping the provided `elements`, with custom behavior applied
     *         for add and remove operations using the `getter`, `setter`, and `copy` functions.
     */
    fun wrap(elements: MutableList<P>, thisRef: O): MutableList<P> {
        return ObserverMutableList(
            elements, { p, i ->
                lazy
                if (getter(p) != null) {
                    p.copy().also { setter(it, thisRef) }
                } else {
                    p.also { setter(it, thisRef) }
                }
            }, { p, i ->
                lazy
                setter(p, null)
            }
        )
    }
}