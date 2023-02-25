package ru.svolf.convertx.presentation.screens.other

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.svolf.convertx.R
import ru.svolf.convertx.presentation.base.BaseTextCoderFragment
import ru.svolf.convertx.utils.algorhitms.Decoder

/**
 * Created by Snow Volf on 22.02.2017, 19:51
 */

class XmlDecodeFragment : BaseTextCoderFragment() {

    override fun onViewsReady() {
        super.onViewsReady()
        binding.fieldInputData.setHint(R.string.hint_encoded_xml)
    }

    override fun convertInput(input: String) {
        super.convertInput(input)
        lifecycleScope.launch {
            val unescaped = async(Dispatchers.Default) {
                Decoder.unescapeXml(input)
            }
            binding.textOutput.text = unescaped.await()
        }
    }

}
