package ru.svolf.convertx.ui.fragments.extended

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.svolf.convertx.R
import ru.svolf.convertx.algorhitms.Decoder
import ru.svolf.convertx.settings.Preferences
import ru.svolf.convertx.ui.Toasty
import ru.svolf.convertx.utils.StringUtils

/**
 * Created by Snow Volf on 22.02.2017, 19:51
 */

class XmlDecodeFragment : Fragment() {
    lateinit var data: EditText
    lateinit var dataOut: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_ex, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        data = view.findViewById(R.id.exData) as EditText
        data.hint = "Текст зашифрованого XML"
        data.isFocusable = true
        dataOut = view.findViewById(R.id.exText) as TextView
        dataOut.hint = "Преобразует символы \"&quot;\", \"&apos;\", т.д. в нормальный текст"

        data.textSize = Preferences.fontSize.toFloat()
        data.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun afterTextChanged(editable: Editable) {
                val source = data.text.toString()
                val unescaped = Decoder.unescapeXml(source)
                dataOut.text = unescaped
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        //добавляем пункты меню
        menu.add(R.string.copy2clipboard)
            .setIcon(R.drawable.ic_menu_copy)
            .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
            .setOnMenuItemClickListener { menuItem ->
                StringUtils.copyToClipboard(requireContext(), dataOut.text.toString())
                showToast(context, getString(R.string.copied2clipboard))

                true
            }
        menu.add(R.string.clear_all)
            .setIcon(R.drawable.ic_menu_clear_all)
            .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
            .setOnMenuItemClickListener { menuItem ->
                clearAllText()
                true
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item!!.itemId == android.R.id.home)
            requireActivity().finish()
        return true
    }

    fun clearAllText() {
        data.setText("")
        dataOut.text = ""
        Toasty.success(requireContext(), getString(R.string.cleared), Toast.LENGTH_SHORT, true).show()
    }

    fun showToast(context: Context?, text: String?) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

}