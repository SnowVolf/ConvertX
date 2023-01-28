package ru.svolf.convertx.presentation.screens.about

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import ru.svolf.convertx.R
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class ChangelogFragment : Fragment() {
    private var changelog: TextView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_changelist, container, false)
    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setHasOptionsMenu(true)
        
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changelog = view.findViewById<View>(R.id.text_changelog) as TextView
        val sb = StringBuilder()
        try {
            val br = BufferedReader(InputStreamReader(requireContext().assets.open("CHANGELOG.txt"), "UTF-8"))
            var line: String?
            while (br.readLine().also { line = it } != null) {
                sb.append(line).append("\n")
            }
            br.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

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