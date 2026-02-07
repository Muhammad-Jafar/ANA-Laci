plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.nav.safe.args)
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "proj.yopro.laci"
    compileSdk =
        libs.versions.compileSdk
            .get()
            .toInt()

    defaultConfig {
        applicationId = "proj.yopro.laci"
        minSdk =
            libs.versions.minSdk
                .get()
                .toInt()
        targetSdk =
            libs.versions.targetSdk
                .get()
                .toInt()
        versionCode = 7
        versionName = "1.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
        getByName("debug") {
            isDebuggable = true
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmToolchain(17)
        }
    }

    buildFeatures {
        viewBinding = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":data:local"))
    implementation(project(":data:component"))
    implementation(project(":model:entities"))
    implementation(project(":model:repositories"))
    implementation(project(":navigation"))

    api(libs.bundles.koin)
    api(libs.bundles.commoncore)
    api(libs.bundles.lifecycle)
    api(libs.bundles.activityfragment)
    api(libs.bundles.navcomponent)
    api(libs.bundles.coroutines)
    api(libs.datastore.preference)
}
