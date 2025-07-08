package de.eso.pitestdemo

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Runs as unit-test but fails to run via pitest.
 * Issue seems to be additional android flavors dimensions (e.g. A1Debug)
 *
 * If you had this project running before, please run gradlew clean to reproduce the issue.
 *
 * gradlew :app:pitestA1Debug
 * gradlew :app:testA1DebugUnitTest
 *
 */
@RunWith(RobolectricTestRunner::class)
class GreetingTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun greetingTest() = runTest {
        // GIVEN
        composeTestRule.setContent {
            Greeting(
                name = "text",
            )
        }

        composeTestRule.awaitIdle()

        // THEN
        composeTestRule
            // Find and match nodes within the UI tree
            .onNode(hasText("Hello text!"))
            // Assert the current state of your UI
            .assertIsDisplayed()
    }
}