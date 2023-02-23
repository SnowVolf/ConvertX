package ru.svolf.convertx.presentation.screens.regex

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.svolf.convertx.databinding.FragmentSpurBinding

/**
 * Created by Snow Volf on 02.03.2017, 20:31
 */
class SpurFragment : Fragment() {
    private var _binding: FragmentSpurBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		_binding = FragmentSpurBinding.inflate(inflater, container, false)
		return binding.root
	}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.regexSpur.text = readTextFileFromAssets(requireContext(), "SPUR.md")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun readTextFileFromAssets(context: Context, fileName: String): String {
        val inputStream = context.assets.open(fileName)
        val buffer = ByteArray(inputStream.available())
        inputStream.read(buffer)
        inputStream.close()
        return String(buffer)
    }
}