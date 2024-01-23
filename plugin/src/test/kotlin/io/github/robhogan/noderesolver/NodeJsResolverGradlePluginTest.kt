/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package io.github.robhogan.noderesolver

import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test

class NodeJsResolverGradlePluginTest {
    @Test fun `plugin adds resolveNodeJsPackage to extraProperties`() {
        // Create a test project and apply the plugin
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("io.github.robhogan.noderesolver")

        // Verify the result
        assert(project.extensions.extraProperties.has("resolveNodeJsPackage"))
    }
}