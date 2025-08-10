plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.nav.safe.args)
}

android {
    namespace = "app.cicilan.navigation"
    compileSdk = libs.versions.compileSdk.get()
        .toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get()
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
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
        freeCompilerArgs += "-Xcontext-receivers"
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
    implementation(project(":data:component"))
    implementation(project(":model:entities"))
    implementation(project(":model:repositories"))

    implementation(libs.bundles.koin)
    implementation(libs.bundles.commoncore)
    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.activityfragment)
    implementation(libs.bundles.navcomponent)
    implementation(libs.bundles.coroutines)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.constraint.layout)
    implementation(libs.recycleview) // RecycleView

    implementation(libs.datastore.preference) // datastore
    implementation(libs.viewpager2) // View Pager
    implementation(libs.preference) // Preference Screen Setting
    implementation(libs.splashscreen) // Splashscreen

    // Crop Image Library
    implementation(libs.android.image.cropper)
}
