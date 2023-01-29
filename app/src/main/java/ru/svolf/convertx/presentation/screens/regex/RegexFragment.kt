package ru.svolf.convertx.presentation.screens.regex

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import ru.svolf.convertx.R
import ru.svolf.convertx.databinding.ActivityRegexDragonBinding

class RegexFragment : Fragment() {
    private var _binding: ActivityRegexDragonBinding? = null
    private val binding get() = _binding!!
    private lateinit var mediator: TabLayoutMediator
    private val icTab = intArrayOf(
        R.drawable.dragon,
        R.drawable.ic_receipt
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ActivityRegexDragonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager(binding.pager)
    }

    private fun setupViewPager(viewPager: ViewPager2) {
        val adapter = ViewPagerAdapter(childFragmentManager, lifecycle).apply {
            addFragment(RegexValidatorFragment(), getString(R.string.tab_regex))
            addFragment(SpurFragment(), getString(R.string.tab_spur))
        }
        viewPager.adapter = adapter
        mediator = TabLayoutMediator(binding.tablayout, binding.pager) { tabLayout, index ->
            tabLayout.icon = ContextCompat.getDrawable(requireContext(), icTab[index])
        }
        mediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediator.detach()
        _binding = null
    }

}