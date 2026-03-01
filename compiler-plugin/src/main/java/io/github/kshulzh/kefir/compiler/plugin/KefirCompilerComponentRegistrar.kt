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


import io.github.kshulzh.kefir.api.KtCompositeProcessor
import io.github.kshulzh.kefir.api.KtProcessor
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.cli.common.messages.getLogger
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.CompilerConfigurationKey
import java.io.File
import java.net.URLClassLoader
import java.util.*

/**
 * A compiler plugin registrar that serves as an entry point for registering custom compiler
 * extensions focused on Intermediate Representation (IR) processing in Kotlin.
 *
 * This registrar interfaces with the compiler configuration to load and apply extensions
 * dynamically based on provided input sources. It supports capabilities to integrate
 * multiple IR processors for custom transformations during the compilation process.
 *
 * @constructor Initializes the `KefirCompilerComponentRegistrar` with support for the K2 compiler backend.
 */
class KefirCompilerComponentRegistrar : CompilerPluginRegistrar() {

    /**
     * Companion object for the `KefirCompilerComponentRegistrar` class.
     * Provides a unique configuration key used to retrieve source files
     * for the compiler component registration process.
     */
    companion object {
        /**
         * A configuration key used to store a nested list of files for the compiler plugin.
         *
         * This key is primarily used within the plugin to manage and process additional
         * source files or resources in the form of lists of file paths.
         *
         * The stored data structure is a list where each element is itself a list of `File`
         * objects, representing grouped collections of files that are utilized during plugin
         * execution. The grouping of files may vary based on specific plugin requirements.
         *
         * The key is registered and accessed through a `CompilerConfiguration` instance,
         * enabling the plugin to retrieve and use the file information during compilation.
         */
        val SOURCES_KEY = CompilerConfigurationKey.create<List<List<File>>>("kefirfiles")
    }

    /**
     * Indicates whether the compiler plugin supports the K2 architecture.
     *
     * K2 is a new front-end implementation for Kotlin compiler, which introduces
     * improved architecture for better performance and scalability. This property
     * allows determining if the plugin is compatible with and designed for the K2 compiler.
     */
    override val supportsK2: Boolean = true

    override val pluginId: String = "kefir"

    /**
     * Registers extensions into the compiler during the relevant phase of initialization.
     *
     * This method primarily configures the Intermediate Representation (IR) processing by
     * dynamically loading plugins, processing extensions, and registering IR processors
     * encapsulated by `KtProcessor` instances.
     *
     * @param configuration The compiler configuration containing the necessary settings and options.
     *                       This may include file paths, logging capabilities, and other
     *                       plugin-related configurations.
     */
    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        val irProcessors = mutableListOf<KtProcessor>()
        configuration.get(SOURCES_KEY)?.forEach { files ->
            val classLoader = URLClassLoader(
                files.map { it.toURI().toURL() }.toTypedArray(),
                KefirComponentRegistrar::class.java.classLoader
            )
            val registrars = ServiceLoader.load(
                KefirComponentRegistrar::class.java,
                classLoader
            ).toList()
            registrars.forEach {
                it.apply {
                    registerExtensions(configuration)
                }
                irProcessors.addAll(it.irExtensions)
            }
        } ?: configuration.getLogger().warning("No files")

        if (irProcessors.isNotEmpty()) {
            IrGenerationExtension.registerExtension(KefirIrExtension(KtCompositeProcessor(irProcessors)))
        } else {
            configuration.getLogger().warning("No processors")
        }
    }
}