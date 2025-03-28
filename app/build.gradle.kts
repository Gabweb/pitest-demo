import pl.droidsonroids.gradle.pitest.PitestPluginExtension


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("pl.droidsonroids.pitest")
}

buildscript {
    configurations.create("pitest")
    dependencies.add(
        "pitest",
        "com.arcmutate:pitest-kotlin-plugin:1.3.0"
    )
    dependencies.add("pitest", "com.arcmutate:base:1.3.1")
    dependencies.add("pitest", "com.arcmutate:android:0.0.2")
    dependencies.add("pitest", "com.arcmutate:pitest-git-plugin:1.3.2")
}

extensions.getByType<PitestPluginExtension>().features.add("+kotlin_extra")

extensions.findByType<PitestPluginExtension>()?.apply {
    setOf("de.eso.*", "de.esolutions.*").also { targets ->
        targetClasses.set(targets)
        targetTests.set(targets.map { "${it}Test" })
    }
    pitestVersion.set("1.19.0")
    verbose.set(true)
    excludeMockableAndroidJar.set(true)
    mutators.set(setOf("STRONGER", "EXTENDED"))
}

android {
    namespace = "de.eso.pitestdemo"
    compileSdk = 34

    defaultConfig {
        applicationId = "de.eso.pitestdemo"
        minSdk = 31
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
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
        kotlinCompilerExtensionVersion = "1.5.15"
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
    implementation(libs.androidx.ui.test.junit4.android)

    implementation(libs.coroutines.core)
    testImplementation(libs.coroutines.test)

    testImplementation("io.mockk:mockk:1.13.17")

    // Test
    testImplementation(libs.junit)
    testImplementation("org.robolectric:robolectric:4.13")

    // Needed for pitest
    testImplementation(libs.androidx.espresso.core)
    // Needed for compose tests
    debugImplementation(libs.androidx.ui.test.manifest)
    releaseImplementation(libs.androidx.ui.test.manifest)
}