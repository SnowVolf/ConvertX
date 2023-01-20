package ru.svolf.convertx.presentation.fragments.extended.hash


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.svolf.convertx.R
import ru.svolf.convertx.algorhitms.Checksum.encodeMD5
import ru.svolf.convertx.algorhitms.Checksum.encodeSHA1
import ru.svolf.convertx.algorhitms.Checksum.encodeSHA224
import ru.svolf.convertx.algorhitms.Checksum.encodeSHA384
import ru.svolf.convertx.algorhitms.Checksum.encodeSHA512
import ru.svolf.convertx.settings.Preferences
import ru.svolf.convertx.utils.Toasty

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
                        .append("MD5:\n").append(encodeMD5(source))
                        .toString() + "\n\nSHA-1:\n" + encodeSHA1(source)
                            + "\n\nSHA-224:\n" + encodeSHA224(source)
                            + "\n\nSHA-384:\n" + encodeSHA384(source)
                            + "\n\nSHA-512:\n" + encodeSHA512(source))
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
        if (item.itemId == android.R.id.home) requireActivity().finish()
        return true
    }

    fun clearAllText() {
        data!!.setText("")
        dataOut!!.text = ""
        Toasty.success(requireContext(), getString(R.string.cleared), Toast.LENGTH_SHORT, true).show()
    }
}