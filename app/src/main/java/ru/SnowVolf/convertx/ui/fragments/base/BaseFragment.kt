package ru.SnowVolf.convertx.ui.fragments.base

import android.graphics.Typeface
import androidx.annotation.StringRes
import ru.SnowVolf.convertx.R
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import java.lang.Exception

/**
 * Created by Snow Volf on 20.06.2017, 0:43
 */
open class BaseFragment : Fragment() {

    lateinit var rootView: View
    @JvmField
    var Mono: Typeface? = null

    @JvmField
    @StringRes
    var TITLE = R.string.app_name
    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onActivityCreated(savedState: Bundle?) {
        super.onActivityCreated(savedState)
        requireActivity().setTitle(TITLE)
        Mono = Typeface.createFromAsset(requireActivity().assets, "fonts/RobotoMono-Regular.ttf")
    }

    override fun onResume() {
        super.onResume()
        try {
            requireActivity().setTitle(TITLE)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}