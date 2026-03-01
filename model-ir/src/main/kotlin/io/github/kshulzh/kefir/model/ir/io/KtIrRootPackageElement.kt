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
import io.github.kshulzh.kefir.model.api.KtPath
import io.github.kshulzh.kefir.model.api.io.KtFileElement
import io.github.kshulzh.kefir.model.api.io.KtPackageScope
import io.github.kshulzh.kefir.model.api.io.KtPackageScopeElement
import io.github.kshulzh.kefir.model.ir.declaration.KtIrFileElement
import io.github.kshulzh.kefir.model.utils.createLazyIrSet2
import io.github.kshulzh.kefir.transform.context.KtTransformContext
import io.github.kshulzh.kefir.transform.model.addFile
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.declarations.name

/**
 * Represents the root package element in the IR transformation context.
 * Manages and organizes the IR files into respective package scopes.
 *
 * @constructor Creates a new instance of KtIrRootPackageElement.
 * @param transformContext The transformation context associated with the package.
 * @param irFiles A mutable list of IR files to be organized into packages. Defaults to an empty list.
 */
class KtIrRootPackageElement(
    var transformContext: KtTransformContext,
    irFiles: MutableList<IrFile> = mutableListOf()
) : KtPackageScope {

    /**
     * A mutable set containing instances of [KtPackageScopeElement] associated with the current package scope.
     *
     * This property is lazily initialized and backed by the `createLazyIrSet2` utility, which facilitates
     * deferred loading, transformation, and dynamic updates of its elements. Each element in the set
     * automatically maintains a reference to its parent package scope.
     *
     * Key behaviors:
     * - Initialization: Populates the set by re-grouping package files via the private `reGroupFiles` method.
     * - Addition: When a [KtFileElement] is added to the set, it is also integrated into the IR module fragment
     *   using the `addFile` method provided in the transformation context.
     * - Transformation: Any transformation performed on the elements will preserve their association with the
     *   parent property [KtPackageScopeElement.parent].
     *
     * This property plays a crucial role in managing hierarchical packages in the intermediate representation (IR),
     * enabling effective creation, traversal, and modification of package structures.
     */
    override val packageElements: MutableSet<KtPackageScopeElement>
            by createLazyIrSet2(
                transformContext = transformContext,
                initializer = { reGroupFiles(irFiles) },
                transformer = { it },
                property = KtPackageScopeElement::parent,
                onAdd = {
                    //todo return transformed file
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
     * Rearranges and organizes a list of IR files into a hierarchical structure of package scope elements
     * based on their fully qualified package names.
     *
     * @param irFiles The mutable list of `IrFile` objects that need to be regrouped into package structures.
     * @return A mutable set of `KtPackageScopeElement` representing the organized package hierarchy.
     */
    private fun reGroupFiles(irFiles: MutableList<IrFile>): MutableSet<KtPackageScopeElement> {
        val root = PackageNode("")

        irFiles.forEach {
            if (it.packageFqName.asString().isNotBlank() && it.packageFqName.asString() != "<root>") {
                val path = KtPath(it.packageFqName.asString().split(".").toMutableList())
                root.getOrCreate(path).nodes.add(FileNode(it))
            } else {
                root.nodes.add(FileNode(it))
            }
        }
        return root.nodes.map { it.toPackageElement() }.toMutableSet()
    }

    override fun toString() = "<ROOT>"

    /**
     * Represents a node in a hierarchical structure of packages or files.
     * This interface is used to define the basic behavior of nodes that can be part of a package structure.
     */
    private interface Node {
        /**
         * Represents the name of a node (e.g., package, file, or other hierarchical elements).
         *
         * This property is used to uniquely identify a node within a hierarchical structure.
         * It is a core component for managing and navigating through package and file structures,
         * enabling operations such as retrieval, creation, and transformation.
         */
        val name: KtName

        /**
         * Converts the current node into a KtPackageScopeElement instance.
         *
         * @return A KtPackageScopeElement representation of the current node,
         *         containing its child package or file elements and hierarchical association.
         */
        fun toPackageElement(): KtPackageScopeElement
    }

    /**
     * Represents a node in the hierarchy used for managing file-level constructs in a package structure.
     * This node corresponds to an IR (Intermediate Representation) file and acts as an intermediary
     * to facilitate the transformation of files into package scope elements.
     *
     * @property irFile The IR file represented by this node.
     */
    private inner class FileNode(val irFile: IrFile) : Node {
        /**
         * Stores the name of the file associated with this node.
         *
         * This property represents the `KtName` of the IR (Intermediate Representation) file encapsulated
         * within this node. The name is derived directly from the `irFile` instance and serves as
         * an identifier for the file in the IR structure.
         */
        override val name: KtName = irFile.name

        /**
         * Converts the current node instance into a package scope element.
         *
         * @return A [KtPackageScopeElement] instance representing the package to which this node corresponds.
         */
        override fun toPackageElement(): KtPackageScopeElement {
            return KtIrFileElement(irFile, transformContext)
        }
    }

    /**
     * Represents a node within a package hierarchy, corresponding to a specific package or sub-package.
     * This class is used to build and manage a tree-like structure of packages, where each node may
     * contain child nodes and represents a particular level in the package structure.
     *
     * @property name The name of the package or sub-package represented by this node.
     * @property nodes A mutable list of child nodes, representing sub-packages or elements contained within the current package.
     */
    private inner class PackageNode(
        override val name: KtName,
        val nodes: MutableList<Node> = mutableListOf(),
    ) : Node {
        /**
         * Converts the current `PackageNode` instance and its child nodes into a hierarchical `KtPackageScopeElement`.
         * The resulting structure represents a deeply nested package hierarchy derived from the current node and its children.
         * Each child node's parent is set to the resulting element to maintain correct hierarchical references.
         *
         * @return A `KtPackageScopeElement` structure representing the converted package node and its nested hierarchy.
         */
        override fun toPackageElement(): KtPackageScopeElement {
            return KtIrPackageElement(
                name,
                transformContext,
                null,
                nodes.map { it.toPackageElement() }.toMutableList()
            ).apply {
                packageElements.forEach { it.parent = this }
            }
        }

        /**
         * Retrieves an existing `PackageNode` with the specified name or creates a new one if it does not exist.
         *
         * @param name The name of the `PackageNode` to retrieve or create.
         * @return The existing or newly created `PackageNode` associated with the given name.
         */
        fun getOrCreate(name: KtName): PackageNode {
            return nodes.find { it.name == name } as? PackageNode ?: PackageNode(name).also { nodes.add(it) }
        }

        /**
         * Retrieves or creates a hierarchical package node structure based on the given path.
         * Each part of the `KtPath` is used to either navigate an existing node or create a new one.
         * If a node does not exist for a specific part of the path, it will be instantiated.
         *
         * @param path The hierarchical path consisting of individual package names used to create or locate nodes.
         * @return The resulting or newly created package node corresponding to the provided path.
         */
        fun getOrCreate(path: KtPath): PackageNode {
            return path.parts.fold(this) { acc, name -> acc.getOrCreate(name) }
        }
    }
}