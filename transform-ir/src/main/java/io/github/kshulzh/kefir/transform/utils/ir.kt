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

package io.github.kshulzh.kefir.transform.utils

import io.github.kshulzh.kefir.model.api.KtAttributes
import io.github.kshulzh.kefir.model.api.KtElement
import io.github.kshulzh.kefir.transform.FirWrapper
import io.github.kshulzh.kefir.transform.IrWrapper
import io.github.kshulzh.kefir.transform.context.KtFirLocalTransformContext
import io.github.kshulzh.kefir.transform.context.KtIrLocalTransformContext
import org.jetbrains.kotlin.fir.FirElement

/**
 * Represents a delegated property for managing the "ir" attribute in the attributes map of a [KtAttributes] instance.
 *
 * This property provides a convenient way to get or set the value associated with the "ir" key in the attributes map.
 * By default, it interacts with the "ir" entry in the underlying `attributes` property of the [KtAttributes] interface.
 *
 * The type of the value is [Any?], allowing flexibility in what can be stored as the "ir" attribute.
 *
 * Getter:
 * Retrieves the value of the "ir" attribute from the attributes map.
 *
 * Setter:
 * Updates the value of the "ir" attribute in the attributes map. The provided value is cast to [Any].
 */
var KtAttributes.ir: Any?
    get() = attributes["ir"]
    set(value) {
        attributes["ir"] = value as Any
    }

/**
 * An extension property for `KtAttributes` that provides access to the `fir` attribute.
 *
 * The `fir` attribute is stored as a key-value pair in the `attributes` map of `KtAttributes`.
 * It allows associating custom metadata or properties dynamically with the `fir` key.
 *
 * The property provides a getter and setter for the `fir` attribute. When a value is set, it is cast to `Any` and stored
 * under the `fir` key in the `attributes` map.
 *
 * @receiver The `KtAttributes` instance on which this property is accessed or modified.
 * @property fir The value associated with the `fir` key in the `attributes` map.
 */
var KtAttributes.fir: Any?
    get() = attributes["fir"]
    set(value) {
        attributes["fir"] = value as Any
    }

/**
 * Retrieves the IR (Intermediate Representation) element associated with the given KtElement,
 * if it is available and matches the expected type.
 *
 * This function checks if the current instance is an `IrWrapper` or implements `KtAttributes`,
 * and attempts to cast the associated IR element to the desired type `T`.
 *
 * @return An instance of type `T` representing the associated IR element, or `null` if the element
 *         is not available or cannot be cast to the expected type.
 */
@Suppress("UNCHECKED_CAST")
fun <T> KtElement.getIr(): T? {
    return when (this) {
        is IrWrapper<*> -> {
            irElement as? T
        }

        is KtAttributes -> {
            ir as? T
        }

        else -> null
    }
}

/**
 * Retrieves a FIR (Frontend Intermediate Representation) element of type [T] associated with the current [KtElement].
 *
 * The method attempts to extract the FIR element by inspecting the type of the current [KtElement].
 * - If the element implements [FirWrapper], it retrieves the `firElement` property and casts it to the specified type [T].
 * - If the element implements [KtAttributes], it retrieves the `fir` property and casts it to the specified type [T].
 * - If none of the above conditions apply, it returns `null`.
 *
 * @return The FIR element of type [T] if found, or `null` if the FIR element is unavailable or cannot be cast to the specified type.
 */
@Suppress("UNCHECKED_CAST")
fun <T> KtElement.getFir(): T? {
    return when (this) {
        is FirWrapper<*> -> {
            firElement as? T
        }

        is KtAttributes -> {
            fir as? T
        }

        else -> null
    }
}

/**
 * Associates the calling object of type [T] with the provided [KtElement] if it implements [KtAttributes].
 * Specifically, assigns the calling object as the `ir` field of the [KtAttributes] element.
 *
 * @param e The [KtElement] to potentially link the calling object to. If the element is an instance of [KtAttributes],
 *          the calling object will be set as its `ir` property.
 * @return The original calling object of type [T].
 */
fun <T> T.linkIr(e: KtElement): T {
    return this.also {
        if (e is KtAttributes) {
            e.ir = it
        }
    }
}

/**
 * Links a `FirElement` with a given `KtElement`. If the provided `KtElement`
 * is an instance of `KtAttributes`, this method associates the `FirElement`
 * with it by setting the `fir` property of the `KtAttributes`.
 *
 * @param e The `KtElement` to link with the calling `FirElement` instance.
 *           If `e` is an instance of `KtAttributes`, the `fir` property is updated.
 * @return The original `FirElement` instance after the linking operation.
 */
fun <T : FirElement> T.linkFir(e: KtElement): T {
    return this.also {
        if (e is KtAttributes) {
            e.fir = it
        }
    }
}

/**
 * Retrieves either the IR (Intermediate Representation) associated with the current `KtElement`
 * or an external value from the provided transformation context, returning it as the desired type.
 *
 * The method first attempts to retrieve the IR representation of the `KtElement` by invoking its `getIr()` function.
 * If no IR can be retrieved or the `KtElement` is null, the method falls back to returning the
 * external property stored in the context as the desired type.
 *
 * @return The IR representation or an external value of type [T], or `null` if neither is available or castable.
 */
@Suppress("UNCHECKED_CAST")
context(c: KtIrLocalTransformContext)
fun <T> KtElement?.getIrOrExternal(): T? {
    return this?.getIr() ?: c.external as? T
}

/**
 * Retrieves the FIR (Frontend Intermediate Representation) element associated with the current
 * Kotlin element. If the FIR element is not available within the current context,
 * the method attempts to retrieve the external FIR element.
 *
 * @return The FIR element of type [T] if available; otherwise, `null`.
 */
@Suppress("UNCHECKED_CAST")
context(c: KtFirLocalTransformContext)
fun <T : FirElement> KtElement?.getFirOrExternal(): T? {
    return this?.getFir() ?: (c.external as? T)
}