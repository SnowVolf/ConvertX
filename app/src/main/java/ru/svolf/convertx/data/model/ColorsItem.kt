package ru.svolf.convertx.data.model

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import ru.svolf.convertx.R
import ru.svolf.convertx.data.entity.Palette
import ru.svolf.convertx.databinding.ItemPaletteMainBinding

/*
 * Created by SVolf on 23.01.2023, 16:17
 * This file is a part of "ConvertX" project
 */
class ColorsItem(var palette: Palette) : AbstractBindingItem<ItemPaletteMainBinding>() {

    override val type: Int
        get() = R.id.item_main_palette

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemPaletteMainBinding {
        return ItemPaletteMainBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: ItemPaletteMainBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        binding.itemMainPalette.backgroundTintList = ColorStateList.valueOf(Color.parseColor(palette.colors[5].hex))
    }

    override fun unbindView(binding: ItemPaletteMainBinding) {
        super.unbindView(binding)
        binding.itemMainPalette.backgroundTintList = null
    }

}