package ru.svolf.convertx.presentation.screens.palette

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import ru.svolf.convertx.R
import ru.svolf.convertx.data.model.ColorsVH
import ru.svolf.convertx.data.model.PaletteVH
import ru.svolf.convertx.databinding.FragmentColorPaletteBinding
import ru.svolf.convertx.utils.StringUtils

/*
 * Created by SVolf on 23.01.2023, 16:09
 * This file is a part of "ConvertX" project
 */
class PaletteFragment : Fragment() {
    private var _binding: FragmentColorPaletteBinding? = null
	private val binding get() = _binding!!
	private val viewModel by viewModels<PaletteViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentColorPaletteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val itemAdapter = ItemAdapter<ColorsVH>()
		val paletteAdapter = ItemAdapter<PaletteVH>()
		val itemClickAdapter = FastAdapter.with(itemAdapter)
		val paletteClickAdapter = FastAdapter.with(paletteAdapter)

		viewModel.getCurrentPalette().observe(viewLifecycleOwner) {
			FastAdapterDiffUtil[paletteAdapter] = it
		}

		viewModel.getAllPalettes().observe(viewLifecycleOwner) {
			FastAdapterDiffUtil[itemAdapter] = it
		}

		itemClickAdapter.onClickListener = { _, _, _, ps ->
			viewModel.setCurrentPalette(ps)
			true
		}

		paletteClickAdapter.onClickListener = { _, _, paletteItem, _ ->
			StringUtils.copyToClipboard(requireContext(), paletteItem.color.hex)
			Toast.makeText(context, R.string.copied2clipboard, Toast.LENGTH_SHORT).show()
			true
		}

		binding.palettesList.adapter = itemClickAdapter
		binding.colorList.adapter = paletteClickAdapter

		Handler(Looper.myLooper()!!).postDelayed({
			//restore selections (this has to be done after the items were added
			paletteClickAdapter.withSavedInstanceState(savedInstanceState)
		}, 50)

	}

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}