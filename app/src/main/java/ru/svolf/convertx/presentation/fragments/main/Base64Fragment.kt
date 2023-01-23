package ru.svolf.convertx.presentation.fragments.main

import android.util.Base64
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import ru.svolf.convertx.R
import ru.svolf.convertx.algorhitms.Decoder
import ru.svolf.convertx.extension.clear
import ru.svolf.convertx.presentation.fragments.base.BaseMainFragment
import ru.svolf.convertx.settings.Preferences
import ru.svolf.convertx.utils.Toasty

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
        val text = Decoder.encodeBase64(input, algs[Preferences.getSpinnerPosition("spinner.base64")])
        binding.fieldOutput.setText(text)
    }

    override fun convertOutput(output: String) {
        if (output.isNotEmpty() and !output.matches("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?\$".toRegex())){
            binding.fieldInput.setText(output)
            binding.fieldOutput.clear()
            binding.fieldInput.requestFocus()
            Toasty.error(requireContext(), getString(R.string.message_malformed_base64)).show()
        } else {
            val text = Decoder.decodeBase64(output, algs[Preferences.getSpinnerPosition("spinner.base64")])
            binding.fieldInput.setText(text)
        }

    }
}