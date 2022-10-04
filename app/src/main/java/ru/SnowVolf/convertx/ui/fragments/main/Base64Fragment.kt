package ru.SnowVolf.convertx.ui.fragments.main

import ru.SnowVolf.convertx.ui.fragments.base.BaseMainFragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import ru.SnowVolf.convertx.R
import com.google.android.material.textfield.TextInputEditText
import android.text.TextWatcher
import android.text.Editable
import android.util.Base64
import android.view.View
import android.widget.*
import ru.SnowVolf.convertx.settings.Preferences
import ru.SnowVolf.convertx.ui.Toasty
import java.lang.Exception

/**
 * Created by Snow Volf on 28.01.2017.
 */
class Base64Fragment : BaseMainFragment() {
    var AlgSwitcher: Spinner? = null
    var algorhitm = 0
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
        mInputField!!.setHint(R.string.hint_utf)
        mOutputField!!.setHint(R.string.hint_base64)
        AlgSwitcher!!.visibility = View.VISIBLE
        val adapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(activity!!, R.array.baseAlgVal, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        AlgSwitcher!!.adapter = adapter
        TITLE = R.string.dr_base64
        val Algs = intArrayOf(Base64.NO_WRAP, Base64.NO_CLOSE, Base64.NO_PADDING, Base64.URL_SAFE, Base64.CRLF, Base64.DEFAULT)
        AlgSwitcher!!.setSelection(Preferences.getSpinnerPosition("spinner.base64"))
        AlgSwitcher!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, `is`: View, pos: Int, id: Long) {
                for (i in 0 until AlgSwitcher!!.count) {
                    if (pos == i) {
                        algorhitm = Algs[i]
                    }
                }
                val spinnerPosition1 = AlgSwitcher!!.selectedItemPosition
                Preferences.setSpinnerPosition("spinner.base64", spinnerPosition1)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                algorhitm = Base64.NO_WRAP
            }
        }
        mInputField!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                val baseContent = mInputField!!.text.toString()
                val codeGirl: String
                val EBytes = Base64.encode(baseContent.toByteArray(), algorhitm)
                codeGirl = String(EBytes)
                mOutputField!!.setText(codeGirl)
                mOutputField!!.setSelection(mOutputField!!.text!!.length) //ставим курсор в конец строки
            }
        })
    }

    override fun Convert() {
        if (mInputField!!.text.toString().isEmpty() && !mOutputField!!.text.toString().isEmpty()) {
            val baseSoup = mOutputField!!.text.toString()
            try {
                val decodeGirl: String
                val DBytes = Base64.decode(baseSoup.toByteArray(), algorhitm)
                decodeGirl = String(DBytes)
                mInputField!!.setText(decodeGirl)
            } catch (ex: Exception) {
                Toasty.error(context!!, getString(R.string.base64_error), Toast.LENGTH_SHORT, true).show()
            }
            mInputField!!.setSelection(mInputField!!.text!!.length)
        }
    }

    override fun clearAllText() {
        mOutputField!!.setText("")
        mInputField!!.setText("")
        Toasty.success(context!!, getString(R.string.cleared), Toast.LENGTH_SHORT, true).show()
    }
}