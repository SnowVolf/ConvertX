package ru.svolf.convertx.presentation.base

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.svolf.convertx.R
import ru.svolf.convertx.databinding.FragmentExBinding
import ru.svolf.convertx.presentation.screens.other.TimestampFragment
import ru.svolf.convertx.utils.StringUtils
import ru.svolf.convertx.utils.clear

/*
 * Created by SVolf on 24.02.2023, 22:40
 * This file is a part of "ConvertX" project
 */
open class BaseTextCoderFragment : Fragment() {
	private var _binding: FragmentExBinding? = null
	protected val binding get() = _binding!!
	private val viewModel by viewModels<MainFragmentViewModel>()

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		_binding = FragmentExBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding.fieldInputData.addTextChangedListener(InputWatcher())
		val menuHost = requireActivity() as MenuHost
		menuHost.addMenuProvider(object : MenuProvider {
			override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
				menuInflater.inflate(R.menu.menu_converters, menu)
				if (this@BaseTextCoderFragment !is TimestampFragment) {
					menu.removeItem(R.id.item_place_input)
				}
			}

			override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
				when (menuItem.itemId) {
					R.id.item_place_input -> placeInput()
					R.id.item_clear -> clearAll()
					R.id.item_copy -> copyConvertedValue()
				}
				return true
			}

		}, viewLifecycleOwner)
		viewModel.textSize.observe(viewLifecycleOwner) {
			with(binding) {
				fieldInputData.textSize = it.toFloat()
				textOutput.textSize = it.toFloat()
			}
		}
		onViewsReady()
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	open fun convertInput(input: String) {}

	open fun onViewsReady() {}

	open fun placeInput() {}

	private fun copyConvertedValue() {
		StringUtils.copyToClipboard(requireContext(), binding.textOutput.text.toString())
		Toast.makeText(context, R.string.copied2clipboard, Toast.LENGTH_SHORT).show()
	}

	private fun clearAll() {
		with(binding) {
			fieldInputData.clear()
			textOutput.clear()
		}
	}

	inner class InputWatcher : TextWatcher {
		private lateinit var inputRunnable: Runnable
		private val inputHandler = Handler(Looper.getMainLooper())

		override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

		}

		override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

		}

		override fun afterTextChanged(s: Editable?) {
			if (this::inputRunnable.isInitialized) {
				inputHandler.removeCallbacks(inputRunnable)
			}
			inputRunnable = Runnable {
				if (!s.isNullOrBlank()) {
					convertInput(s.toString())
				}
			}
			inputHandler.postDelayed(inputRunnable, 2000)
		}
	}

}