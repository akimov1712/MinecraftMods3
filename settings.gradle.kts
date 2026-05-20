pluginManagement {
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
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MinecraftMods3"
include(":app")
include(":data")
include(":domain")
include(":core")
include(":core:ui")
include(":core:android")
include(":core:common")
include(":feature")
include(":feature:splash")
include(":feature:tabs")
include(":feature:browse")
include(":feature:saved")
include(":feature:propose")
include(":feature:help")
include(":navigation")
include(":feature:mod")
