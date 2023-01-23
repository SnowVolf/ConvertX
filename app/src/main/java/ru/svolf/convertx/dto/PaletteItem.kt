package ru.svolf.convertx.dto

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import ru.svolf.convertx.R
import ru.svolf.convertx.api.Color
import ru.svolf.convertx.databinding.ItemPaletteColorsBinding

/*
 * Created by SVolf on 23.01.2023, 16:27
 * This file is a part of "ConvertX" project
 */
class PaletteItem(var color: Color) : AbstractBindingItem<ItemPaletteColorsBinding>() {

    override val type: Int
        get() = R.id.color_accent

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemPaletteColorsBinding {
        return ItemPaletteColorsBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: ItemPaletteColorsBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        binding.colorCode.text = color.hex
        binding.colorAccent.text = color.accent
        binding.colorView.setImageDrawable(ColorDrawable(android.graphics.Color.parseColor(color.hex)))
    }

    override fun unbindView(binding: ItemPaletteColorsBinding) {
        super.unbindView(binding)
        binding.colorCode.text = null
        binding.colorAccent.text = null
        binding.colorView.setImageDrawable(null)
    }

}