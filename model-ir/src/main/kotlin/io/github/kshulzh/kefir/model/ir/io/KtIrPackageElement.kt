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

package io.github.kshulzh.kefir.model.ir.io

import io.github.kshulzh.kefir.model.api.KtName
import io.github.kshulzh.kefir.model.api.io.KtFileElement
import io.github.kshulzh.kefir.model.api.io.KtPackageElement
import io.github.kshulzh.kefir.model.api.io.KtPackageScope
import io.github.kshulzh.kefir.model.api.io.KtPackageScopeElement
import io.github.kshulzh.kefir.model.utils.createLazyIrSet2
import io.github.kshulzh.kefir.transform.context.KtTransformContext
import io.github.kshulzh.kefir.transform.model.addFile

/**
 * Represents an implementation of [KtPackageElement] and [KtPackageScope] using IR (Intermediate Representation) constructs.
 * This class provides functionality for creating and managing hierarchical package structures.
 *
 * @property name The name of the package represented by this element.
 * @property transformContext The context containing IR transformation utilities and metadata.
 * @property parent The parent package scope, which can be null if this represents a root package.
 * @constructor Initializes the package element with the specified name, transformation context, and an optional parent package scope.
 */
class KtIrPackageElement(
    override var name: KtName,
    var transformContext: KtTransformContext,
    override var parent: KtPackageScope? = null,
    packageElements: MutableList<KtPackageScopeElement> = mutableListOf()
) : KtPackageElement {
    /**
     * Holds a mutable set of package scope elements associated with this package.
     *
     * The property is backed by a lazily initialized, transformable set that allows dynamic addition,
     * removal, and update of elements while maintaining references to their parent scope.
     *
     * When a `KtFileElement` is added to the set, it is also added to the module fragment via the transformation
     * context, effectively integrating the file into the IR module.
     *
     * The initialization of this set and its transformations are handled by the `createLazyIrSet2` utility,
     * which provides mechanisms for lazy loading, element transformations, and property association.
     */
    override val packageElements: MutableSet<KtPackageScopeElement> by createLazyIrSet2(
        transformContext = transformContext,
        initializer = { packageElements.toMutableSet() },
        transformer = { it },
        property = KtPackageScopeElement::parent,
        onAdd = {
            //todo return file
            if (it is KtFileElement) {
                transformContext.moduleFragment.addFile(
                    it, this
                )
            }
            null
        }
    )

    /**
     * Creates a new package scope as a child of the current package scope.
     *
     * @param name The name of the package to create.
     * @return The newly created package scope.
     */
    override fun createPackage(name: KtName): KtPackageScope {
        return KtIrPackageElement(name, transformContext, this).also {
            packageElements.add(it)
        }
    }

    /**
     * Returns the string representation of the package element, which is defined by its `name`.
     *
     * This method provides a textual representation for this object by returning its `name` property.
     * It is primarily useful for debugging or any context where the string interpretation of the object is required.
     */
    override fun toString() = name
}