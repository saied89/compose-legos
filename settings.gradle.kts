import org.gradle.api.initialization.resolve.RepositoriesMode

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        google()
        mavenCentral()
        maven("https://androidx.dev/snapshots/builds/8683091/artifacts/repository")
    }
}
rootProject.name = "ComposeLegos"
include(":app")
include(":samples")
include(":processor")
include(":processor-api")
include(":ideaplugin")
