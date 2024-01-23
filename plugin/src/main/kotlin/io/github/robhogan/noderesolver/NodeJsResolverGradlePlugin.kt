package io.github.robhogan.noderesolver

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import org.gradle.api.plugins.PluginAware
import groovy.lang.Closure
import org.gradle.api.GradleException
import org.gradle.api.plugins.ExtensionAware
import java.io.File

class NodeJsResolverGradlePlugin : Plugin<PluginAware> {
    private fun resolvePackage(packageName: String, startDir: File): File {
        val candidate = File(startDir, "node_modules/$packageName")
        if (candidate.exists()) return candidate.canonicalFile

        val parentDir = startDir.parentFile
        if (parentDir == null || startDir.canonicalPath == parentDir.canonicalPath) {
            throw GradleException("Failed to find the package '$packageName'. Ensure you have installed node_modules.")
        }
        return resolvePackage(packageName, parentDir)
    }

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
        val resolveNodeJsPackageClosure = object : Closure<Unit>(this) {
            fun doCall(packageName: String): File {
                return doCall(packageName, rootProjectDir)
            }

            fun doCall(packageName: String, startDir: File): File {
                return resolvePackage(packageName, startDir)
            }
        }
        target.extensions.extraProperties["resolveNodeJsPackage"] = resolveNodeJsPackageClosure
    }
}
