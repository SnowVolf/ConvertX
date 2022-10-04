package ru.SnowVolf.convertx.ui.fragments.main

import ru.SnowVolf.convertx.ui.fragments.base.BaseMainFragment
import android.os.Bundle
import ru.SnowVolf.convertx.R
import com.google.android.material.textfield.TextInputEditText
import android.text.InputType
import android.text.TextWatcher
import android.text.Editable
import android.text.method.DigitsKeyListener
import android.view.*
import android.widget.*
import androidx.appcompat.view.menu.MenuBuilder
import ru.SnowVolf.convertx.algorhitms.DecoderAlgs
import kotlin.Throws
import ru.SnowVolf.convertx.ui.Toasty
import org.acra.ACRA
import ru.SnowVolf.convertx.settings.Preferences
import java.io.UnsupportedEncodingException
import java.lang.Exception
import java.lang.NumberFormatException
import java.math.BigInteger
import java.nio.charset.Charset
import java.util.*

/**
 * Created by Snow Volf on 28.01.2017.
 */
class HexFragment : BaseMainFragment() {
    private var AlgSwitcher: Spinner? = null
    private var isInt = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_main, container, false)
        AlgSwitcher = rootView.findViewById<View>(R.id.decodeMode) as Spinner
        mInputField = rootView.findViewById<View>(R.id.inputText) as TextInputEditText
        mOutputField = rootView.findViewById<View>(R.id.outputText) as TextInputEditText
        mCopyInput = rootView.findViewById<View>(R.id.copy_btn2) as Button
        mCopyOutput = rootView.findViewById<View>(R.id.copy_btn) as Button
        mClearOutput = rootView.findViewById<View>(R.id.clear_btn) as Button
        mPasteInput = rootView.findViewById<View>(R.id.paste_btn) as Button
        mPasteOutput = rootView.findViewById<View>(R.id.paste_btn2) as Button
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mOutputField!!.typeface = Mono
        mInputField!!.typeface = Mono
        mInputField!!.hint = "String"
        mOutputField!!.setHint(R.string.hint_hex)
        AlgSwitcher!!.visibility = View.VISIBLE
        val adapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(activity!!, R.array.hexMode, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        AlgSwitcher!!.adapter = adapter
        TITLE = R.string.dr_hex
        AlgSwitcher!!.setSelection(Preferences.getSpinnerPosition("spinner.hex"))
        AlgSwitcher!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, `is`: View, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        Preferences.setSpinnerPosition("spinner.hex", position)
                        addListener(0)
                    }
                    1 -> {
                        addListener(1)
                        Preferences.setSpinnerPosition("spinner.hex", position)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                addListener(0)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        //добавляем пункты меню
        menu.add(R.string.convert)
            .setIcon(R.drawable.ic_menu_convert)
            .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
            .setOnMenuItemClickListener { menuItem: MenuItem? ->
                if (!isInt) try {
                    ConvertHex()
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                }
                if (isInt) {
                    ConvertFromHex()
                }
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

    private fun addListener(mode: Int) {
        if (mode == 0) {
            isInt = false
            mInputField!!.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
            mInputField!!.hint = "String"
            mInputField!!.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun afterTextChanged(editable: Editable) {
                    val plainText = mInputField!!.text.toString()
                    val hexGirl = String.format("%040X", BigInteger(1, plainText.toByteArray()))
                    mOutputField!!.setText(String.format("0x%s", hexGirl))
                    mOutputField!!.setSelection(mOutputField!!.text!!.length) //ставим курсор в конец строки*/
                }
            })
        }
        if (mode == 1) {
            isInt = true
            mInputField!!.hint = "Integer"
            mOutputField!!.keyListener = DigitsKeyListener.getInstance("0123456789ABCDEFabcdefx")
            mInputField!!.inputType = InputType.TYPE_CLASS_PHONE
            mInputField!!.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun afterTextChanged(editable: Editable) {
                    try {
                        val value = mInputField!!.text.toString().toInt()
                        val integer = DecoderAlgs.ConvertInt2Hex(value)
                        mOutputField!!.setText(integer)
                    } catch (ignore: NumberFormatException) {
                    }
                    mOutputField!!.setSelection(mOutputField!!.text!!.length) //ставим курсор в конец строки*/
                }
            })
        }
    }

    @Throws(UnsupportedEncodingException::class)
    private fun ConvertHex() {
        /**
         * Закомментироал код обработки, потому как он есть в TextWatcher-e
         * Удалять пока не буду, вдруг пригодится
         */
        if (mInputField!!.text.toString().isEmpty() && !mOutputField!!.text.toString().isEmpty()) {
            try {
                var text = mOutputField!!.text.toString()
                text = text.replace("\\W".toRegex(), "").lowercase(Locale.getDefault())
                //убираем 0x в начале, если он есть
                if (text.startsWith("0x")) {
                    text = text.replaceFirst("0x".toRegex(), "")
                }
                val bytes = DecoderAlgs.hexDecode(text)
                val result = String(bytes, Charsets.UTF_8)
                mInputField!!.setText(result)
                mInputField!!.setSelection(mInputField!!.text!!.length)
            } catch (e: Exception) {
                Toasty.error(context!!, getString(R.string.hex_error), Toast.LENGTH_LONG, true).show()
            }
        }
    }

    private fun ConvertFromHex() {
        if (mInputField!!.text.toString().isEmpty() && !mOutputField!!.text.toString().isEmpty()) {
            var text = mOutputField!!.text.toString()
            //убираем 0x в начале, если он есть
            if (text.startsWith("0x")) {
                text = text.replaceFirst("0x".toRegex(), "")
            }
            try {
                val dec = DecoderAlgs.ConvertHex2Int(text)
                val d = Integer.toString(dec)
                try {
                    mInputField!!.setText(d)
                } catch (e: Exception) {
                    ACRA.getErrorReporter().handleException(e)
                }
            } catch (nfe: NumberFormatException) {
                Toasty.error(context!!, getString(R.string.incorrect_hex_int), Toast.LENGTH_LONG, true).show()
            }
        }
    }

    override fun clearAllText() {
        mInputField!!.setText("")
        mOutputField!!.setText("")
        Toasty.success(context!!, getString(R.string.cleared), Toast.LENGTH_SHORT, true).show()
    }
}