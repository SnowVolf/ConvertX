package ru.svolf.convertx.presentation.screens.other

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import ru.svolf.convertx.R

/**
 * Created by Snow Volf on 14.02.2017.
 */

class ExtendedCodersFragment : Fragment() {
    private var listView: ListView? = null
    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ex_encoders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView = view.findViewById(R.id.coder_list) as ListView
        navController = NavHostFragment.findNavController(this)
        val items = resources.getStringArray(R.array.coderNames)
        val titleAdapter = ArrayAdapter(requireContext(), R.layout.list_row, R.id.list_title, items)
        listView!!.adapter = titleAdapter
        listView!!.setOnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> navController.navigate(R.id.action_listCoders_to_CRC)
                1 -> navController.navigate(R.id.action_listCoders_to_adler)
                2 -> navController.navigate(R.id.action_listCoders_to_xmlDecodeFragment)
                3 -> navController.navigate(R.id.action_listCoders_to_timestampFragment)
            }
        }
    }

}
