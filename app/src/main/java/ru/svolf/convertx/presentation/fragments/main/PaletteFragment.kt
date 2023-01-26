package ru.svolf.convertx.presentation.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import ru.svolf.convertx.R
import ru.svolf.convertx.data.model.ColorsItem
import ru.svolf.convertx.data.model.PaletteItem
import ru.svolf.convertx.databinding.FragmentColorPaletteBinding
import ru.svolf.convertx.utils.StringUtils
import java.util.stream.Collectors

/*
 * Created by SVolf on 23.01.2023, 16:09
 * This file is a part of "ConvertX" project
 */
class PaletteFragment : Fragment() {
    private var _binding: FragmentColorPaletteBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: PaletteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[PaletteViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentColorPaletteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val itemAdapter = ItemAdapter<ColorsItem>()
        val paletteAdapter = ItemAdapter<PaletteItem>()
        val itemClickAdapter = FastAdapter.with(itemAdapter)
        val paletteClickAdapter = FastAdapter.with(paletteAdapter)

        viewModel.getPalettes().observe(viewLifecycleOwner) {
            itemAdapter.add(
                it.stream().map(::ColorsItem).collect(Collectors.toList())
            )
            it[0].colors.forEach { c ->
                paletteAdapter.add(PaletteItem(c))
            }
        }

        itemClickAdapter.onClickListener = { _, _, colorsItem, _ ->
            if (paletteAdapter.adapterItemCount != 0) {
                paletteAdapter.clear()
            }
            for (c in colorsItem.palette.colors) {
                paletteAdapter.add(PaletteItem(c))
            }
            true
        }

        paletteClickAdapter.onClickListener = { _, _, paletteItem, _ ->
            StringUtils.copyToClipboard(requireContext(), paletteItem.color.hex)
            Toast.makeText(context, R.string.copied2clipboard, Toast.LENGTH_SHORT).show()
            true
        }

        binding.palettesList.adapter = itemClickAdapter
        binding.colorList.adapter = paletteClickAdapter

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}