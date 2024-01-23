/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Gradle plugin project to get you started.
 * For more details on writing Custom Plugins, please refer to https://docs.gradle.org/8.5/userguide/custom_plugins.html in the Gradle documentation.
 */

group = "io.github.robhogan"
version = "0.2.0-alpha-3"

plugins {
    // Apply the Java Gradle plugin development plugin to add support for developing Gradle plugins
    `java-gradle-plugin`

    // Apply the Kotlin JVM plugin to add support for Kotlin.
    alias(libs.plugins.jvm)

    `maven-publish`
    id("com.gradle.plugin-publish") version "1.2.1"
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use the Kotlin JUnit 5 integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    implementation(gradleApi())
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}


gradlePlugin {
    website = "https://reactnative.dev"
    vcsUrl = "https://github.com/facebook/react-native"
    plugins {
        create("nodeResolver") {
            id = "io.github.robhogan.noderesolver"
            displayName = "React Node.js resolver plugin"
            description = "A Gradle Plugin for correctly resolving node_modules paths from your build/settings.gradle"
            tags.set(listOf("react", "node", "nodejs", "node_modules", "resolver", "resolution"))
            implementationClass = "io.github.robhogan.noderesolver.ReactNodejsResolverGradlePlugin"
        }
    }
}

tasks.named<Test>("test") {
    // Use JUnit Jupiter for unit tests.
    useJUnitPlatform()
}

publishing {
    repositories {
        maven {
            name = "localPluginRepository"
            url = uri("../local-plugin-repository")
        }
    }
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = "io.github.robhogan"
            artifactId = "noderesolver"
        }

        // Publication for the plugin marker
        create<MavenPublication>("pluginMarkerMaven") {
            groupId = "io.github.robhogan"
            artifactId = "io.github.robhogan.noderesolver.gradle.plugin"
            pom {
                withXml {
                    asNode().appendNode("dependencies").appendNode("dependency").apply {
                        appendNode("groupId", "io.github.robhogan")
                        appendNode("artifactId", "noderesolver")
                        appendNode("version", version)
                    }
                }
            }
        }
    }
}
