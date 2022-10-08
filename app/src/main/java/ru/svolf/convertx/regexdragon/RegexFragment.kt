package ru.svolf.convertx.regexdragon

import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import ru.svolf.convertx.R
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment

class RegexFragment : Fragment() {
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null
    private lateinit var rootView: View
    private val icTab = intArrayOf(
        R.drawable.dragon,
        R.drawable.ic_toast_info_outline
    )

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
        
    }

    private fun setupViewPager(viewPager: ViewPager?) {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(RegexValidatorFragment(), "Regex")
        adapter.addFragment(SpurFragment(), "Шпора")
        
        viewPager!!.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) requireActivity().finish()
        return true
    }

}