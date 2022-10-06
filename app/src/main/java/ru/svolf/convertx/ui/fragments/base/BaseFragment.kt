package ru.svolf.convertx.ui.fragments.base

import androidx.annotation.StringRes
import ru.svolf.convertx.R
import android.view.View
import androidx.fragment.app.Fragment
import java.lang.Exception

/**
 * Created by Snow Volf on 20.06.2017, 0:43
 */
open class BaseFragment : Fragment() {

    lateinit var rootView: View

    @JvmField
    @StringRes
    var activityTitle = R.string.app_name

    override fun onResume() {
        super.onResume()
        try {
            requireActivity().setTitle(activityTitle)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}