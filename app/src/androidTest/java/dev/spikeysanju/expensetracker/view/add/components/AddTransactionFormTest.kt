package dev.spikeysanju.expensetracker.view.add.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

class AddTransactionFormTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun addTransactionForm_InputAndSave_ReturnsCorrectData() {
        var savedTitle = ""
        var savedAmount = ""

        composeTestRule.setContent {
            AddTransactionForm { title, amount, _, _, _, _ ->
                savedTitle = title
                savedAmount = amount
            }
        }

        // Enter Title
        composeTestRule.onNodeWithText("Title").performTextInput("Coffee")
        
        // Enter Amount
        composeTestRule.onNodeWithText("Amount").performTextInput("150")

        // Click Save
        composeTestRule.onNodeWithText("Save Transaction").performClick()

        // Assert
        assert(savedTitle == "Coffee")
        assert(savedAmount == "150")
    }
}
