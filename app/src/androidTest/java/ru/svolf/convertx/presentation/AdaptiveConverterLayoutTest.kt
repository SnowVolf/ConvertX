package ru.svolf.convertx.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.svolf.convertx.presentation.compose.AdaptiveConverterLayout

@RunWith(AndroidJUnit4::class)
class AdaptiveConverterLayoutTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun resizingRepositionsFieldsAndPreservesEditingState() {
        val wide = mutableStateOf(false)
        composeRule.setContent {
            AdaptiveConverterLayout(
                sideBySide = wide.value,
                modifier = Modifier.requiredWidth(if (wide.value) 360.dp else 320.dp),
                input = {
                    val text = remember { mutableStateOf("") }
                    BasicTextField(
                        value = text.value,
                        onValueChange = { text.value = it },
                        modifier = Modifier.fillMaxWidth().testTag("input").height(80.dp)
                    )
                },
                controls = { Box(Modifier.size(48.dp).testTag("controls")) },
                modeSelector = { Box(Modifier.fillMaxWidth().height(48.dp).testTag("mode")) },
                output = { Box(Modifier.fillMaxWidth().height(120.dp).testTag("output")) }
            )
        }
        assertVertical()
        composeRule.onNodeWithTag("input").performClick().performTextInput("ConvertX")
        composeRule.runOnIdle { wide.value = true }

        val input = composeRule.onNodeWithTag("input").fetchSemanticsNode().boundsInRoot
        val output = composeRule.onNodeWithTag("output").fetchSemanticsNode().boundsInRoot
        assertEquals(input.top, output.top, 0.5f)
        assertEquals(input.width, output.width, 0.5f)
        val controls = composeRule.onNodeWithTag("controls").fetchSemanticsNode().boundsInRoot
        val mode = composeRule.onNodeWithTag("mode").fetchSemanticsNode().boundsInRoot
        assertTrue(input.right <= controls.left)
        assertTrue(controls.right <= output.left)
        assertEquals((input.top + output.bottom) / 2, controls.center.y, 0.5f)
        assertTrue(mode.bottom <= input.top)
        composeRule.onNodeWithTag("input").assertIsFocused().assertTextContains("ConvertX")

        composeRule.runOnIdle { wide.value = false }
        assertVertical()
        composeRule.onNodeWithTag("input").assertIsFocused().assertTextContains("ConvertX")
    }

    private fun assertVertical() {
        val input = composeRule.onNodeWithTag("input").fetchSemanticsNode().boundsInRoot
        val controls = composeRule.onNodeWithTag("controls").fetchSemanticsNode().boundsInRoot
        val output = composeRule.onNodeWithTag("output").fetchSemanticsNode().boundsInRoot
        assertEquals(input.left, output.left, 0.5f)
        assertTrue(input.bottom <= controls.top)
        assertTrue(controls.bottom <= output.top)
        val mode = composeRule.onNodeWithTag("mode").fetchSemanticsNode().boundsInRoot
        assertTrue(input.bottom <= mode.top)
        assertTrue(mode.bottom <= output.top)
    }
}
