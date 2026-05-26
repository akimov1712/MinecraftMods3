plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.cas)
}

val applicationArtifactId: String =
    property("applicationName")?.toString() ?: "minecraftmods3"

android {
    namespace = "dev.akmvxx.app"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "dev.akmvxx.$applicationArtifactId"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "APPLICATION_ID", "\"$applicationId\"")
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
        buildConfig = true
    }
}

cas {
    includeOptimalAds = true
    adapters {
        ironSource = true
        googleAds = true
        unityAds = true
        kidoz = true
        liftoffMonetize = true
        inMobi = true
        chartboost = true
        dtExchange = true
        mintegral = true
        appLovin = true
        audienceNetwork = true
        pangle = true
        yangoAds = true
        bigoAds = true
        casExchange = true
        startIO = true
        hyprMX = true
        ysoNetwork = true
        ogury = true
        prado = true
        superAwesome = true
        smaato = true
        maticoo = true
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
    implementation(project(":core:ads"))
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
