package ru.SnowVolf.convertx.regexdragon

import ru.SnowVolf.convertx.settings.DefStrings.TAG
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import ru.SnowVolf.convertx.R
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.view.menu.MenuBuilder
import ru.SnowVolf.convertx.settings.DefStrings
import ru.SnowVolf.convertx.regexdragon.ViewPagerAdapter
import ru.SnowVolf.convertx.regexdragon.RegexDragonValidator
import ru.SnowVolf.convertx.regexdragon.SpurFragment
import ru.SnowVolf.convertx.regexdragon.TheoryFragment
import androidx.fragment.app.Fragment

class RegexDragonFragment : Fragment() {
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null
    private lateinit var rootView: View
    private val icTab = intArrayOf(
        R.drawable.dragon,
        R.drawable.ic_menu_spur,
        R.drawable.ic_toast_info_outline
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "RegexDragon")
        Log.w(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        activity!!.title = "RegexDragon"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.activity_regex_dragon, container, false)
        viewPager = rootView.findViewById<View>(R.id.pager) as ViewPager
        tabLayout = rootView.findViewById<View>(R.id.tablayout) as TabLayout
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewPager(viewPager)
        tabLayout!!.setupWithViewPager(viewPager)
        setIcons()
    }

    private fun setIcons() {
        tabLayout!!.getTabAt(0)!!.setIcon(icTab[0])
        tabLayout!!.getTabAt(1)!!.setIcon(icTab[1])
        tabLayout!!.getTabAt(2)!!.setIcon(icTab[2])
        Log.i(TAG, "Set Icons as Tab indicators")
    }

    private fun setupViewPager(viewPager: ViewPager?) {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(RegexDragonValidator(), "Regex")
        adapter.addFragment(SpurFragment(), "Шпора")
        adapter.addFragment(TheoryFragment(), "Теория")
        Log.i(TAG, "Bind Fragments : Total : " + adapter.count)
        viewPager!!.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) activity!!.finish()
        return true
    }

}