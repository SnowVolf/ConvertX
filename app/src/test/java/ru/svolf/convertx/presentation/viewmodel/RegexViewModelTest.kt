package ru.svolf.convertx.presentation.viewmodel

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RegexViewModelTest {
    @Test
    fun `valid expression highlights all matches`() {
        val viewModel = RegexViewModel()

        viewModel.setExpression("cat")
        viewModel.setSample("cat scatter")

        assertEquals(listOf(0 until 3, 5 until 8), viewModel.state.value.matches)
        assertEquals("/g", viewModel.state.value.flagLabel)
    }

    @Test
    fun `invalid expression is exposed as validation error`() {
        val viewModel = RegexViewModel()

        viewModel.setExpression("[")
        viewModel.setSample("text")

        assertNotNull(viewModel.state.value.error)
        assertTrue(viewModel.state.value.matches.isEmpty())
    }
}
