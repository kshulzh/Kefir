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

package io.github.kshulzh.kefir.compiler.plugin

import io.github.kshulzh.kefir.api.KtProcessor
import io.github.kshulzh.kefir.api.KtProcessorImpl
import io.github.kshulzh.kefir.api.Processor
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar

/**
 * Represents an abstract base class for registering components or extensions
 * to augment the compiler's behavior, specifically related to the Intermediate Representation (IR) phase.
 * It extends `CompilerPluginRegistrar` to enable customization using the plugin system.
 *
 * This class provides support for Kotlin's K2 compiler and allows dynamic registration
 * of processing logic encapsulated in `KtProcessor` instances. Furthermore, it offers mechanisms
 * to integrate these processors into the compilation process, enabling tasks like code generation
 * or transformation during the IR phase.
 */
abstract class KefirComponentRegistrar : CompilerPluginRegistrar() {

    /**
     * Indicates whether the plugin supports the new K2 compiler backend.
     *
     * When set to `true`, the compiler plugin registrar ensures compatibility with the K2 compiler
     * infrastructure, enabling plugins to operate within the context of the updated Kotlin architecture.
     *
     * This value allows conditional behavior based on the compiler version and backend in use,
     * ensuring that plugins can adapt and perform as expected.
     */
    override val supportsK2: Boolean = true

    override val pluginId: String = "kefir"

    /**
     * A mutable list to hold instances of `KtProcessor` that can be registered and subsequently used
     * for IR processing tasks during compilation.
     *
     * `irExtensions` serves as a container for processors that extend the functionality of the IR
     * generation phase within the compiler by injecting custom transformation logic or behavior.
     *
     * This property is typically utilized in the context of `KefirComponentRegistrar` to dynamically
     * register and manage a collection of processors which are later consumed by an IR generation
     * extension, such as `KefirIrExtension`.
     */
    val irExtensions: MutableList<KtProcessor> = mutableListOf()

    /**
     * Registers an IR (Intermediate Representation) extension for processing Kotlin packages.
     * Adds the given `KtProcessor` instance to the list of IR extensions maintained by the
     * `KefirComponentRegistrar` class.
     *
     * @param processor The `KtProcessor` implementation that defines custom processing logic
     *                  for Kotlin package scopes within the context of code generation or transformation.
     */
    fun registerIrExtension(processor: KtProcessor) {
        irExtensions.add(processor)
    }

    /**
     * Registers an intermediate representation (IR) processing extension.
     *
     * This method converts a provided `Processor` into a `KtProcessor` implementation
     * using `KtProcessorImpl`, and then delegates the registration of the IR extension
     * to the `registerIrExtension(KtProcessor)` method.
     *
     * @param processor A `Processor` instance defining the logic to be processed
     * in the Kotlin IR transformation context.
     */
    fun registerIrExtension(processor: Processor) {
        registerIrExtension(KtProcessorImpl(processor))
    }
}