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

import io.github.kshulzh.problemgraph.action.NodeActionImpl

/**
 * Represents an interface for managing a stack of node actions.
 * This stack is used to handle transformation-related operations
 * during the processing of nodes in a Kotlin compiler context.
 */
interface KtNodeStack {
    /**
     * Represents a stack used to manage node actions during transformation processes.
     * This stack is implemented as an `ArrayDeque` and stores elements of type `NodeActionImpl<T>`.
     * It is used to maintain a record of actions or operations to be performed on nodes
     * within a transformation context, allowing for efficient retrieval and manipulation.
     */
    val nodeStack: ArrayDeque<NodeActionImpl<*>>
}