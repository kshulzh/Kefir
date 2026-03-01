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

import io.github.kshulzh.kefir.transform.transformer.FirTransformer
import io.github.kshulzh.kefir.transform.utils.KtFirStructure
import io.github.kshulzh.problemgraph.context.ProblemContext
import org.jetbrains.kotlin.fir.FirSession

/**
 * Implementation of the `KtFirTransformContext` interface, providing a concrete context for
 * Kotlin FIR (Frontend Intermediate Representation) transformations. This class encapsulates
 * the FIR session, structure, transformation tools, and associated attributes, offering a
 * comprehensive environment for transforming Kotlin code into its FIR equivalent.
 *
 * @property firSession The FIR session for managing and analyzing Kotlin FIR elements.
 * @property firStructure A structure used for handling FIR declarations during transformations,
 *                        providing operations for adding files, classes, and callable declarations.
 * @property problemContext An optional component for managing problem-specific metadata or states
 *                          associated with complex transformations or debugging.
 * @property attributes Represents a mutable collection of context-specific properties, useful for
 *                      dynamically storing additional data during the transformation process.
 * @property firTransform An instance of `FirTransformer`, allowing the application of FIR transformations
 *                        on various kinds of Kotlin elements such as declarations, expressions, types,
 *                        and statements.
 */
class KtFirTransformContextImpl(
    override val firSession: FirSession,
    override val firStructure: KtFirStructure,
    override val problemContext: ProblemContext?,
    override var attributes: MutableMap<String, Any> = mutableMapOf(),
    override val firTransform: FirTransformer = FirTransformer(),
) : KtFirTransformContext