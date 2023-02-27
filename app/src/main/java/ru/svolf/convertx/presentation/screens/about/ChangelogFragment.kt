package ru.svolf.convertx.presentation.screens.about

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.svolf.convertx.databinding.FragmentChangelistBinding

class ChangelogFragment : Fragment() {
	private var _binding: FragmentChangelistBinding? = null
	private val binding get() = _binding!!

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		_binding = FragmentChangelistBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding.textChangelog.text = readTextFileFromAssets(requireContext(), "CHANGELOG.txt")
	}

	private fun readTextFileFromAssets(context: Context, fileName: String): String {
		val inputStream = context.assets.open(fileName)
		val buffer = ByteArray(inputStream.available())
		inputStream.read(buffer)
		inputStream.close()
		return String(buffer)
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}