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

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import io.github.kshulzh.kefir.api.KtProcessorImpl
import io.github.kshulzh.kefir.api.Processor
import org.intellij.lang.annotations.Language

/**
 * Represents a compilation context that allows adding Kotlin and Java source files
 * and processing them using a custom or default annotation processor.
 * Provides a simplified interface to interact with the Kotlin compilation framework
 * during testing or dynamic compilation scenarios.
 *
 * @property sources A mutable list of source files that are part of this compilation.
 * @property initializer A lambda function executed during the initialization of the instance,
 * allowing further customization or setup.
 */
class KefirCompilation(
    val sources: MutableList<SourceFile> = mutableListOf(),
    val initializer: KefirCompilation.() -> Unit = {}
) {
    init {
        initializer()
    }

    /**
     * Adds a Kotlin source file to the compilation sources.
     *
     * @param name the name of the Kotlin source file.
     * @param source the Kotlin source code as a string.
     * @return true if the source file was successfully added, false otherwise.
     */
    fun kotlin(name: String, @Language("kotlin") source: String) = sources.add(SourceFile.kotlin(name, source))

    /**
     * Adds a Java source file to the collection of source files.
     *
     * @param name the name of the Java source file
     * @param source the source code of the Java file
     */
    fun java(name: String, @Language("java") source: String) = sources.add(SourceFile.java(name, source))

    /**
     * Processes the compilation using a provided annotation processor and compiler plugin.
     *
     * The method integrates custom logic into Kotlin compilation by utilizing
     * a specified `Processor` implementation. It accommodates plugin registrars
     * and annotation processors, compiles the sources, and returns the results.
     *
     * @param processor A lambda function defining custom processing logic. Defaults to an empty processor if not provided.
     */
    infix fun process(processor: Processor = {}) = KefirResults(
        KotlinCompilation().apply {
            sources = this@KefirCompilation.sources

            // pass your own instance of an annotation processor
            annotationProcessors = listOf()

            // pass your own instance of a compiler plugin
            compilerPluginRegistrars = listOf(KefirTestComponentRegistrar(KtProcessorImpl(processor)))
            commandLineProcessors = listOf()

            inheritClassPath = true
            messageOutputStream = System.out // see diagnostics in real time
        }.compile()
    )
}