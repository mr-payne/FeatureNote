plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlinAndroidKsp)
    alias(libs.plugins.hiltAndroid)
    alias(libs.plugins.kotlin.compose)
    id("maven-publish") // Apply the Maven Publish plugin
}

group = "com.thussey" // Replace with your desired group ID
version = "1.0.0"       // Replace with your desired version

publishing {
    publications {
        create<MavenPublication>("release") { // Define a publication named 'release'
            groupId = "com.thussey"      // Redundant if set at project level, but good practice
            artifactId = "featurenote" // Replace with your library's artifact ID
            version = "1.0.0"               // Redundant if set at project level, but good practice

            afterEvaluate {
                from(components["release"]) // Publish the 'release' variant of your Android library
            }
        }
    }
}

android {
    namespace = "com.thussey.featurenote"
    compileSdk {
        version = release(36)
    }

    buildFeatures {
        compose = true
    }

    defaultConfig {
       // applicationId = "com.thussey.featurenote"
        minSdk = 24
        //targetSdk = 36
        //versionCode = 1
        //versionName = "1.0"

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

    publishing {
        singleVariant("release") {}
    }
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    // My modules
    implementation(libs.notesnetwork)
    implementation(libs.notesdb)
    implementation(libs.core)

    implementation(libs.retrofit) // Or the latest version

    // navigation 3
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    // navigation 3 view models
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)

    // material 3
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material3.windowsizeclass)
    implementation(libs.androidx.material3.navigation3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.adaptive.layout)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    // maybe navigation
    implementation(libs.androidx.hilt.navigation.compose) // viewmodel creation/saved state handle integration
    implementation(libs.kotlinx.serialization.json) // kotlin x serialization dependency

    // --
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler) //  <--- CORRECT: Use ksp for Room compiler
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)
}