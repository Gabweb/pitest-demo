import com.android.build.gradle.BaseExtension
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.kotlin.dsl.named
import pl.droidsonroids.gradle.pitest.PitestTask
import kotlin.jvm.java

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

    val hasFlavor = true // project.findProperty("flavored") != null

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


afterEvaluate {
    // Hard-coded for reproduction case
    val flavors = listOf("A1Robolectric", "A1Debug")

    flavors.forEach { flavor ->
        // This task would configure pitest to run in changed-based mode.
        tasks.register("pitestChanges$flavor") {
            val baseTask = tasks.named<PitestTask>("pitest$flavor")
            finalizedBy(baseTask)
            doFirst {
                baseTask.configure {
                    val reportBaseDir = reportDir.get()
                    reportDir.set(reportBaseDir.dir("../changes$flavor"))
                    // Would set git feature, not required to reproduce issue.
                }
            }
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

    debugImplementation(libs.androidx.ui.test.manifest)

    // Test
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.espresso.core)
    testImplementation(libs.androidx.ui.test.junit4)
    testImplementation("org.junit.platform:junit-platform-launcher:1.13.4")
    testImplementation("org.junit.vintage:junit-vintage-engine:5.13.4")
}


configureSharedTests()

/**
 * A dedicated build-type "robolectric" (similar to debug and release) is used in order to run
 * the same androidTests either on the Emulator or via Robolectric.
 */
fun Project.configureSharedTests(modifier: (com.android.build.gradle.internal.dsl.BuildType.() -> Unit) = { }) {
    with(project.extensions.getByType(BaseExtension::class.java)) {
        buildTypes {
            create("robolectric") {
                initWith(getByName("debug"))
                matchingFallbacks.addAll(listOf("debug", "release"))
                modifier()
            }
            sourceSets.matching { it.name.startsWith("test") && it.name.endsWith("Robolectric") }.all {
                val variant = name.removePrefix("test").removeSuffix("Robolectric")
                variant.directoryCandidates().forEach {
                    java.srcDir("src/androidTest$it/kotlin")
                    java.srcDir("src/androidTest$it/java")
                }
            }
        }
    }

    // all shared androidTests are run with test tasks
    // so basically the same dependencies are required
    val testRobolectricImplementation = configurations.getByName("testRobolectricImplementation")
    val androidTestImplementation = configurations.getByName("androidTestImplementation")
    testRobolectricImplementation.extendsFrom(androidTestImplementation)

    // robolectric configuration is just a special debug configuration
    // => inherit all dependencies
    val debugImplementationDependencies = configurations.getByName("debugImplementation").dependencies.toList()
    val runtimeOnlyDependencies = configurations.getByName("debugRuntimeOnly").dependencies.toList()

    afterEvaluate {
        val afterEvaluateDebugDependencies = configurations.getByName("debugImplementation").dependencies.toList()
        val afterEvaluateRuntimeDependencies = configurations.getByName("debugRuntimeOnly").dependencies.toList()
        configurations.getByName("robolectricImplementation").dependencies.addAll(afterEvaluateDebugDependencies)
        configurations.getByName("robolectricRuntimeOnly").dependencies.addAll(afterEvaluateRuntimeDependencies)

        if (debugImplementationDependencies != afterEvaluateDebugDependencies ||
            runtimeOnlyDependencies != afterEvaluateRuntimeDependencies
        ) {
            logger.info("[shared-test] debugImplementationDependencies: {}", debugImplementationDependencies)
            logger.info("[shared-test] afterEvaluateDebugDependencies: {}", afterEvaluateDebugDependencies)
            logger.info("[shared-test] runtimeOnlyDependencies: {}", runtimeOnlyDependencies)
            logger.info("[shared-test] afterEvaluateRuntimeDependencies: {}", afterEvaluateRuntimeDependencies)
            logger.warn(
                "[shared-test] WARNING: Detected changes in debugImplementation or " +
                        "runtimeOnly after configureSharedTests call. This is a bad practice and should be avoided."
            )
        }
    }
}

private fun String.directoryCandidates(): List<String> =
    split(Regex("(?=\\p{Lu})")).plus(this).plus("").distinct()