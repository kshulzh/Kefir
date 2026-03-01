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

import org.jetbrains.kotlin.KtLightSourceElement
import org.jetbrains.kotlin.KtSourceElement
import org.jetbrains.kotlin.com.intellij.lang.LighterASTNode
import org.jetbrains.kotlin.com.intellij.openapi.util.Ref
import org.jetbrains.kotlin.com.intellij.psi.tree.IElementType
import org.jetbrains.kotlin.com.intellij.util.diff.FlyweightCapableTreeStructure

/**
 * Creates a source element represented by a `KtLightSourceElement`.
 *
 * @return An instance of `KtSourceElement` which wraps a `LighterASTNode` and associated tree structure.
 */
fun createSource(): KtSourceElement {
    val lighterAST = object : LighterASTNode {
        /**
         * Retrieves the type of the token associated with this element.
         *
         * @return the token type represented as an instance of IElementType, or null if the type is not defined.
         */
        override fun getTokenType(): IElementType? {
            TODO("Not yet implemented")
        }

        /**
         * Retrieves the start offset of this element in the source code.
         *
         * @return the start offset as an integer
         */
        override fun getStartOffset(): Int {
            TODO("Not yet implemented")
        }

        /**
         * Returns the end offset of an element.
         *
         * @return the end offset as an integer
         */
        override fun getEndOffset(): Int {
            TODO("Not yet implemented")
        }

    }
    val flyweight = object : FlyweightCapableTreeStructure<LighterASTNode> {
        /**
         * Retrieves the root node of the Lighter Abstract Syntax Tree (AST).
         *
         * @return the root node of the LighterAST.
         */
        override fun getRoot(): LighterASTNode {
            TODO("Not yet implemented")
        }

        /**
         * Retrieves the parent node of the specified `LighterASTNode` in the abstract syntax tree.
         *
         * @param p0 the node whose parent is to be retrieved
         * @return the parent node of the specified `LighterASTNode`, or null if the node does not have a parent
         */
        override fun getParent(p0: LighterASTNode): LighterASTNode? {
            TODO("Not yet implemented")
        }

        /**
         * Retrieves the children of the given node and stores them in the provided reference.
         *
         * @param p0 the parent node whose children are being retrieved
         * @param p1 a reference to an array where the children of the node will be stored
         * @return the number of children of the given node
         */
        override fun getChildren(
            p0: LighterASTNode,
            p1: Ref<Array<out LighterASTNode?>?>
        ): Int {
            p1.set(arrayOf())
            return 0
        }

        /**
         * Disposes the specified child nodes up to the provided count.
         *
         * @param p0 An array of child nodes to be disposed.
         * @param p1 The count of nodes from the array to be disposed.
         */
        override fun disposeChildren(
            p0: Array<out LighterASTNode?>?,
            p1: Int
        ) {
            TODO("Not yet implemented")
        }

        /**
         * Converts the provided `LighterASTNode` instance into its string representation.
         *
         * @param p0 the `LighterASTNode` instance to be converted to a `CharSequence`
         * @return a `CharSequence` representing the string form of the given node
         */
        override fun toString(p0: LighterASTNode): CharSequence {
            TODO("Not yet implemented")
        }

        /**
         * Returns the starting offset of the given LighterASTNode.
         *
         * @param p0 the LighterASTNode for which the start offset is required
         * @return the starting offset of the specified LighterASTNode as an integer
         */
        override fun getStartOffset(p0: LighterASTNode): Int {
            TODO("Not yet implemented")
        }

        /**
         * Retrieves the ending offset of the given `LighterASTNode`.
         *
         * @param p0 the `LighterASTNode` for which the ending offset is to be calculated.
         * @return the ending offset of the node.
         */
        override fun getEndOffset(p0: LighterASTNode): Int {
            TODO("Not yet implemented")
        }

    }
    return KtLightSourceElement(
        lighterAST,
        0,
        0,
        flyweight
    )
}