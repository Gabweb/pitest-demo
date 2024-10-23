package de.eso.pitestdemo

import androidx.compose.material3.Text
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Assert.assertEquals
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
class ExampleUnitTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun should_show_the_given_location() {
        // GIVEN
        composeTestRule.setContent {
            Text(
                text = "text",
            )
        }

        // THEN
        assertEquals(true, true)
    }
}