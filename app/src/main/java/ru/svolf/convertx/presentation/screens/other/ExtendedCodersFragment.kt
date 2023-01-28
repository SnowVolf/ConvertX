package ru.svolf.convertx.presentation.screens.other

import android.os.Bundle
import android.view.*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
                0 -> navController.navigate(R.id.action_listCoders_to_mdSha)
                1 -> navController.navigate(R.id.action_listCoders_to_CRC)
                2 -> navController.navigate(R.id.action_listCoders_to_adler)
                3 -> navController.navigate(R.id.action_listCoders_to_xmlDecodeFragment)
                4 -> navController.navigate(R.id.action_listCoders_to_timestampFragment)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            requireActivity().finish()
        return true
    }

}
