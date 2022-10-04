package ru.SnowVolf.convertx.ui.fragments.extended

import ru.SnowVolf.convertx.ui.fragments.base.BaseFragment
import android.widget.EditText
import android.widget.TextView
import android.os.Bundle
import ru.SnowVolf.convertx.R
import android.text.InputType
import android.graphics.Typeface
import android.text.TextWatcher
import android.text.Editable
import android.view.*
import ru.SnowVolf.convertx.ui.Toasty
import android.widget.Toast
import androidx.appcompat.view.menu.MenuBuilder
import java.lang.Exception
import java.lang.NumberFormatException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Snow Volf on 04.03.2017, 6:35
 */
class ColorConverterFragment : BaseFragment() {
    var hexaVoid: EditText? = null
    var smali: TextView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_timestamp, container, false)
        hexaVoid = rootView.findViewById<View>(R.id.Timestamp) as EditText
        hexaVoid!!.hint = "hex"
        hexaVoid!!.inputType = InputType.TYPE_CLASS_TEXT
        hexaVoid!!.isFocusable = true
        smali = rootView.findViewById<View>(R.id.humanDate) as TextView
        smali!!.hint = "smali"
        return rootView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val Mono = Typeface.createFromAsset(activity!!.assets, "fonts/RobotoMono-Regular.ttf")
        hexaVoid!!.typeface = Mono
        hexaVoid!!.typeface = Mono
        hexaVoid!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (!hexaVoid!!.text.toString().isEmpty()) {
                    try {
                        val source = hexaVoid!!.text.toString().lowercase(Locale.getDefault())
                        val digi2let =
                            source.replace("0", "f").replace("1", "e").replace("2", "d").replace("3", "c").replace("4", "b").replace("5", "a")
                                .replace("6", "9").replace("7", "8").replace("8", "7").replace("9", "6").replace("a", "5").replace("b", "4")
                                .replace("c", "3").replace("d", "2").replace("e", "1").replace("f", "0")
                        smali!!.text = digi2let
                    } catch (nfe: NumberFormatException) {
                        nfe.printStackTrace()
                    }
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        //добавляем пункты меню
        menu.add(R.string.convert)
            .setIcon(R.drawable.ic_menu_convert)
            .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
            .setOnMenuItemClickListener { menuItem: MenuItem? ->
                ConvertHexaVoid()
                true
            }
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
        hexaVoid!!.setText("")
        smali!!.text = ""
        Toasty.info(context!!, getString(R.string.cleared), Toast.LENGTH_SHORT, true).show()
    }

    fun ConvertHexaVoid() {
        if (!smali!!.text.toString().isEmpty() && hexaVoid!!.text.toString().isEmpty()) {
            val srcDate = smali!!.text.toString()
            val local: Date
            try {
                local = SimpleDateFormat("dd.MM.YYYY HH:mm:ss", Locale.getDefault()).parse(srcDate)
                hexaVoid!!.setText("" + local.time)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}