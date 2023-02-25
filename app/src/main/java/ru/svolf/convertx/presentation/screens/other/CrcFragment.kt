package ru.svolf.convertx.presentation.screens.other

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.svolf.convertx.R
import ru.svolf.convertx.presentation.base.BaseTextCoderFragment
import java.util.zip.CRC32

/**
 * Created by Snow Volf on 14.02.2017.
 */

class CrcFragment : BaseTextCoderFragment() {

    override fun onViewsReady() {
        super.onViewsReady()
        binding.fieldInputData.setHint(R.string.hint_crc)
    }

    override fun convertInput(input: String) {
        super.convertInput(input)
        lifecycleScope.launch {
            val crc = async(Dispatchers.Default) {
                val carc = CRC32()
                carc.update(input.toByteArray())
                carc.value
            }
            binding.textOutput.text = crc.await().toString()
        }
    }

}
