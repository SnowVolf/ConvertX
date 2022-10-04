package ru.SnowVolf.convertx.ui.fragments.base

import ru.SnowVolf.convertx.utils.StringUtils.copyToClipboard
import ru.SnowVolf.convertx.utils.StringUtils.readFromClipboard
import com.google.android.material.textfield.TextInputEditText
import android.os.Bundle
import ru.SnowVolf.convertx.R
import android.text.TextWatcher
import android.text.Editable
import android.view.*
import android.widget.Button
import ru.SnowVolf.convertx.ui.Toasty
import android.widget.Toast
import androidx.appcompat.view.menu.MenuBuilder
import ru.SnowVolf.convertx.settings.Preferences
import java.lang.Exception
import java.lang.NullPointerException

/**
 * Created by Snow Volf on 20.06.2017, 11:35
 */
open class BaseMainFragment : BaseFragment() {
    @JvmField
    var mInputField: TextInputEditText? = null
    @JvmField
    var mOutputField: TextInputEditText? = null
    @JvmField
    var mCopyInput: Button? = null
    @JvmField
    var mCopyOutput: Button? = null
    @JvmField
    var mClearOutput: Button? = null
    @JvmField
    var mPasteInput: Button? = null
    @JvmField
    var mPasteOutput: Button? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_main, container, false)
        mInputField!!.setHint(R.string.hint_utf)
        mOutputField!!.setHint(R.string.hint_unicode)
        mInputField = rootView.findViewById<View>(R.id.inputText) as TextInputEditText
        mOutputField = rootView.findViewById<View>(R.id.outputText) as TextInputEditText
        mCopyInput = rootView.findViewById<View>(R.id.copy_btn2) as Button
        mCopyOutput = rootView.findViewById<View>(R.id.copy_btn) as Button
        mClearOutput = rootView.findViewById<View>(R.id.clear_btn) as Button
        mPasteInput = rootView.findViewById<View>(R.id.paste_btn) as Button
        mPasteOutput = rootView.findViewById<View>(R.id.paste_btn2) as Button
        return rootView
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mCopyInput!!.setOnClickListener { v: View? -> copyInput(v) }
        mCopyOutput!!.setOnClickListener { v: View? -> copyOutput(v) }
        mClearOutput!!.setOnClickListener { view: View? -> clearOutput() }
        mPasteInput!!.setOnClickListener { view: View? -> pasteInput() }
        mPasteOutput!!.setOnClickListener { view: View? -> pasteOutput() }
        mOutputField!!.typeface = Mono
        mOutputField!!.typeface = Mono
        mOutputField!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.toString() == "") {
                    mPasteOutput!!.visibility = View.VISIBLE
                    mClearOutput!!.visibility = View.INVISIBLE
                } else {
                    mPasteOutput!!.visibility = View.INVISIBLE
                    mClearOutput!!.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
    }

    override fun onResume() {
        super.onResume()
        if (mInputField != null && mOutputField != null) {
            try {
                mInputField!!.textSize = Preferences.fontSize.toFloat()
                mOutputField!!.textSize = Preferences.fontSize.toFloat()
            } catch (ex: Exception) {
                ex.printStackTrace()
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
                Convert()
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

    open fun Convert() {}
    fun copyInput(v: View?) {
        copyToClipboard(requireContext(), mInputField!!.text.toString())
        Toasty.info(requireContext(), getString(R.string.copied2clipboard), Toast.LENGTH_SHORT, true).show()
    }

    fun copyOutput(v: View?) {
        copyToClipboard(requireContext(), mOutputField!!.text.toString())
        Toasty.info(requireContext(), getString(R.string.copied2clipboard), Toast.LENGTH_SHORT, true).show()
    }

    fun clearOutput() {
        mOutputField!!.setText("")
    }

    fun pasteInput() {
        try {
            mInputField!!.setText(readFromClipboard())
        } catch (ignored: NullPointerException) {
        }
    }

    fun pasteOutput() {
        try {
            mOutputField!!.setText(readFromClipboard())
        } catch (ignored: NullPointerException) {
        }
    }

    open fun clearAllText() {
        mOutputField!!.setText("")
        mInputField!!.setText("")
        Toasty.success(requireContext(), getString(R.string.cleared), Toast.LENGTH_SHORT, true).show()
    }
}