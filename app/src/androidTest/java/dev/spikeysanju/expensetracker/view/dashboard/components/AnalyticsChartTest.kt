package dev.spikeysanju.expensetracker.view.dashboard.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class AnalyticsChartTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun analyticsChart_DisplaysCorrectTotal() {
        val testData = listOf(
            ChartData(Color.Red, 500f, "Food"),
            ChartData(Color.Blue, 500f, "Rent")
        )

        composeTestRule.setContent {
            AnalyticsChart(data = testData)
        }

        // Verify the total text is calculated correctly (500 + 500 = 1000)
        composeTestRule.onNodeWithText("₹1000").assertIsDisplayed()
    }

    @Test
    fun analyticsChart_DisplaysLegendItems() {
        val testData = listOf(
            ChartData(Color.Red, 100f, "TestingLabel")
        )

        composeTestRule.setContent {
            AnalyticsChart(data = testData)
        }

        // Verify the legend label is displayed
        composeTestRule.onNodeWithText("TestingLabel").assertIsDisplayed()
    }

    @Test
    fun analyticsChart_CanvasIsPresent() {
        composeTestRule.setContent {
            AnalyticsChart(data = emptyList())
        }

        // Verify the canvas exists using the test tag
        composeTestRule.onNodeWithTag("AnalyticsCanvas").assertExists()
    }
}
