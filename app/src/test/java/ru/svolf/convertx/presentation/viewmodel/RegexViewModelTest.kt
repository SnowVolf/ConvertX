package ru.svolf.convertx.presentation.viewmodel

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

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

    @Test
    fun `flags change matching behavior and selected state`() {
        val viewModel = RegexViewModel()

        viewModel.setExpression("cat")
        viewModel.setSample("CAT cat")
        viewModel.toggleFlag(0)

        assertEquals(listOf(0 until 3, 4 until 7), viewModel.state.value.matches)
        assertTrue(viewModel.flagSelected(0))
        assertEquals("/gi", viewModel.state.value.flagLabel)

        viewModel.toggleFlag(0)
        assertEquals(listOf(4 until 7), viewModel.state.value.matches)
        assertEquals("/g", viewModel.state.value.flagLabel)
    }

    @Test
    fun `empty expression and clear reset derived values but preserve flags`() {
        val viewModel = RegexViewModel()

        viewModel.toggleFlag(1)
        viewModel.setExpression("cat")
        viewModel.setSample("cat")
        viewModel.clear()

        assertEquals("", viewModel.state.value.expression)
        assertEquals("", viewModel.state.value.sample)
        assertEquals("", viewModel.state.value.result)
        assertEquals("/gm", viewModel.state.value.flagLabel)
        assertTrue(viewModel.flagSelected(1))
    }
}
