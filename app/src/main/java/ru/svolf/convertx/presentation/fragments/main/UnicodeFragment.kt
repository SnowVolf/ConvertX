package ru.svolf.convertx.presentation.fragments.main

import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.svolf.convertx.R
import ru.svolf.convertx.algorhitms.Decoder
import ru.svolf.convertx.data.entity.HistoryItem
import ru.svolf.convertx.extension.clear
import ru.svolf.convertx.presentation.fragments.base.BaseMainFragment

/**
 * Created by Snow Volf on 28.01.2017.
 */
class UnicodeFragment : BaseMainFragment() {

    override fun onViewsReady() {
        super.onViewsReady()
        binding.fieldInput.setHint(R.string.hint_utf)
        binding.fieldOutput.setHint(R.string.hint_unicode)
    }

    override fun convertInput(input: String) {
        val unicodeValue = StringBuilder()
        for (element in input) unicodeValue.append("\\u").append(Integer.toHexString(element.code or 0x10000).substring(1))
        binding.fieldOutput.setText(unicodeValue)
        CoroutineScope(Dispatchers.IO).launch {
            val hist = HistoryItem().apply {
                this.id = System.currentTimeMillis()
                this.input = input
                this.output = unicodeValue.toString()
                this.decoder = 0
            }
            getDao().insert(hist)
        }
    }

    override fun convertOutput(output: String) {
        //проверяем валидность String, и переносим его в нужное поле при необходимости
        if (output.isNotEmpty() and !output.matches("(\\\\u\\d{4})+".toRegex())) {
            binding.fieldInput.setText(output)
            binding.fieldOutput.clear()
            binding.fieldInput.requestFocus()
            Toast.makeText(requireContext(), getString(R.string.message_malformed_unicode), Toast.LENGTH_SHORT).show()
        } else {
            val text = Decoder.decodeUnicode(output)
            binding.fieldOutput.setText(text)
            CoroutineScope(Dispatchers.IO).launch {
                val hist = HistoryItem().apply {
                    id = System.currentTimeMillis()
                    input = text
                    this.output = output
                    decoder = 0
                }
                getDao().insert(hist)
            }
        }
    }

}