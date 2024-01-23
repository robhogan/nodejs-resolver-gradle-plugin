package io.github.robhogan.noderesolver

import org.gradle.api.GradleException
import java.io.File

open class NodeResolverGradlePluginExtension {
    fun resolvePackage(packageName: String, startDir: File): File {
        val candidate = File(startDir, "node_modules/$packageName")
        if (candidate.exists()) return candidate.canonicalFile

        val parentDir = startDir.parentFile
        if (parentDir == null || startDir.canonicalPath == parentDir.canonicalPath) {
            throw GradleException("Failed to find the package '$packageName'. Ensure you have installed node_modules.")
        }
        return resolvePackage(packageName, parentDir)
    }
}