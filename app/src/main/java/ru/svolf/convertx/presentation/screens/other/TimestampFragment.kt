package ru.svolf.convertx.presentation.screens.other

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.svolf.convertx.R
import ru.svolf.convertx.presentation.base.BaseTextCoderFragment
import ru.svolf.convertx.utils.algorhitms.Decoder

/**
 * Created by Snow Volf on 26.02.2017, 20:44
 */

class TimestampFragment : BaseTextCoderFragment() {

	override fun onViewsReady() {
		super.onViewsReady()
		binding.fieldInputData.setHint(R.string.hint_timestamp)
		binding.textOutput.hint = "dd.MM.yyyy HH:mm:ss"
	}

	override fun convertInput(input: String) {
		super.convertInput(input)
		lifecycleScope.launch {
			val date = async(Dispatchers.Default) {
				Decoder.getNormalDate(input.toLong())
			}
			binding.textOutput.text = date.await()
		}
	}

	override fun placeInput() {
		super.placeInput()
		binding.fieldInputData.setText(System.currentTimeMillis().toString())
	}
}
