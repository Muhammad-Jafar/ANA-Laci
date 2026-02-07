plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.nav.safe.args)
}

android {
    namespace = "proj.yopro.laci.navigation"
    compileSdk =
        libs.versions.compileSdk
            .get()
            .toInt()

    defaultConfig {
        minSdk =
            libs.versions.minSdk
                .get()
                .toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmToolchain(17)
            freeCompilerArgs =
                listOf(
                    "-Xcontext-parameters",
                )
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":data:component"))
    implementation(project(":model:entities"))
    implementation(project(":model:repositories"))

    implementation(libs.bundles.commoncore)
    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.activityfragment)
    implementation(libs.bundles.koin)
    implementation(libs.bundles.navcomponent)
    implementation(libs.bundles.coroutines)
    implementation(libs.constraint.layout)
    implementation(libs.recycleview)
    implementation(libs.datastore.preference)
    implementation(libs.viewpager2)
    implementation(libs.preference)
    implementation(libs.splashscreen)
    implementation(libs.android.image.cropper)
}
