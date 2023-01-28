package ru.svolf.convertx.data.model

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import ru.svolf.convertx.R
import ru.svolf.convertx.databinding.ItemMainMenuBinding

class MainMenuVH(var id: Int, private var icon: Int, private var title: Int) : AbstractBindingItem<ItemMainMenuBinding>() {


    override val type: Int
        get() = R.id.item_title

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemMainMenuBinding {
        return ItemMainMenuBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: ItemMainMenuBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        binding.itemIcon.setImageResource(icon)
        binding.itemTitle.setText(title)
    }

    override fun unbindView(binding: ItemMainMenuBinding) {
        super.unbindView(binding)
        binding.itemIcon.setImageDrawable(null)
        binding.itemTitle.text = null
    }
}