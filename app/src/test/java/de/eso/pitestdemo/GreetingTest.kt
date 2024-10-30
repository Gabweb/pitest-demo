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
 *
 * gradlew testDebugUnitTest
 * gradlew pitestDebug
 *
 * Has probably something to do with androidx.compose.ui:ui-test-manifest
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