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

package io.github.kshulzh.kefir.gradle.plugin

import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption
import java.io.File

/**
 * A Gradle plugin that integrates the Kefir Kotlin Compiler plugin into a Gradle project.
 * This plugin configures the compiler plugin artifact and applies custom configurations
 * required for the Kefir processing in Kotlin compilations.
 */
class KefirGradlePlugin : KotlinCompilerPluginSupportPlugin {
    /**
     * Companion object for the KefirGradlePlugin class. Contains constants that
     * represent metadata about the Kefir Gradle plugin, including its group name,
     * artifact name, and version number.
     */
    companion object {
        /**
         * Defines the group name used to identify the serialization plugin artifact.
         * This constant is utilized in Gradle-related configurations to specify the group
         * for resolving the plugin dependency.
         */
        const val SERIALIZATION_GROUP_NAME = "io.github.kshulzh.kefir"

        /**
         * Represents the name of the artifact used by the compiler plugin.
         *
         * This constant is utilized in the process of fetching or specifying
         * the artifact for the serialization plugin within the Kotlin Gradle plugin.
         */
        const val ARTIFACT_NAME = "compiler-plugin"

        /**
         * Represents the current version of the Kefir Gradle Plugin.
         *
         * This version identifier is applied to the plugin artifact and used
         * to define dependencies or configurations in the plugin's runtime.
         *
         * Example format: `<major>.<minor>.<patch>-<qualifier>`
         * where `-SNAPSHOT` indicates a non-final version under development.
         */
        const val VERSION_NUMBER = "0.0.1"
    }

    /**
     * Represents the configuration for the Kefir plugin in a Gradle project. This configuration is used to manage
     * custom dependencies and their resolved files for the plugin's functionality.
     *
     * It is created and initialized during the application of the plugin to a Gradle project and should be
     * accessed only after initialization. The configuration is configured to be resolvable and non-consumable,
     * meaning it is intended for retrieving dependency files without being directly consumed by other parts
     * of the build.
     */
    lateinit var kefirConfiguration: Configuration

    /**
     * Represents the Gradle Project associated with the plugin.
     *
     * This variable is used to initialize and configure the plugin for the
     * target project, and it plays a key role in managing dependencies and
     * compilation tasks during plugin application.
     *
     * This property is assigned in the `apply` function and is utilized in
     * other plugin-related logic to access project-specific configurations
     * and settings.
     */
    lateinit var project: Project

    /**
     * Applies the plugin to the given Kotlin compilation, configuring it to utilize necessary dependencies and options.
     *
     * @param kotlinCompilation the Kotlin compilation to which the plugin is being applied
     * @return a provider containing a list of subplugin options, each representing specific configurations and dependencies
     */
    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
        return kotlinCompilation.target.project.provider {
            listOf(
                SubpluginOption(
                    "kefirfiles",
                    collectKefirDependencies(project).values.joinToString(";;") { dep -> dep.joinToString(";") { it.absolutePath } })
            )
        }
    }

    /**
     * Returns the unique identifier for the compiler plugin associated with this Gradle plugin.
     *
     * @return the unique compiler plugin ID as a string
     */
    override fun getCompilerPluginId(): String = "kefir"

    /**
     * Retrieves the artifact information required for the plugin.
     *
     * @return the artifact descriptor containing the group name, artifact name, and version number.
     */
    override fun getPluginArtifact(): SubpluginArtifact =
        SubpluginArtifact(SERIALIZATION_GROUP_NAME, ARTIFACT_NAME, VERSION_NUMBER)

    /**
     * Determines whether the plugin is applicable to the given Kotlin compilation.
     *
     * @param kotlinCompilation The Kotlin compilation context being evaluated.
     * @return `true` if the plugin is applicable to the provided Kotlin compilation, otherwise `false`.
     */
    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean {
        return true
    }

    /**
     * Applies the Kefir Gradle plugin to the given project. This sets up the "kefir" configuration
     * in the target project for handling Kefir-related dependencies and configurations.
     *
     * @param target the Gradle project to which the plugin is being applied
     */
    override fun apply(target: Project) {
        kefirConfiguration = target.configurations.create("kefir").apply {
            isCanBeConsumed = false
            isCanBeResolved = true
            description = "Kefir files"
            isVisible = false
        }
        project = target
        configureKefirDependencies(target)

        super.apply(target)
    }

    /**
     * Configures build dependencies to ensure proper build ordering when using kefir configuration.
     * This method ensures that any project dependencies added to the kefir configuration
     * are built before the current project's compilation tasks.
     */
    private fun configureKefirDependencies(project: Project) {
        project.afterEvaluate {
            kefirConfiguration.dependencies.forEach { dependency ->
                if (dependency is ProjectDependency) {
                    val dependentProject = dependency.dependencyProject

                    // Ensure the dependent project is built before this project's compile tasks
                    project.tasks.matching { task ->
                        task.name.startsWith("compile") || task.name.contains("Compile")
                    }.configureEach { compileTask ->
                        compileTask.dependsOn("${dependentProject.path}:build")
                    }

                    // Also ensure it's built before Kotlin compilation specifically
                    project.tasks.matching { task ->
                        task.name.contains("compileKotlin")
                    }.configureEach { kotlinCompileTask ->
                        kotlinCompileTask.dependsOn("${dependentProject.path}:compileKotlin")
                    }
                }
            }
        }
    }


    /**
     * Collects and resolves Kefir dependencies from the project's Kefir configuration.
     * This method generates a mapping of dependency notations to the corresponding resolved files.
     *
     * @param project the Gradle project for which the dependencies are being collected
     * @return a map where the keys are dependency notations (in the form "group:name:version")
     *         and the values are lists of files resolved for each dependency
     */
    private fun collectKefirDependencies(project: Project): Map<String, List<File>> {
        if (!this::kefirConfiguration.isInitialized) return emptyMap()

        val result = mutableMapOf<String, List<File>>()

        // Iterate over each declared dependency
        kefirConfiguration.dependencies.forEach { dep: Dependency ->
            val depNotation = listOfNotNull(dep.group, dep.name, dep.version)
                .joinToString(":")
                .ifEmpty { dep.name }

            // Create a detached configuration for this dependency only
            val singleConfig = project.configurations.detachedConfiguration(dep).apply {
                isCanBeResolved = true
            }

            // Resolve and collect the files
            val files = singleConfig.resolve().toList()
            result[depNotation] = files
        }

        return result
    }
}