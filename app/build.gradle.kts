plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
}

val applicationArtifactId: String =
    property("applicationName")?.toString() ?: throw GradleException("Not found properties \"applicationName\"")
val appVersionCode: Int =
    property("versionCode")?.toString()?.toInt() ?: throw GradleException("Not found properties \"versionCode\"")
val appVersionName: String =
    property("versionName")?.toString() ?: throw GradleException("Not found properties \"versionName\"")
val metricaApiKey: String =
    property("metricaApiKey")?.toString() ?: throw GradleException("Not found properties \"metricaApiKey\"")

android {
    namespace = "dev.akmvxx.app"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.appsystemdev.$applicationArtifactId"
        minSdk = 26
        targetSdk = 36
        versionCode = appVersionCode
        versionName = appVersionName

        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "APPLICATION_ID", "\"$applicationId\"")
        buildConfigField("String", "METRICA_API_KEY", "\"$metricaApiKey\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)

    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)

    implementation(libs.appmetrica.analytics)
    implementation(libs.play.review.ktx)

    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":core:ui"))
    implementation(project(":core:android"))
    implementation(project(":core:common"))
    implementation(project(":feature:tabs"))
    implementation(project(":feature:splash"))
    implementation(project(":feature:browse"))
    implementation(project(":feature:saved"))
    implementation(project(":feature:propose"))
    implementation(project(":feature:help"))
    implementation(project(":feature:mod"))
    implementation(project(":feature:files"))
    implementation(project(":feature:guide"))
    implementation(project(":navigation"))

}
