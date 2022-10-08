package ru.svolf.convertx.ui.fragments.extended.hash


import ru.svolf.convertx.algorhitms.Checksum.MD5
import ru.svolf.convertx.algorhitms.Checksum.SHA1
import ru.svolf.convertx.algorhitms.Checksum.SHA224
import ru.svolf.convertx.algorhitms.Checksum.SHA384
import ru.svolf.convertx.algorhitms.Checksum.SHA512
import android.widget.EditText
import android.widget.TextView
import android.os.Bundle
import ru.svolf.convertx.R
import android.text.TextWatcher
import android.text.Editable
import android.view.*
import ru.svolf.convertx.ui.Toasty
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.svolf.convertx.settings.Preferences
import java.lang.StringBuilder

/**
 * Created by Snow Volf on 14.02.2017.
 */
class MdShaFragment : Fragment() {
    var data: EditText? = null
    var dataOut: TextView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ex, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        data = view.findViewById<View>(R.id.exData) as EditText
        data!!.setHint(R.string.hint_md_sha)
        data!!.isFocusable = true
        dataOut = view.findViewById<View>(R.id.exText) as TextView

        data!!.textSize = Preferences.fontSize.toFloat()

        data!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                val source = data!!.text.toString()
                /**
                 * Алгоритмы в ChecksumAlgs, чтоб код не заграждать
                 */
                dataOut!!.text =
                    (StringBuilder() //фикс предупреждения Lint: [do not concentrate text displayed with setText. Use resources string with placeholders] в Android Studio
                        .append("MD5:\n").append(MD5(source))
                        .toString() + "\n\nSHA-1:\n" + SHA1(source)
                            + "\n\nSHA-224:\n" + SHA224(source)
                            + "\n\nSHA-384:\n" + SHA384(source)
                            + "\n\nSHA-512:\n" + SHA512(source))
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        //добавляем пункты меню
        menu.add(R.string.clear_all)
            .setIcon(R.drawable.ic_menu_clear_all)
            .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
            .setOnMenuItemClickListener { menuItem: MenuItem? ->
                clearAllText()
                true
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) activity!!.finish()
        return true
    }

    fun clearAllText() {
        data!!.setText("")
        dataOut!!.text = ""
        Toasty.success(context!!, getString(R.string.cleared), Toast.LENGTH_SHORT, true).show()
    }
}