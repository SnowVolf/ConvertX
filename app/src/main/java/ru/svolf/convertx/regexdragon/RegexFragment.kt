package ru.svolf.convertx.regexdragon

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import ru.svolf.convertx.R
import ru.svolf.convertx.databinding.ActivityRegexDragonBinding

class RegexFragment : Fragment() {
    private lateinit var binding: ActivityRegexDragonBinding
    private lateinit var mediator: TabLayoutMediator
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
    }

    private fun setupViewPager(viewPager: ViewPager2) {
        val adapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        adapter.addFragment(RegexValidatorFragment(), getString(R.string.tab_regex))
        adapter.addFragment(SpurFragment(), getString(R.string.tab_spur))
        viewPager.adapter = adapter
        mediator = TabLayoutMediator(binding.tablayout, binding.pager) { tabLayout, index ->
            tabLayout.icon = ContextCompat.getDrawable(requireContext(), icTab[index])
        }
        mediator.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) requireActivity().finish()
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediator.detach()
    }

}