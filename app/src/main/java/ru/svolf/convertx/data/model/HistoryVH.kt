package ru.svolf.convertx.data.model

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import com.mikepenz.fastadapter.swipe.ISwipeable
import ru.svolf.convertx.R
import ru.svolf.convertx.data.entity.HistoryItem
import ru.svolf.convertx.databinding.ItemHistoryBinding
import java.text.SimpleDateFormat
import java.util.*

/*
 * Created by SVolf on 26.01.2023, 20:50
 * This file is a part of "ConvertX" project
 */
class HistoryVH(private val historyItem: HistoryItem) : AbstractBindingItem<ItemHistoryBinding>(), ISwipeable {
    var swipedDirection: Int = 0
    var swipedAction: Runnable? = null

    override val type: Int
        get() = R.id.item_content

    override val isSwipeable: Boolean
        get() = true

    val id = historyItem.id

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemHistoryBinding {
        return ItemHistoryBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: ItemHistoryBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        val context = binding.root.context
        binding.swipeResultContent.visibility = if (swipedDirection != 0) View.VISIBLE else View.GONE
        binding.itemContent.visibility = if (swipedDirection != 0) View.GONE else View.VISIBLE
        // For future updates
        //var swipedAction: CharSequence? = null
        var swipedText: CharSequence? = null

        if (swipedDirection != 0) {
            //swipedAction = context.getString(R.string.action_undo)
            swipedText = if (swipedDirection == ItemTouchHelper.LEFT) context.getString(R.string.message_removed) else "Unknown"
            binding.swipeResultContent.setBackgroundColor(if (swipedDirection == ItemTouchHelper.LEFT) Color.RED else Color.BLUE)
        }
        //binding.swipedAction.text = swipedAction ?: "Kuka"
        binding.swipedText.text = swipedText ?: "Belik"

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
        swipedAction = null
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