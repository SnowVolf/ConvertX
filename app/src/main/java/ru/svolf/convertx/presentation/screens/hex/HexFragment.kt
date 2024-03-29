package ru.svolf.convertx.presentation.screens.hex

import android.text.InputType
import android.text.method.DigitsKeyListener
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import ru.svolf.convertx.R
import ru.svolf.convertx.data.entity.HistoryItem
import ru.svolf.convertx.presentation.base.BaseMainFragment
import ru.svolf.convertx.presentation.screens.settings.Preferences
import ru.svolf.convertx.utils.DecoderConst
import ru.svolf.convertx.utils.algorhitms.Decoder

/**
 * Created by Snow Volf on 28.01.2017.
 */
class HexFragment : BaseMainFragment() {
	private var isInt = false
	private var resultedString: Deferred<String>? = null

	override fun onViewsReady() {
		super.onViewsReady()
		binding.fieldOutput.setHint(R.string.hint_hex)
		binding.fieldOutput.keyListener = DigitsKeyListener.getInstance("1234567890ABCDEFabcdef")
		binding.fieldOutput.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE

		val hexAdapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(requireActivity(), R.array.hexMode, android.R.layout.simple_spinner_item)
		hexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
		binding.spinnerMode.apply {
			visibility = View.VISIBLE
			adapter = hexAdapter
			setSelection(Preferences.getSpinnerPosition("spinner.hex"))
			onItemSelectedListener = object : OnItemSelectedListener {
				override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
					Preferences.setSpinnerPosition("spinner.hex", position)
					addListener(position)
				}

				override fun onNothingSelected(parent: AdapterView<*>?) {
					Preferences.setSpinnerPosition("spinner.hex", 0)
					addListener(0)
				}
			}
		}

	}

	override fun convertInput(input: String) {
		lifecycleScope.launch {
			if (isInt) {
				resultedString = async(Dispatchers.Default) {
					Decoder.intToHex(input)
				}
			} else {
				resultedString = async(Dispatchers.Default) {
					Decoder.toHexString(input)
				}
			}

			withContext(Dispatchers.IO) {
				val hist = HistoryItem().apply {
					this.id = persistedId
					this.input = input
					this.output = resultedString?.await()
					this.decoder = DecoderConst.HEX
					this.spinnerPosition = Preferences.getSpinnerPosition("spinner.hex")
				}
				getDao().insert(hist)
			}
			try {
				binding.fieldOutput.setText(resultedString?.await())
			} catch (ex: java.lang.NumberFormatException) {
				Toast.makeText(requireContext(), ex.message!!, Toast.LENGTH_SHORT).show()
			}
		}
	}

	override fun convertOutput(output: String) {
		lifecycleScope.launch {
			var string: Deferred<String>? = null
			if (isInt) {
				try {
					string = async(Dispatchers.Default) {
						Decoder.hexToInt(output)
					}
				} catch (ex: java.lang.NumberFormatException) {
					Toast.makeText(requireContext(), ex.message!!, Toast.LENGTH_SHORT).show()
				}
			} else {
				try {
					val string2 = output.replaceFirst("0x", "")
					string = async(Dispatchers.Default) {
						Decoder.decodeHexString(string2)
					}
				} catch (ex: Exception) {
					Toast.makeText(requireContext(), ex.message!!, Toast.LENGTH_SHORT).show()
				}
			}
			withContext(Dispatchers.IO) {
				val hist = HistoryItem().apply {
					this.id = persistedId
					this.input = string?.await()
					this.output = output
					this.decoder = DecoderConst.HEX
					this.spinnerPosition = Preferences.getSpinnerPosition("spinner.hex")
				}
				getDao().insert(hist)
			}
			binding.fieldInput.setText(string?.await())
		}
	}


	private fun addListener(mode: Int) {
		when (mode) {
			0 -> {
				isInt = false
				with(binding) {
					fieldInput.setHint(R.string.hint_string)
					binding.fieldInput.inputType = InputType.TYPE_CLASS_TEXT
				}
			}
			1 -> {
				isInt = true
				with(binding) {
					fieldInput.setHint("Integer")
					binding.fieldInput.keyListener = DigitsKeyListener.getInstance("1234567890ABCDEFabcdef")
					binding.fieldInput.inputType = InputType.TYPE_CLASS_NUMBER
				}
			}
		}
	}
}