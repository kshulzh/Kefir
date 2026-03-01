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

package io.github.kshulzh.kefir.test

import io.github.kshulzh.kefir.api.KtProcessor
import io.github.kshulzh.kefir.compiler.plugin.KefirIrExtension
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration

/**
 * A Kotlin Compiler Plugin Registrar for registering the Kefir IR transformation extension.
 *
 * The `KefirTestComponentRegistrar` manages the integration of the `KefirIrExtension`
 * that processes Kotlin IR and FIR structures using a provided `KtProcessor`.
 *
 * @property processor An instance of `KtProcessor` that defines the processing
 * logic to be executed during the IR generation phase.
 */
class KefirTestComponentRegistrar(
    val processor: KtProcessor
) : CompilerPluginRegistrar() {
    /**
     * Represents the unique identifier for the Kefir compiler plugin.
     * This ID is used to register and identify the plugin within the
     * Kotlin compiler infrastructure.
     */
    override val pluginId: String = "kefir"

    /**
     * Indicates whether the plugin supports the K2 compiler infrastructure.
     *
     * When set to `true`, this property confirms compatibility with the K2
     * (new generation Kotlin compiler) infrastructure, enabling the plugin
     * to function correctly within the new compiler framework.
     */
    override val supportsK2: Boolean = true

    /**
     * Registers the required extensions for Kotlin compilation within the current extension storage context.
     *
     * @param configuration The compiler configuration instance containing settings for the plugin.
     */
    override fun ExtensionStorage.registerExtensions(
        configuration: CompilerConfiguration
    ) {
        IrGenerationExtension.Companion.registerExtension(KefirIrExtension(processor))
    }
}