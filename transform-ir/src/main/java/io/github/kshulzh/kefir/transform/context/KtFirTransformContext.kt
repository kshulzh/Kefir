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

import io.github.kshulzh.kefir.model.api.KtAttributes
import io.github.kshulzh.kefir.model.api.io.KtPackageScope
import io.github.kshulzh.kefir.transform.transformer.FirTransformer
import io.github.kshulzh.kefir.transform.utils.KtFirStructure
import io.github.kshulzh.problemgraph.context.ProblemContext
import org.jetbrains.kotlin.fir.FirSession

/**
 * Represents a transformation context specifically designed for working with FIR (Frontend IR) structures.
 * This interface provides access to essential components required for FIR transformation, such as the session,
 * transformer, structure, and attributes, along with an optional problem analysis context.
 */
interface KtFirTransformContext : KtAttributes {
    /**
     * Provides access to the `FirTransformer` instance used for performing transformations
     * on FIR (Frontend Intermediate Representation) elements within this context.
     *
     * This transformer is typically employed to convert various Kotlin code elements
     * into their corresponding FIR representations. It supports transformation operations
     * for different kinds of elements such as files, declarations, expressions, types,
     * statements, and other Kotlin language constructs.
     */
    val firTransform: FirTransformer

    /**
     * Represents the Kotlin FIR (Frontend Intermediate Representation) session.
     * The FIR session provides access to various components and utilities related
     * to the analysis and transformation of Kotlin FIR elements, such as symbol
     * providers, declared member scopes, and cached symbol names. It is tightly
     * integrated with the `FirModuleDescriptor` associated with the current module.
     */
    val firSession: FirSession

    /**
     * Represents a structure for handling FIR declarations during a Kotlin FIR transformation process.
     * Provides operations for adding files, classes, and callable declarations into the current transformation context.
     * It is part of the `KtFirTransformContext` and is used to organize FIR element management when applying transformations.
     */
    val firStructure: KtFirStructure

    /**
     * Represents an optional context for managing problem-specific metadata or states during
     * transformation processes in the Kotlin FIR (Frontend IR) or IR (Intermediate Representation) pipeline.
     *
     * This context is particularly useful for associating domain-specific information with the ongoing
     * transformation, enabling better handling of complex transformations or debugging specific issues.
     *
     * The presence of `problemContext` depends on the implementation and usage within a particular
     * transformation context, and it may be null if not explicitly set.
     */
    val problemContext: ProblemContext?

    val root: KtPackageScope

    val externalRoot: KtPackageScope
}