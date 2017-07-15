package ru.SnowVolf.convertx.ui.fragments.extra

import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.support.v7.view.menu.MenuBuilder
import android.view.*
import android.widget.ArrayAdapter
import android.widget.ListView
import ru.SnowVolf.convertx.R
import ru.SnowVolf.convertx.ui.fragments.base.BaseFragment
import ru.SnowVolf.convertx.ui.fragments.extended.TimestampFragment
import ru.SnowVolf.convertx.ui.fragments.extended.UnescaperFragment
import ru.SnowVolf.convertx.ui.fragments.extended.hash.Adler
import ru.SnowVolf.convertx.ui.fragments.extended.hash.CRC
import ru.SnowVolf.convertx.ui.fragments.extended.hash.MdSha

/**
 * Created by Snow Volf on 14.02.2017.
 */

class ListCoders : BaseFragment() {
    private var listView: ListView? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
        TITLE = R.string.dr_other1
        val items = resources.getStringArray(R.array.coderNames)
        val titleAdapter = ArrayAdapter(context, R.layout.list_row, R.id.list_title, items)
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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        var menu = menu
        if (menu != null) {
            menu.clear()
        } else
            menu = MenuBuilder(context)
        menu.add("").setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home)
            activity.finish()
        return true
    }

    fun startAdler() {
        activity.supportFragmentManager.popBackStack()
        activity.supportFragmentManager
                .beginTransaction().replace(R.id.frame_container, Adler())
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
    }

    fun startCRC() {
        activity.supportFragmentManager.popBackStack()
        activity.supportFragmentManager
                .beginTransaction().replace(R.id.frame_container, CRC())
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
    }

    fun startMD() {
        activity.supportFragmentManager.popBackStack()
        activity.supportFragmentManager
                .beginTransaction().replace(R.id.frame_container, MdSha())
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
    }

    fun startXML() {
        activity.supportFragmentManager.popBackStack()
        activity.supportFragmentManager
                .beginTransaction().replace(R.id.frame_container, UnescaperFragment())
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
    }

    fun startTimestamp() {
        activity.supportFragmentManager.popBackStack()
        activity.supportFragmentManager
                .beginTransaction().replace(R.id.frame_container, TimestampFragment())
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
    }
}
