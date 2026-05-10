/*
 * Copyright (c) 2025-2026. Kirill Shulzhenko
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

package io.github.kshulzh.kefir.model.ir.declaration

import io.github.kshulzh.kefir.model.api.KtName
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationElement
import io.github.kshulzh.kefir.model.api.declaration.KtDeclarationElement
import io.github.kshulzh.kefir.model.api.io.KtFileElement
import io.github.kshulzh.kefir.model.api.io.KtPackageScope
import io.github.kshulzh.kefir.model.utils.createLazyIrSet2
import io.github.kshulzh.kefir.transform.IrWrapper
import io.github.kshulzh.kefir.transform.context.KtTransformContext
import io.github.kshulzh.kefir.transform.model.addDeclaration
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.declarations.name
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI

/**
 * Represents an Intermediate Representation (IR) implementation of a Kotlin file element.
 *
 * This class wraps an [IrFile] element, enabling transformations and interactions with
 * Kotlin IR structures within a package scope. It serves as both a wrapper for IR elements
 * and an implementation of the [KtFileElement] interface.
 *
 * @property irElement The [IrFile] instance that this element wraps.
 * @property transformContext The transformation context used for handling IR-related operations
 * within this file element.
 * @property parent The parent [KtPackageScope] representing the package that contains this file
 * element, or null if no parent scope is assigned.
 */
class KtIrFileElement(
    override var irElement: IrFile,
    var transformContext: KtTransformContext,
    override var parent: KtPackageScope? = null
) : KtFileElement, IrWrapper<IrFile> {
    /**
     * A mutable set of declaration elements associated with the file element.
     *
     * This property represents all declarations (e.g., classes, functions, or properties)
     * contained within the IR file element. It is implemented lazily and allows for
     * dynamic transformation and handling of declarations within the intermediate
     * representation transformation context.
     *
     * The declarations are initialized using the `irElement`'s existing declarations,
     * transformed into Kotlin declarations using the `wrapIrDeclaration` function.
     * When a new declaration is added to this set, it is also added to the underlying
     * `IrFile` structure using the `addDeclaration` extension function.
     *
     * The set is managed in a way to support incremental updates, such that any added
     * or removed declaration is synchronized with the IR structure.
     */
    @OptIn(UnsafeDuringIrConstructionAPI::class)
    override val declarations: MutableSet<KtDeclarationElement> by
    createLazyIrSet2(
        transformContext = transformContext,
        initializer = { irElement.declarations.toMutableSet() },
        transformer = { wrapIrDeclaration(it, transformContext, this@KtIrFileElement) },
        property = KtDeclarationElement::declarationsScope,
        onAdd = {
            irElement.addDeclaration(it, this)
        },
        onDelete = {
            TODO()
        }
    )

    /**
     * Represents the name of the associated `IrFile` element.
     * This property is overridden to provide access to the underlying IR representation's name.
     *
     * The getter fetches the name from the associated `irElement`.
     * The setter updates the name of the underlying IR element with the provided value.
     */
    override var name: KtName
        get() = irElement.name
        set(value) {}

    /**
     * Returns a string representation of the file element.
     *
     * The string representation includes a `<FILE>` prefix followed by the name of the file.
     *
     * @return A string in the format `"<FILE> [file name]"`.
     */
    override fun toString() = "<FILE> $name"
    /**
     * Represents the list of annotation elements associated with the current `KtIrFileElement`.
     *
     * This property provides access to the annotations applied to the `KtIrFileElement` instance,
     * allowing for the retrieval or manipulation of annotation metadata within the Kotlin Intermediate Representation (IR) model.
     *
     * The annotations are expressed as a mutable list of `KtAnnotationElement`, which encapsulates
     * individual annotation data and its associated information, including annotation type and scope.
     */
    override val annotations: MutableList<KtAnnotationElement>
        get() = TODO("Not yet implemented")
}