package ru.svolf.convertx.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.svolf.convertx.R
import ru.svolf.convertx.presentation.base.MainActivity

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun startScreenShowsUnicodeConverter() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        composeRule.onAllNodesWithText(context.getString(R.string.dr_unicode)).onFirst()
            .assertIsDisplayed()
    }

    @Test
    fun menuButtonOpensBackdrop() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        composeRule.onNodeWithContentDescription(context.getString(R.string.dr_other1))
            .performClick()
        composeRule.onNodeWithContentDescription(context.getString(R.string.dr_close_app))
            .assertIsDisplayed()
        composeRule.onNodeWithText(context.getString(R.string.dr_base64)).assertIsDisplayed()
        composeRule.onNodeWithContentDescription(context.getString(R.string.dr_close_app))
            .performClick()
        composeRule.onNodeWithContentDescription(context.getString(R.string.dr_other1))
            .assertIsDisplayed()
    }
}
