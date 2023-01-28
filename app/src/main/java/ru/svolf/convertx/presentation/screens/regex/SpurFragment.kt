package ru.svolf.convertx.presentation.screens.regex

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import ru.svolf.convertx.R
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * Created by Snow Volf on 02.03.2017, 20:31
 */
class SpurFragment : Fragment() {
    private var spurr: TextView? = null
    private lateinit var rootView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_spur, container, false)
        spurr = rootView.findViewById<View>(R.id.regex_spur) as TextView
        return rootView
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        menu.add("").setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) activity!!.finish()
        return true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val sb = StringBuilder()
        try {
            val br = BufferedReader(InputStreamReader(context!!.assets.open("SPUR.md"), "UTF-8"))
            var line: String?
            while (br.readLine().also { line = it } != null) {
                sb.append(line).append("\n")
            }
            br.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        spurr!!.text = sb
    }
}