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
import io.github.kshulzh.kefir.model.api.KtPath
import io.github.kshulzh.kefir.model.api.io.KtFileElement
import io.github.kshulzh.kefir.model.api.io.KtPackageScope
import io.github.kshulzh.kefir.model.api.io.KtPackageScopeElement
import io.github.kshulzh.kefir.model.api.utils.ObserverMutableSet
import io.github.kshulzh.kefir.model.fir.declaration.KtFirFileElement
import io.github.kshulzh.kefir.model.ir.utils.DifMutableSet
import io.github.kshulzh.kefir.transform.context.KtFirLocalTransformContext1
import io.github.kshulzh.kefir.transform.context.KtFirTransformContext
import io.github.kshulzh.kefir.transform.context.local
import io.github.kshulzh.kefir.transform.utils.submit
import org.jetbrains.kotlin.fir.declarations.FirFile
import org.jetbrains.kotlin.fir.packageFqName

/**
 * Represents the root package element within the Kotlin FIR (Frontend IR) system scope.
 * This class manages and organizes FIR files into structured package elements,
 * enabling transformations and hierarchical organization of the FIR tree.
 *
 * @property transformFirContext Provides the context and utilities required for FIR transformations.
 * @param firFiles A mutable list of FIR files to be grouped and organized into package elements.
 */
class KtFirRootPackageElement(
    var transformFirContext: KtFirTransformContext,
    firFiles: MutableList<FirFile> = mutableListOf()
) : KtPackageScope {
    /**
     * Represents a mutable set of KtPackageScopeElement objects that belong to a root package element.
     * The property is initialized lazily and serves as an observable collection where additions and deletions
     * trigger specific actions. This helps maintain relationships and context for each package scope element.
     *
     * - Adding a new element sets its parent to the root package element and triggers a FIR transformation
     *   if applicable and no problem context exists.
     * - Removing an element clears its parent reference.
     * - When operating within a problem context, applied changes are deferred and handled through a
     *   `DifMutableSet` structure, which tracks added and removed elements separately.
     */
//todo migrate to new solution
    override val packageElements: MutableSet<KtPackageScopeElement> by lazy {
        ObserverMutableSet(
            reGroupFiles(firFiles).apply { forEach { it.parent = this@KtFirRootPackageElement } }.toMutableSet(),
            onAdd = {
                it.parent = this@KtFirRootPackageElement
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
     * @param name The name of the package to be created.
     * @return The newly created package scope with the specified name.
     */
    override fun createPackage(name: KtName): KtPackageScope {
        return KtFirPackageElement(name, transformFirContext, this).also {
            packageElements.add(it)
        }
    }

    /**
     * Regroups the provided list of FIR files into a hierarchy based on their package names and
     * returns the resulting list of package scope elements.
     *
     * @param firFiles The mutable list of FIR files to be re-grouped.
     * @return A mutable list of package scope elements representing the regrouped hierarchy of packages.
     */
    private fun reGroupFiles(firFiles: MutableList<FirFile>): MutableList<KtPackageScopeElement> {
        val root = PackageNode("")

        firFiles.forEach {
            if (it.packageFqName.asString().isNotBlank() && it.packageFqName.asString() != "<root>") {
                val path = KtPath(it.packageFqName.asString().split(".").toMutableList())
                root.getOrCreate(path).nodes.add(FileNode(it))
            } else {
                root.nodes.add(FileNode(it))
            }
        }
        return root.nodes.map { it.toPackageElement() }.toMutableList()
    }

    /**
     * Represents a node within a hierarchical structure, providing the basic components for
     * identification and transformation into a corresponding package element.
     */
    private interface Node {
        /**
         * Represents the name of a Kotlin element managed or processed by the containing class.
         *
         * This property is an instance of [KtName] and may represent names associated with various
         * Kotlin structural elements such as packages, files, or scopes within the current context.
         *
         * The `name` property is shared across multiple implementations, including those related
         * to package hierarchy creation, manipulation, or scope elements management.
         *
         * This property plays a key role in uniquely identifying elements within larger structures,
         * supporting lookup, and ensuring correct object relationships.
         */
        val name: KtName

        /**
         * Converts the current element into a `KtPackageScopeElement` representation.
         *
         * The implementation may incorporate child nodes or associated elements to construct
         * the resulting package scope. Additionally, it ensures that the `parent` property
         * of the elements within the package scope is correctly assigned.
         *
         * @return A `KtPackageScopeElement` instance representing the current element and its hierarchy.
         */
        fun toPackageElement(): KtPackageScopeElement
    }

    /**
     * Represents a node corresponding to a single FIR (Frontend Intermediate Representation) file.
     * It is used to organize FIR files into a hierarchical structure, enabling the grouping and processing
     * of files within a package or module context.
     *
     * @property firFile The FIR file associated with this node. Provides the source content and metadata
     * for further transformations and processing.
     */
    private inner class FileNode(val firFile: FirFile) : Node {
        /**
         * The `name` property represents the name identifier of the node within the file structure.
         *
         * In the context of the `FileNode` class, it corresponds to the name of the `FirFile` that
         * the node encapsulates. This property is used to uniquely identify and associate file-level
         * elements within the Kotlin Frontend Intermediate Representation (FIR).
         *
         * @see FirFile
         * @see KtName
         */
        override val name: KtName = firFile.name

        /**
         * Converts the current node to a `KtPackageScopeElement` representation.
         *
         * @return The `KtPackageScopeElement` instance corresponding to this node,
         * constructed using the associated FIR file and transformation context.
         */
        override fun toPackageElement(): KtPackageScopeElement {
            return KtFirFileElement(firFile, transformFirContext)
        }
    }

    /**
     * Represents a node within a package hierarchy. Each `PackageNode` corresponds to a specific
     * name segment in the hierarchical structure, and it can contain child nodes to represent
     * sub-packages or other nested structures.
     *
     * @property name The name associated with this package node.
     * @property nodes A mutable list of child nodes contained within this package node.
     */
    private inner class PackageNode(
        override val name: KtName,
        val nodes: MutableList<Node> = mutableListOf(),
    ) : Node {
        /**
         * Converts the current node and its descendants into a `KtPackageScopeElement`.
         * The resulting element includes all child nodes transformed recursively into package elements.
         * The parent relationship is updated to ensure proper hierarchy in the resulting package structure.
         *
         * @return A `KtPackageScopeElement` representing the current node and its descendants.
         */
        override fun toPackageElement(): KtPackageScopeElement {
            return KtFirPackageElement(
                name,
                transformFirContext,
                null,
                nodes.map { it.toPackageElement() }.toMutableSet()
            ).apply {
                packageElements.forEach { it.parent = this }
            }
        }

        /**
         * Retrieves a `PackageNode` with the specified name if it exists within the `nodes` collection.
         * If no matching node is found, creates a new `PackageNode` with the given name, adds it to the
         * `nodes` collection, and returns it.
         *
         * @param name The name of the package to retrieve or create.
         * @return An existing or newly created `PackageNode` with the specified name.
         */
        fun getOrCreate(name: KtName): PackageNode {
            return nodes.find { it.name == name } as? PackageNode ?: PackageNode(name).also { nodes.add(it) }
        }

        /**
         * Navigates through the given path and retrieves an existing `PackageNode` if it exists.
         * If a `PackageNode` for any part of the path does not exist, it creates a new one.
         *
         * @param path The `KtPath` object representing the sequence of names through which to navigate
         *             or create the package nodes.
         * @return The `PackageNode` corresponding to the last part of the specified `KtPath`.
         */
        fun getOrCreate(path: KtPath): PackageNode {
            return path.parts.fold(this) { acc, name -> acc.getOrCreate(name) }
        }
    }
}