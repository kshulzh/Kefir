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

import io.github.kshulzh.kefir.compiler.plugin.KefirCompilerComponentRegistrar.Companion.SOURCES_KEY
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.config.CompilerConfiguration
import java.io.File

/**
 * Class responsible for processing command-line arguments for the Kefir compiler plugin.
 * Implements the CommandLineProcessor interface to handle plugin-specific options.
 */
class KefirCommandLineProcessor : CommandLineProcessor {

    /**
     * Identifier for the compiler plugin.
     * Used to uniquely specify the plugin within the Kotlin compiler infrastructure.
     */
    override val pluginId: String = "kefir"

    /**
     * Represents the collection of CLI options specific to the Kefir compiler plugin.
     * This property defines the custom options that can be passed to the compiler for this plugin.
     *
     * The options in this collection are used by the compiler to configure the plugin
     * and enable functionality based on user-provided arguments.
     */
    override val pluginOptions: Collection<CliOption> = listOf(CliOption("kefirfiles", "set", ""))

    /**
     * Processes a given command-line option and updates the compiler configuration accordingly.
     *
     * @param option The command-line option to be processed.
     * @param value The value associated with the given option.
     * @param configuration The compiler configuration that will be updated based on the option.
     */
    override fun processOption(
        option: AbstractCliOption,
        value: String,
        configuration: CompilerConfiguration
    ) {
        when (option.optionName) {
            "kefirfiles" -> configuration.put(
                SOURCES_KEY,
                value.split(";;").map { it.split(";").map { file -> File(file) } })

            else -> {}
        }
    }
}