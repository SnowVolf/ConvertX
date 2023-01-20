package ru.svolf.convertx.presentation.fragments.main

import ru.svolf.convertx.R
import ru.svolf.convertx.algorhitms.Decoder
import ru.svolf.convertx.extension.clear
import ru.svolf.convertx.presentation.fragments.base.BaseMainFragment
import ru.svolf.convertx.utils.Toasty

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
    }

    override fun convertOutput(output: String) {
        //проверяем валидность String, и переносим его в нужное поле при необходимости
        if (output.isNotEmpty() and !output.matches("(\\\\u[0-9]{4})+".toRegex())) {
            binding.fieldInput.setText(output)
            binding.fieldOutput.clear()
            binding.fieldInput.requestFocus()
            Toasty.error(requireContext(), getString(R.string.message_malformed_unicode)).show()
        } else {
            val text = Decoder.decodeUnicode(output)
            binding.fieldOutput.setText(text)
        }

    }

}