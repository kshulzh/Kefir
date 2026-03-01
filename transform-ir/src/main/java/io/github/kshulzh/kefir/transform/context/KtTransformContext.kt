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

package io.github.kshulzh.kefir.transform.context

import io.github.kshulzh.kefir.transform.transformer.IrTransformer
import io.github.kshulzh.kefir.transform.utils.KtIrStructure
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment

/**
 * KtTransformContext represents a context for transforming Kotlin IR (Intermediate Representation) elements
 * within a specific scope, typically used for plugin and code transformation purposes.
 * It extends the KtFirTransformContext, incorporating FIR (Frontend IR) capabilities, while specializing in
 * handling and interacting with the IR structures of a Kotlin compilation process.
 *
 * This interface provides access to essential IR-related components, such as the module fragment, the IR transformer,
 * and the plugin context, making it suitable for transforming and analyzing IR elements during the execution phase.
 */
interface KtTransformContext : KtFirTransformContext {
    /**
     * Represents the Intermediate Representation (IR) module fragment within the transformation context.
     *
     * The `moduleFragment` contains the entire IR corresponding to a Kotlin module. It serves as the root
     * node in the IR structure and provides access to all declarations and statements within the module.
     * This property is primarily used during the IR transformation process to traverse, analyze, or modify
     * the IR elements.
     */
    val moduleFragment: IrModuleFragment

    /**
     * Represents an instance of an [IrTransformer] used within the transformation
     * context to operate on various Kotlin IR (Intermediate Representation) elements.
     *
     * The [irTransform] serves as a core utility within the transformation pipeline,
     * providing functionality to convert or process different Kotlin IR entities such as
     * files, declarations, expressions, and more.
     *
     * This variable is primarily used in the context of intermediate representation (IR) transformation
     * processes, allowing developers to systematically transform or manipulate IR nodes during
     * compilation or plugin development.
     */
    val irTransform: IrTransformer

    /**
     * Provides access to the `IrPluginContext` instance, which is used during the transformation process
     * in the Kotlin Intermediate Representation (IR) pipeline.
     *
     * The `IrPluginContext` offers essential utilities and services necessary for performing modifications
     * and analysis of the IR. This includes features like symbol resolution, type translation, and integration
     * with compiler plugins. It serves as an entry point for interacting with and transforming IR elements
     * within the current compilation module.
     */
    val pluginContext: IrPluginContext

    /**
     * Represents a structure for managing elements related to the IR (Intermediate Representation)
     * transformation process in Kotlin. Provides utilities for maintaining and modifying
     * IR-related components and metadata during the transformation of Kotlin code into IR.
     *
     * Primarily used within the context of a transformation pipeline where FIR (Frontend Intermediate
     * Representation) elements are converted into their corresponding IR counterparts. It acts as
     * a storage and handling mechanism for IR classifiers and other IR entities.
     */
    val irStructure: KtIrStructure
}