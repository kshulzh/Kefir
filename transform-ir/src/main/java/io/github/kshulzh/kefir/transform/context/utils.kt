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

package io.github.kshulzh.kefir.transform.context

/**
 * Creates or retrieves a local transformation context for the current FIR transformation.
 * If the current context is already a local transformation context, it updates the `external` property
 * with the given value. Otherwise, it creates a new instance of `KtFirLocalTransformContext2` with the
 * specified `external` value and the current context as its delegate.
 *
 * @param external An optional parameter to associate an external FIR element or additional data
 *                 with the local transformation context. Defaults to null.
 * @return A `KtFirLocalTransformContext` instance representing the initialized or updated
 *         local transformation context.
 */
fun KtFirTransformContext.local(external: Any? = null): KtFirLocalTransformContext {
    return (this as? KtFirLocalTransformContext?)?.also {
        it.external = external
    } ?: KtFirLocalTransformContext2(this, external = external)
}

/**
 * Creates a local IR (Intermediate Representation) transformation context specific to the current scope.
 * If the current context is already an instance of `KtIrLocalTransformContext`, it updates its external property
 * with the provided value and returns it. Otherwise, it creates a new `KtIrLocalTransformContext2` instance
 * based on the current transformation context and the provided external property.
 *
 * @param external Optional user-defined data to associate with the local transformation context. Defaults to `null`.
 * @return An instance of `KtIrLocalTransformContext`, representing a context for performing local IR transformations.
 */
fun KtTransformContext.local(external: Any? = null): KtIrLocalTransformContext {
    return (this as? KtIrLocalTransformContext)?.also { it.external = external } ?: KtIrLocalTransformContext2(
        this,
        external = external
    )
}