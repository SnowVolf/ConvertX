package ru.svolf.convertx.presentation.screens.other

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.svolf.convertx.R
import ru.svolf.convertx.presentation.base.BaseTextCoderFragment
import java.util.zip.Adler32

/**
 * Created by Snow Volf on 14.02.2017.
 */

class AdlerFragment : BaseTextCoderFragment() {

	override fun onViewsReady() {
		super.onViewsReady()
		binding.fieldInputData.setHint(R.string.hint_adler32)
	}

	override fun convertInput(input: String) {
		super.convertInput(input)
		lifecycleScope.launch {
			val adler32 = async(Dispatchers.Default) {
				val adler = Adler32()
				adler.update(input.toByteArray())
				adler.value
			}
			binding.textOutput.text = adler32.await().toString()
		}
	}

}
