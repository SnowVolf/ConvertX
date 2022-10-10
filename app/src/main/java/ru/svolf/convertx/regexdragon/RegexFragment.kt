package ru.svolf.convertx.regexdragon

import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import ru.svolf.convertx.R
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import ru.svolf.convertx.databinding.ActivityRegexDragonBinding

class RegexFragment : Fragment() {
    private lateinit var binding: ActivityRegexDragonBinding
    private val icTab = intArrayOf(
        R.drawable.dragon,
        R.drawable.ic_receipt
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ActivityRegexDragonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager(binding.pager)
        binding.tablayout.setupWithViewPager(binding.pager)
        setIcons()
    }

    private fun setIcons() {
        binding.tablayout.getTabAt(0)!!.setIcon(icTab[0])
        binding.tablayout.getTabAt(1)!!.setIcon(icTab[1])
        
    }

    private fun setupViewPager(viewPager: ViewPager?) {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(RegexValidatorFragment(), getString(R.string.tab_regex))
        adapter.addFragment(SpurFragment(), getString(R.string.tab_spur))
        
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