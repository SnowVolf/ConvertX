package ru.SnowVolf.convertx.ui.fragments.main

import ru.SnowVolf.convertx.ui.fragments.base.BaseMainFragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import ru.SnowVolf.convertx.R
import com.google.android.material.textfield.TextInputEditText
import android.text.TextWatcher
import android.text.Editable
import android.view.View
import android.widget.Button
import ru.SnowVolf.convertx.ui.Toasty
import android.widget.Toast
import ru.SnowVolf.convertx.algorhitms.DecoderAlgs
import java.lang.StringBuilder

/**
 * Created by Snow Volf on 28.01.2017.
 */
class UnicodeFragment : BaseMainFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_main, container, false)
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
        mCopyInput!!.setOnClickListener { v: View? -> copyInput(v) }
        mCopyOutput!!.setOnClickListener { v: View? -> copyOutput(v) }
        mClearOutput!!.setOnClickListener { view: View? -> clearOutput() }
        mInputField!!.setHint(R.string.hint_utf)
        mOutputField!!.setHint(R.string.hint_unicode)
        mOutputField!!.typeface = Mono
        mOutputField!!.typeface = Mono
        TITLE = R.string.dr_unicode
        mInputField!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                val utfSource = mInputField!!.text.toString()
                val unicodeValue = StringBuilder()
                for (i in 0 until utfSource.length) unicodeValue.append("\\u").append(Integer.toHexString(utfSource[i].code or 0x10000).substring(1))
                mOutputField!!.setText(unicodeValue)
                //фикс неправильной установки курсора
                mOutputField!!.setSelection(mOutputField!!.text!!.length)
            }
        })
    }

    override fun Convert() {
        if (!mOutputField!!.text.toString().isEmpty() && mInputField!!.text.toString().isEmpty()) {
            val unicodeContent = mOutputField!!.text.toString()

            //проверяем валидность String, и переносим его в нужное поле при необходимости
            if (!unicodeContent.matches("(\\\\u[0-9]{4})+".toRegex())) {
                mInputField!!.setText(unicodeContent)
                Toasty.info(context!!, getString(R.string.text_moved_correct), Toast.LENGTH_SHORT, true).show()
            }
            val text = DecoderAlgs.unescapeJava(unicodeContent)
            mInputField!!.setText(text)
            mInputField!!.setSelection(mInputField!!.text!!.length)
        }
    }

    override fun clearAllText() {
        mOutputField!!.setText("")
        mInputField!!.setText("")
        Toasty.success(context!!, getString(R.string.cleared), Toast.LENGTH_SHORT, true).show()
    }
}