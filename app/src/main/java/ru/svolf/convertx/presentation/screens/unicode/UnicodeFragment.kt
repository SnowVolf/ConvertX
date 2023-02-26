package ru.svolf.convertx.presentation.screens.unicode

import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.svolf.convertx.R
import ru.svolf.convertx.data.entity.HistoryItem
import ru.svolf.convertx.presentation.base.BaseMainFragment
import ru.svolf.convertx.utils.DecoderConst
import ru.svolf.convertx.utils.algorhitms.Decoder
import ru.svolf.convertx.utils.clear

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
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                getDao().insert(HistoryItem().apply {
                    this.id = persistedId
                    this.input = input
                    this.output = unicodeValue.toString()
                    this.decoder = DecoderConst.UNICODE
                })
            }
        }
    }

    override fun convertOutput(output: String) {
        //проверяем валидность String, и переносим его в нужное поле при необходимости
        if (output.isNotEmpty() and !output.matches("(\\\\u[A-Za-z\\d]{4})+".toRegex())) {
            binding.fieldInput.setText(output)
            binding.fieldOutput.clear()
            binding.fieldInput.requestFocus()
            Toast.makeText(requireContext(), getString(R.string.message_malformed_unicode), Toast.LENGTH_SHORT).show()
        } else {
            lifecycleScope.launch {
                val text = async {
                    Decoder.decodeUnicode(output)
                }

                withContext(Dispatchers.IO) {
                    val hist = HistoryItem().apply {
                        id = persistedId
                        input = text.await()
                        this.output = output
                        decoder = DecoderConst.UNICODE
                    }
                    getDao().insert(hist)
                }
                binding.fieldInput.setText(text.await())
            }
        }
    }

}