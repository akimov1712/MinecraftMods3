plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "dev.akmvxx.app"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "dev.akmvxx.minecraftmods3"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
    implementation(project(":navigation"))

}
