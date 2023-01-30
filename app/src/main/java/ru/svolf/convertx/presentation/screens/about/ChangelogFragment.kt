package ru.svolf.convertx.presentation.screens.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.svolf.convertx.databinding.ActivityChangelistBinding
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class ChangelogFragment : Fragment() {
    private var _binding: ActivityChangelistBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ActivityChangelistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sb = StringBuilder()
        try {
            val br = BufferedReader(InputStreamReader(requireContext().assets.open("CHANGELOG.txt"), "UTF-8"))
            var line: String?
            while (br.readLine().also { line = it } != null) {
                sb.append(line).append("\n")
            }
            br.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        binding.textChangelog.text = sb
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}