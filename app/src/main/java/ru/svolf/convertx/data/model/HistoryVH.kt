package ru.svolf.convertx.data.model

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import ru.svolf.convertx.R
import ru.svolf.convertx.data.entity.HistoryItem
import ru.svolf.convertx.databinding.ItemHistoryBinding
import java.text.SimpleDateFormat
import java.util.*

/*
 * Created by SVolf on 26.01.2023, 20:50
 * This file is a part of "ConvertX" project
 */
class HistoryVH(private val historyItem: HistoryItem) : AbstractBindingItem<ItemHistoryBinding>() {

    override val type: Int
        get() = R.id.text_decoder_type

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemHistoryBinding {
        return ItemHistoryBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: ItemHistoryBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        val dateFormat = SimpleDateFormat("EEE, HH:mm", Locale.getDefault())
        binding.contentInput.text = historyItem.input
        binding.textOutput.text = historyItem.output
        binding.textDecoderType.text = getDecoderType(historyItem.decoder)
        binding.textTime.text = historyItem.id?.let { dateFormat.format(Date(it)) }
    }

    override fun unbindView(binding: ItemHistoryBinding) {
        super.unbindView(binding)
        binding.contentInput.text = null
        binding.textOutput.text = null
        binding.textDecoderType.text = null
        binding.textTime.text = null
    }

    private fun getDecoderType(type: Int?): String {
        return when (type) {
            0 -> "Unicode"
            1 -> "Base64"
            2 -> "HEX"
            3 -> "Regexp"
            else -> "Unknown"
        }
    }

}