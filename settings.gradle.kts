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
        maven("https://jitpack.io")
        maven {
            name = "MintegralAdsRepo"
            url = uri("https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_oversea")
            content { includeGroup("com.mbridge.msdk.oversea") }
        }
        maven {
            name = "PangleAdsRepo"
            url = uri("https://artifact.bytedance.com/repository/pangle")
            content { includeGroup("com.pangle.global") }
        }
        maven {
            name = "ChartboostAdsRepo"
            url = uri("https://cboost.jfrog.io/artifactory/chartboost-ads/")
            content {
                includeGroup("com.chartboost")
                includeGroup("com.iab.omid.library")
            }
        }
        maven {
            name = "YSONetworkRepo"
            url = uri("https://ysonetwork.s3.eu-west-3.amazonaws.com/sdk/android")
            content { includeGroup("com.ysocorp") }
        }
        maven {
            name = "OguryAdsRepo"
            url = uri("https://maven.ogury.co")
            content {
                includeGroup("co.ogury")
                includeGroup("co.ogury.module")
            }
        }
        maven {
            name = "SmaatoAdsRepo"
            url = uri("https://s3.amazonaws.com/smaato-sdk-releases/")
            content {
                includeGroup("com.smaato.android.sdk")
                includeGroup("com.verve")
            }
        }
        maven {
            name = "SuperAwesomeRepo"
            url = uri("https://aa-sdk.s3-eu-west-1.amazonaws.com/android_repo/")
            content { includeGroup("tv.superawesome.sdk.publisher") }
        }
        maven("https://artifactory-external.vkpartner.ru/artifactory/maven")
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
include(":core:ads")
include(":feature")
include(":feature:splash")
include(":feature:tabs")
include(":feature:browse")
include(":feature:saved")
include(":feature:propose")
include(":feature:help")
include(":navigation")
include(":feature:mod")
include(":feature:files")
include(":feature:guide")
