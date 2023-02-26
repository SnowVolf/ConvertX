package ru.svolf.convertx.presentation.screens.base64

import android.util.Base64
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.svolf.convertx.R
import ru.svolf.convertx.data.entity.HistoryItem
import ru.svolf.convertx.presentation.base.BaseMainFragment
import ru.svolf.convertx.presentation.screens.settings.Preferences
import ru.svolf.convertx.utils.DecoderConst
import ru.svolf.convertx.utils.algorhitms.Decoder
import ru.svolf.convertx.utils.clear

/**
 * Created by Snow Volf on 28.01.2017.
 */
class Base64Fragment : BaseMainFragment() {
    var algorhitm = 0
    val algs = intArrayOf(Base64.NO_WRAP, Base64.NO_CLOSE, Base64.NO_PADDING, Base64.URL_SAFE, Base64.CRLF, Base64.DEFAULT)

    override fun onViewsReady() {
        super.onViewsReady()
        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.baseAlgVal, android.R.layout.simple_spinner_item
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.fieldInput.setHint(R.string.hint_utf)
        binding.fieldOutput.setHint(R.string.hint_base64)
        binding.spinnerMode.apply {
            visibility = View.VISIBLE
            adapter = spinnerAdapter
            setSelection(Preferences.getSpinnerPosition("spinner.base64"))
        }.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                for (i in 0 until binding.spinnerMode.count) {
                    if (pos == i) {
                        algorhitm = algs[i]
                    }
                }
                Preferences.setSpinnerPosition("spinner.base64", pos)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                algorhitm = Base64.NO_WRAP
            }
        }
    }

    override fun convertInput(input: String) {
        lifecycleScope.launch {
            val text = async(Dispatchers.Default) {
                Decoder.encodeBase64(input, algs[Preferences.getSpinnerPosition("spinner.base64")])
            }
            withContext(Dispatchers.IO) {
                val hist = HistoryItem().apply {
                    id = persistedId
                    this.input = input
                    output = text.await()
                    decoder = DecoderConst.BASE64
                    spinnerPosition = Preferences.getSpinnerPosition("spinner.base64")
                }
                getDao().insert(hist)
            }
            binding.fieldOutput.setText(text.await())
        }
    }

    override fun convertOutput(output: String) {
        if (output.isNotEmpty() and !output.matches("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?\$".toRegex())){
            binding.fieldInput.setText(output)
            binding.fieldOutput.clear()
            binding.fieldInput.requestFocus()
            Toast.makeText(requireContext(), getString(R.string.message_malformed_base64), Toast.LENGTH_SHORT).show()
        } else {
            lifecycleScope.launch {
                val text = async(Dispatchers.Default) {
                    Decoder.decodeBase64(output, algs[Preferences.getSpinnerPosition("spinner.base64")])
                }
                withContext(Dispatchers.IO) {
                    val hist = HistoryItem().apply {
                        id = persistedId
                        input = text.await()
                        this.output = output
                        decoder = DecoderConst.BASE64
                        spinnerPosition = Preferences.getSpinnerPosition("spinner.base64")
                    }
                    getDao().insert(hist)
                }
                binding.fieldInput.setText(text.await())
            }
        }

    }
}