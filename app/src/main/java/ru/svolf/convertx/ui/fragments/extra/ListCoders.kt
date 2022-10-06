package ru.svolf.convertx.ui.fragments.extra

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.FragmentTransaction
import ru.svolf.convertx.R
import ru.svolf.convertx.ui.fragments.base.BaseFragment
import ru.svolf.convertx.ui.fragments.extended.TimestampFragment
import ru.svolf.convertx.ui.fragments.extended.XmlDecodeFragment
import ru.svolf.convertx.ui.fragments.extended.hash.Adler
import ru.svolf.convertx.ui.fragments.extended.hash.CRC
import ru.svolf.convertx.ui.fragments.extended.hash.MdSha

/**
 * Created by Snow Volf on 14.02.2017.
 */

class ListCoders : BaseFragment() {
    private var listView: ListView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater!!.inflate(R.layout.fragment_ex_encoders, container, false)
        listView = rootView!!.findViewById(R.id.coder_list) as ListView
        return rootView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activityTitle = R.string.dr_other1
        val items = resources.getStringArray(R.array.coderNames)
        val titleAdapter = ArrayAdapter(requireContext(), R.layout.list_row, R.id.list_title, items)
        listView!!.adapter = titleAdapter
        listView!!.setOnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> startMD()
                1 -> startCRC()
                2 -> startAdler()
                3 -> startXML()
                4 -> startTimestamp()
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

    fun startAdler() {
        requireActivity().supportFragmentManager.popBackStack()
        requireActivity().supportFragmentManager
            .beginTransaction().replace(R.id.frame_container, Adler())
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    fun startCRC() {
        requireActivity().supportFragmentManager.popBackStack()
        requireActivity().supportFragmentManager
            .beginTransaction().replace(R.id.frame_container, CRC())
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    fun startMD() {
        requireActivity().supportFragmentManager.popBackStack()
        requireActivity().supportFragmentManager
            .beginTransaction().replace(R.id.frame_container, MdSha())
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    fun startXML() {
        requireActivity().supportFragmentManager.popBackStack()
        requireActivity().supportFragmentManager
            .beginTransaction().replace(R.id.frame_container, XmlDecodeFragment())
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    fun startTimestamp() {
        requireActivity().supportFragmentManager.popBackStack()
        requireActivity().supportFragmentManager
            .beginTransaction().replace(R.id.frame_container, TimestampFragment())
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }
}
