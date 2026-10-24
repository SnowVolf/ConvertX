package ru.svolf.convertx.presentation

import android.os.ParcelFileDescriptor
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.performTextReplacement
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertSame
import org.junit.Assume.assumeNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.svolf.convertx.presentation.base.MainActivity

private const val RetainedInput = "Fold"
private const val RetainedOutput = "\\u0046\\u006f\\u006c\\u0064"
private const val ConfigurationTimeout = 10_000L

@RunWith(AndroidJUnit4::class)
class ConverterRetentionTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun activityRecreationPreservesInputAndResult() {
        enterText()
        composeRule.activityRule.scenario.recreate()
        assertRetainedText()
    }

    @Test
    fun foldingPreservesActivityInputAndResult() {
        val states = shell("cmd device_state print-states")
        val closed = stateId(states, "CLOSED")
        val opened = stateId(states, "OPENED")
        val initial = Regex("identifier=(\\d+)").find(shell("cmd device_state state"))
            ?.groupValues?.get(1)
        assumeNotNull(closed, opened, initial)
        enterText()
        val activity = composeRule.activity
        try {
            val transitions = if (initial == closed) listOf(opened!!, closed!!) else listOf(closed!!, opened!!)
            for (state in transitions) {
                val oldWidth = composeRule.activity.resources.configuration.screenWidthDp
                shell("cmd device_state state $state")
                composeRule.waitUntil(ConfigurationTimeout) {
                    composeRule.activity.resources.configuration.screenWidthDp != oldWidth
                }
                composeRule.waitForIdle()
                assertSame("Folding must not recreate the Activity", activity, composeRule.activity)
                assertRetainedText()
            }
        } finally {
            shell("cmd device_state state $initial")
        }
    }

    private fun enterText() {
        composeRule.onAllNodes(hasSetTextAction())[0].performTextReplacement(RetainedInput)
        composeRule.waitUntil(ConfigurationTimeout) {
            composeRule.onAllNodesWithText(RetainedOutput).fetchSemanticsNodes().isNotEmpty()
        }
    }

    private fun assertRetainedText() {
        composeRule.onAllNodes(hasSetTextAction())[0].assertTextContains(RetainedInput)
        composeRule.onAllNodes(hasSetTextAction())[1].assertTextContains(RetainedOutput)
    }

    private fun stateId(states: String, name: String): String? =
        Regex("identifier=(\\d+), name='$name'").find(states)?.groupValues?.get(1)

    private fun shell(command: String): String {
        val automation = InstrumentationRegistry.getInstrumentation().uiAutomation
        return ParcelFileDescriptor.AutoCloseInputStream(automation.executeShellCommand(command))
            .bufferedReader().use { it.readText() }
    }
}
