package io.github.robhogan.noderesolver

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import org.gradle.api.plugins.PluginAware
import groovy.lang.Closure
import org.gradle.api.plugins.ExtensionAware
import java.io.File

class ReactNodejsResolverGradlePlugin : Plugin<PluginAware> {
    override fun apply(target: PluginAware) {
        if (target !is ExtensionAware) {
            throw IllegalStateException("Unsupported plugin target: ${target.javaClass.name} is not ExtensionAware")
        }

        val rootProjectDir = when (target) {
            is Settings -> target.rootProject.projectDir
            is Project -> target.rootProject.projectDir
            else -> throw IllegalStateException("Unsupported plugin target: ${target.javaClass.name} is not Project or Settings")
        }

        // For convenience and brevity from Groovy scripts, expose a global 'resolveNodePackage'
        // closure via extensions.extraProperties
        val resolveNodePackageClosure = object : Closure<Unit>(this) {
            fun doCall(packageName: String): File {
                return doCall(packageName, rootProjectDir)
            }

            fun doCall(packageName: String, startDir: File): File {
                return NodeResolverGradlePluginExtension().resolvePackage(packageName, startDir)
            }
        }
        target.extensions.extraProperties["resolveNodePackage"] = resolveNodePackageClosure
    }
}