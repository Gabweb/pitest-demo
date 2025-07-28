import pl.droidsonroids.gradle.pitest.PitestPlugin.PITEST_CONFIGURATION_NAME
import pl.droidsonroids.gradle.pitest.PitestPluginExtension

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("pl.droidsonroids.pitest") version "0.2.22" apply false
}


subprojects {
    afterEvaluate {
        apply(plugin = "pl.droidsonroids.pitest")

        buildscript {
            dependencies.add(
                PITEST_CONFIGURATION_NAME,
                "com.arcmutate:pitest-kotlin-plugin:1.4.3"
            )
            dependencies.add(PITEST_CONFIGURATION_NAME, "com.arcmutate:base:1.5.0")
            dependencies.add(PITEST_CONFIGURATION_NAME, "com.arcmutate:android:0.0.5")
            dependencies.add(PITEST_CONFIGURATION_NAME, "com.arcmutate:pitest-git-plugin:2.2.4")
        }

        extensions.getByType<PitestPluginExtension>().features.add("+kotlin_extra")

        extensions.findByType<PitestPluginExtension>()?.apply {
            setOf("de.eso.*", "de.esolutions.*").also { targets ->
                targetClasses.set(targets)
                targetTests.set(targets.map { "${it}Test" })
            }
            pitestVersion.set("1.20.0")
            verbose.set(true)
            excludeMockableAndroidJar.set(true)
            mutators.set(setOf("STRONGER", "EXTENDED"))
        }
    }
}