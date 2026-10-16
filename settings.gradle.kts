enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://mokelab.github.io/android-devportal/repo")
        }
    }
}

rootProject.name = "MokePlayground"
include(":app")
include(":core:devportal")
include(":core:ui")

// Feature modules to try are listed in local.properties, e.g.
//   features=compose:foundation:flexbox,compose:foundation:style
val enabledFeatures: List<String> = java.util.Properties().run {
    val file = settingsDir.resolve("local.properties")
    if (file.exists()) file.inputStream().use { load(it) }
    getProperty("features").orEmpty()
        .split(',')
        .map { it.trim() }
        .filter { it.isNotEmpty() }
}
enabledFeatures.forEach { name ->
    val dir = settingsDir.resolve("feature/" + name.replace(':', '/'))
    require(dir.resolve("build.gradle.kts").exists()) {
        "local.properties: feature '$name' listed in 'features' was not found ($dir)"
    }
    include(":feature:$name")
}
gradle.extra["playground.features"] = enabledFeatures
