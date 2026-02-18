package de.eso.pitestdemo

import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Rule
import org.junit.runner.RunWith

/**
 * Reproducer
 *
 * a) Failing to run (com.arcmutate:android:0.0.7)
 *    - Set android plugin version in build.gradle.kts to 0.0.7
 *    - gradlew :app:clean (just to be sure)
 *    - gradlew :app:pitestChangesA1Debug
 *    - Fails as classpath entry is not added
 *    - (gradlew :app:pitestA1Debug works as expected)
 *
 * b) Using wrong classpath entry (com.arcmutate:android:0.0.9)
 *    - Set android plugin version in build.gradle.kts to 0.0.9
 *    - gradlew :app:clean (just to be sure)
 *    - gradlew :app:pitestA1Debug
 *    - gradlew :app:pitestChangesA1Robolectric
 *    - Uses wrong robolectric config "a1Debug".
 */
@RunWith(AndroidJUnit4::class)
class SharedTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun greetingTest() = runTest {
        // GIVEN
        composeTestRule.setContent {
            Greeting(
                name = "text",
            ) {
                Text(
                    text = "Hello text!",
                )
            }
        }

        composeTestRule.awaitIdle()

        // THEN
        composeTestRule
            // Find and match nodes within the UI tree
            .onNode(hasText("Hello text!"))
            // Assert the current state of your UI
            .assertIsDisplayed()
    }

    @Test
    fun greetingTest2() = runTest {
        // GIVEN
        composeTestRule.setContent {
            Greeting(
                name = "",
            ) {
                Text(
                    text = "Nope nope",
                )
            }
        }

        composeTestRule.awaitIdle()

        // THEN
        composeTestRule
            // Find and match nodes within the UI tree
            .onNode(hasText("Hello!"))
            // Assert the current state of your UI
            .assertIsDisplayed()
    }
}