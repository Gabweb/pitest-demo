plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "de.eso.pitestdemo"
    compileSdk = 34

    defaultConfig {
        applicationId = "de.eso.pitestdemo"
        minSdk = 30
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    val hasFlavor =  project.findProperty("flavored") != null

    if (hasFlavor) {
        flavorDimensions += "dimensionOne"
        flavorDimensions += "dimensionTwo"
        productFlavors {
            create("a") {
                dimension = "dimensionOne"
            }
            create("b") {
                dimension = "dimensionOne"
            }

            create("1") {
                dimension = "dimensionTwo"
            }
            create("2") {
                dimension = "dimensionTwo"
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}



dependencies {
    // Standard Setup
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.androidx.ui.test.junit4)

    // Test
    testImplementation(libs.bundles.jupiter)
    testRuntimeOnly(libs.bundles.jupiter.runtime)
    testImplementation(libs.robolectric)

    // Needed for pitest
    testImplementation(libs.androidx.espresso.core)
    // Needed for compose tests
    debugImplementation(libs.androidx.ui.test.manifest)
    releaseImplementation(libs.androidx.ui.test.manifest)
}