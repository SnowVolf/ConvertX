package ru.SnowVolf.convertx.ui.fragments.extra

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.appcompat.view.menu.MenuBuilder
import ru.SnowVolf.convertx.R
import ru.SnowVolf.convertx.settings.DefStrings.TAG
import ru.SnowVolf.convertx.ui.activity.MainActivity
import ru.SnowVolf.convertx.ui.fragments.base.BaseFragment
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class ChangelistFragment : BaseFragment() {
    private var changelog: TextView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.activity_changelist, container, false)
        changelog = rootView.findViewById<View>(R.id.text_changelog) as TextView
        return rootView
    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setHasOptionsMenu(true)
        Log.i(TAG, "StartFragment : Changelist")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        TITLE = R.string.changelog
        (activity as MainActivity?)!!.setToolbarSubtitle(getBuildName(requireContext()))
        val sb = StringBuilder()
        try {
            val br = BufferedReader(InputStreamReader(requireContext().assets.open("CHANGELOG.javagirl"), "UTF-8"))
            var line: String?
            while (br.readLine().also { line = it } != null) {
                sb.append(line).append("\n")
            }
            br.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val Mono = Typeface.createFromAsset(requireContext().assets, "fonts/RobotoMono-Regular.ttf")
        changelog!!.setTypeface(Mono)
        changelog!!.text = sb
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        menu.add("").setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) requireActivity().finish()
        return true
    }

    fun getBuildName(context: Context): String {
        var programBuild = "" //context.getString(R.string.app_name);
        try {
            val pkg = context.packageName
            val pkgInfo = context.packageManager.getPackageInfo(pkg, PackageManager.GET_META_DATA)
            programBuild += " v." + pkgInfo.versionName
        } catch (e1: PackageManager.NameNotFoundException) {
            e1.printStackTrace()
        }
        return programBuild
    }
}