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

package io.github.kshulzh.kefir.model.fir.io

import io.github.kshulzh.kefir.model.api.KtName
import io.github.kshulzh.kefir.model.api.io.KtFileElement
import io.github.kshulzh.kefir.model.api.io.KtPackageElement
import io.github.kshulzh.kefir.model.api.io.KtPackageScope
import io.github.kshulzh.kefir.model.api.io.KtPackageScopeElement
import io.github.kshulzh.kefir.model.api.utils.ObserverMutableSet
import io.github.kshulzh.kefir.model.ir.utils.DifMutableSet
import io.github.kshulzh.kefir.transform.context.KtFirLocalTransformContext1
import io.github.kshulzh.kefir.transform.context.KtFirTransformContext
import io.github.kshulzh.kefir.transform.context.local
import io.github.kshulzh.kefir.transform.utils.submit

/**
 * Represents a specific implementation of [KtPackageElement] designed to work with
 * Kotlin FIR (Frontend Intermediate Representation) structures. It serves as a package
 * container and supports transformations of its elements in the context of FIR processing.
 *
 * @property name The name of the package represented by this element.
 * @property transformFirContext Context used for performing FIR transformations and managing
 * related local or global properties.
 * @property parent An optional reference to the parent package to which this package belongs.
 * Defaults to null if this is a root package.
 * @property packageElements A set of package scope elements belonging to this package.
 * This property is lazily initialized with an observer-based implementation that reacts
 * when elements are added or removed.
 */
class KtFirPackageElement(
    override var name: KtName,
    var transformFirContext: KtFirTransformContext,
    override var parent: KtPackageScope? = null,
    packageElements: MutableSet<KtPackageScopeElement> = mutableSetOf()
) : KtPackageElement {
    /**
     * Represents a lazily initialized mutable set of `KtPackageScopeElement` objects that belong to the package.
     *
     * This set utilizes an `ObserverMutableSet` to observe and manage changes and their associated effects:
     * - On addition of an element, the parent of the element is set to the current package and, if applicable,
     *   FIR transformation is triggered for `KtFileElement` instances.
     * - On removal of an element, the parent reference of the element is cleared.
     *
     * If a problem context exists in the associated `KtFirTransformContext`, a `DifMutableSet` is created to
     * track added elements independently, and their transformations are submitted to the problem context's action manager.
     */
    //todo migrate to new solution
    override val packageElements: MutableSet<KtPackageScopeElement> by lazy {
        ObserverMutableSet(
            packageElements.apply { forEach { it.parent = this@KtFirPackageElement } }.toMutableSet(),
            onAdd = {
                it.parent = this@KtFirPackageElement
                if (it is KtFileElement && transformFirContext.problemContext == null) {
                    with(transformFirContext.local()) { transformFirContext.firTransform(it) }
                }
                null
            },
            onDelete = {
                it.parent = null
            }).let { set1 ->
            val problemContext = transformFirContext.problemContext
            if (problemContext == null) {
                set1
            } else {
                DifMutableSet(set1).also { element ->
                    problemContext.submit {
                        element.added.forEach {
                            if (it is KtFileElement) {
                                with(
                                    KtFirLocalTransformContext1(
                                        transformFirContext,
                                        ArrayDeque(listOf(this))
                                    )
                                ) { transformFirContext.firTransform(it) }
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * Creates a new package scope with the given name and associates it with the current scope.
     * The created package is also added to the collection of package elements managed by this scope.
     *
     * @param name the name of the package to be created.
     * @return the new package scope with the specified name.
     */
    override fun createPackage(name: KtName): KtPackageScope {
        return KtFirPackageElement(name, transformFirContext, this).also {
            packageElements.add(it)
        }
    }

    /**
     * Returns the string representation of the current object by using its `name` property.
     * Overrides the default `toString` method to provide a custom string representation.
     *
     * @return A string representation, specifically the value of the `name` property.
     */
    override fun toString() = name
}