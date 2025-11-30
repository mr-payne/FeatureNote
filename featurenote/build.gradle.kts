plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlinAndroidKsp)
    alias(libs.plugins.hiltAndroid)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    id("maven-publish") // Apply the Maven Publish plugin
}

group = "com.thussey" // Replace with your desired group ID
version = "1.0.0"       // Replace with your desired version

android {
    namespace = "com.thussey.featurenote"
    compileSdk {
        version = release(36)
    }

    sourceSets {
        getByName("main") {
            kotlin.srcDir("src/main/kotlin")
        }
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
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

kotlin {
    jvmToolchain(21)
}

// --- MAVEN PUBLISHING CONFIGURATION ---

afterEvaluate {
    publishing {
        publications {
            // The name of this block, "release", becomes part of the publishing task name
            // e.g., "publishReleasePublicationTo..."
            create<MavenPublication>("release") {
                // --- Define Artifact Metadata ---
                groupId = "com.thussey" // Replace with your group ID
                artifactId = "featurenote"               // Your library's name
                version = "1.0.0"                 // Your library's version

                // --- Define What to Publish ---
                // This targets the output of the "release" build variant.
                from(components["release"])

                // --- Customize POM (Project Object Model) ---
                pom {
                    name.set("Core Library")
                    description.set("A description of your core library.")
                    url.set("https://github.com/yourcompany/yourproject") // Optional: project URL

                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set("your-id")
                            name.set("Your Name")
                            email.set("you@example.com")
                        }
                    }
                    // Optional: SCM (Source Control Management) info
//                    scm {
//                        connection.set("scm:git:git://github.com/yourcompany/yourproject.git")
//                        developerConnection.set("scm:git:ssh://github.com/yourcompany/yourproject.git")
//                        url.set("https://github.com/yourcompany/yourproject/tree/main")
//                    }
                }
            }
        }
        repositories {
            // --- Define Where to Publish ---
            // Example: Publishing to the local Maven repository (`~/.m2/repository`)
            mavenLocal {
                name = "local"
            }

            // Example: Publishing to a remote repository (replace with your repo's URL and credentials)
            /*
            maven {
                name = "MyRemoteRepo"
                url = uri("https://your-repo-url/maven-releases")
                credentials {
                    username = project.property("repoUsername") as String?
                    password = project.property("repoPassword") as String?
                }
            }
            */
        }
    }
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