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

        // ─── Yodo1 MAS mediation adapter repositories ────────────────────
        // Listed under "Required Maven Repositories" in
        // https://developers.yodo1.com/docs/sdk/guides/android/integration
        maven {
            name = "IronSourceAdsRepo"
            url = uri("https://android-sdk.is.com")
            content { includeGroupByRegex("com\\.ironsource.*") }
        }
        maven {
            name = "PangleAdsRepo"
            url = uri("https://artifact.bytedance.com/repository/pangle")
            content {
                includeGroup("com.pangle.global")
                includeGroup("com.bytedance.sdk")
            }
        }
        maven {
            name = "MintegralAdsRepo"
            url = uri("https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_oversea")
            content { includeGroup("com.mbridge.msdk.oversea") }
        }
        maven {
            name = "BidMachineAdsRepo"
            url = uri("https://artifactory.bidmachine.io/bidmachine")
            content {
                includeGroup("io.bidmachine")
                includeGroupByRegex("io\\.bidmachine\\..*")
                includeGroup("com.explorestack")
                includeGroupByRegex("com\\.explorestack\\..*")
            }
        }
        maven {
            name = "YSONetworkRepo"
            url = uri("https://ysonetwork.s3.eu-west-3.amazonaws.com/sdk/android")
            content { includeGroup("com.ysocorp") }
        }
        maven {
            name = "PubMaticAdsRepo"
            url = uri("https://repo.pubmatic.com/artifactory/public-repos")
            content { includeGroup("com.pubmatic.sdk") }
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
            name = "AnythinkAdsRepo"
            url = uri("https://jfrog.anythinktech.com/artifactory/overseas_sdk")
            content {
                includeGroup("com.anythink.android")
                includeGroupByRegex("com\\.anythink\\..*")
            }
        }
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
