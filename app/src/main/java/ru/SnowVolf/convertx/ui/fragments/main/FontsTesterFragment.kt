package ru.SnowVolf.convertx.ui.fragments.main

import ru.SnowVolf.convertx.ui.fragments.base.BaseFragment
import android.widget.TextView
import android.widget.RelativeLayout
import android.widget.EditText
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatImageButton
import android.os.Bundle
import ru.SnowVolf.convertx.R
import android.annotation.SuppressLint
import android.text.TextWatcher
import android.text.Editable
import android.widget.SeekBar.OnSeekBarChangeListener
import com.google.android.material.textfield.TextInputEditText
import android.content.DialogInterface
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.menu.MenuBuilder
import ru.SnowVolf.convertx.settings.Preferences

/**
 * Created by Snow Volf on 04.07.2017, 18:15
 */
class FontsTesterFragment : BaseFragment() {
    private var mFont: TextView? = null
    private var mFontBold: TextView? = null
    private var mFontItalic: TextView? = null
    private var mFontBoldItalic: TextView? = null
    private var mFontCasual: TextView? = null
    private var mFontMonospace: TextView? = null
    private var mFontSerif: TextView? = null
    private var mFontSansSerif: TextView? = null
    private var mFontSansSerifCond: TextView? = null
    private var mBackgroundLayout: RelativeLayout? = null
    private var mEditLayout: RelativeLayout? = null
    private var mFontColorLayout: RelativeLayout? = null
    private var mFontBackground: RelativeLayout? = null
    private var mFontSizeEdit: EditText? = null
    private var mFontSeekBar: SeekBar? = null
    private var mFontPlus: AppCompatImageButton? = null
    private var mFontMinus: AppCompatImageButton? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_rgb_simple, container, false)
        /* TextViews */mFont = rootView.findViewById<View>(R.id.textView) as TextView
        mFontBold = rootView.findViewById<View>(R.id.textViewBold) as TextView
        mFontItalic = rootView.findViewById<View>(R.id.textViewItalic) as TextView
        mFontBoldItalic = rootView.findViewById<View>(R.id.textViewBoldItalic) as TextView
        mFontCasual = rootView.findViewById<View>(R.id.textViewCasual) as TextView
        mFontMonospace = rootView.findViewById<View>(R.id.textViewMono) as TextView
        mFontSerif = rootView.findViewById<View>(R.id.textViewSerif) as TextView
        mFontSansSerif = rootView.findViewById<View>(R.id.textViewSansSerif) as TextView
        mFontSansSerifCond = rootView.findViewById<View>(R.id.textViewSansSerifCond) as TextView
        /* Layouts */mBackgroundLayout = rootView.findViewById<View>(R.id.textBackground) as RelativeLayout
        mEditLayout = rootView.findViewById<View>(R.id.row_0) as RelativeLayout
        mFontColorLayout = rootView.findViewById<View>(R.id.row_2) as RelativeLayout
        mFontBackground = rootView.findViewById<View>(R.id.row_3) as RelativeLayout
        /* Widgets */mFontSizeEdit = rootView.findViewById<View>(R.id.textEdit) as EditText
        mFontSeekBar = rootView.findViewById<View>(R.id.textSeekBar) as SeekBar
        mFontPlus = rootView.findViewById<View>(R.id.textButtonPlus) as AppCompatImageButton
        mFontMinus = rootView.findViewById<View>(R.id.textButtonMinus) as AppCompatImageButton
        return rootView
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedState: Bundle?) {
        super.onActivityCreated(savedState)
        TITLE = R.string.dr_fonts_tester
        //mFontBackground.setOnClickListener(v -> showColorBackgroundDialog());
        mEditLayout!!.setOnClickListener { v: View? -> showFontTextDialog() }
        mBackgroundLayout!!.setBackgroundColor(Preferences.colorBackground)
        //mFontColorLayout.setOnClickListener(v -> showFontColorDialog());
        setAllText(Preferences.textString!!)
        setAllTextColor(Preferences.colorText)
        mFontSeekBar!!.progress = Preferences.colorSize
        setAllTextSize(mFontSeekBar!!.progress + 1)
        mFontSizeEdit!!.setText(Integer.toString(mFontSeekBar!!.progress + 1))
        mFontMinus!!.setOnClickListener { view: View? ->
            if (mFontSeekBar!!.progress > 0) {
                val i = mFontSeekBar!!.progress - 1
                mFontSeekBar!!.progress = i
                setAllTextSize(i + 1)
                mFontSizeEdit!!.setText(Integer.toString(i + 1))
                Preferences.colorSize = mFontSeekBar!!.progress
            }
        }
        mFontPlus!!.setOnClickListener { view: View? ->
            if (mFontSeekBar!!.progress > 0) {
                val i = mFontSeekBar!!.progress + 1
                mFontSeekBar!!.progress = i
                setAllTextSize(i + 1)
                mFontSizeEdit!!.setText(Integer.toString(i + 1))
                Preferences.colorSize = mFontSeekBar!!.progress
            }
        }
        mFontSizeEdit!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString() != "") {
                    mFontSeekBar!!.progress = Integer.valueOf(s.toString()) - 1
                    mFontSizeEdit!!.setSelection(s.length)
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        mFontSeekBar!!.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                setAllTextSize(i + 1)
                mFontSizeEdit!!.setText(Integer.toString(i + 1))
                Preferences.colorSize = seekBar.progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                mFontSizeEdit!!.selectAll()
                Preferences.colorSize = seekBar.progress
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        menu.add("").setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
    }

    private fun setAllText(text: String) {
        mFont!!.text = text
        mFontBold!!.text = text
        mFontItalic!!.text = text
        mFontBoldItalic!!.text = text
        mFontCasual!!.text = text
        mFontMonospace!!.text = text
        mFontSerif!!.text = text
        mFontSansSerif!!.text = text
        mFontSansSerifCond!!.text = text
    }

    private fun setAllTextColor(color: Int) {
        mFont!!.setTextColor(color)
        mFontBold!!.setTextColor(color)
        mFontItalic!!.setTextColor(color)
        mFontBoldItalic!!.setTextColor(color)
        mFontCasual!!.setTextColor(color)
        mFontMonospace!!.setTextColor(color)
        mFontSerif!!.setTextColor(color)
        mFontSansSerif!!.setTextColor(color)
        mFontSansSerifCond!!.setTextColor(color)
    }

    private fun setAllTextSize(size: Int) {
        mFont!!.textSize = size.toFloat()
        mFontBold!!.textSize = size.toFloat()
        mFontItalic!!.textSize = size.toFloat()
        mFontBoldItalic!!.textSize = size.toFloat()
        mFontCasual!!.textSize = size.toFloat()
        mFontMonospace!!.textSize = size.toFloat()
        mFontSerif!!.textSize = size.toFloat()
        mFontSansSerif!!.textSize = size.toFloat()
        mFontSansSerifCond!!.textSize = size.toFloat()
    }

    private fun showFontTextDialog() {
        val string = Preferences.textString
        @SuppressLint("InflateParams") val v = activity!!.layoutInflater.inflate(R.layout.dialog_enter_text, null)
        val editText = v.findViewById<View>(R.id.enterText) as TextInputEditText
        editText.setText(string)
        AlertDialog.Builder(context!!)
            .setView(v)
            .setPositiveButton(R.string.ok) { d: DialogInterface?, i: Int ->
                Preferences.textString = editText.text.toString()
                setAllText(editText.text.toString())
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    } //    private void showFontColorDialog(){
    //        ColorPickerView pickerView = new ColorPickerView(getActivity());
    //        pickerView.setColor(Preferences.getColorText());
    //        pickerView.showAlpha(false);
    //        AlertDialog dialog = new AlertDialog.Builder(getContext())
    //                .setTitle(R.string.font_color)
    //                .setView(pickerView)
    //                .setPositiveButton(R.string.ok, (d, i) -> {
    //                    Preferences.setColorText(pickerView.getColor());
    //                    setAllTextColor(pickerView.getColor());
    //                })
    //                .setNegativeButton(R.string.cancel, null)
    //                .setNeutralButton(R.string.paste, null)
    //                .show();
    //        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(v1 -> {
    //            String color = StringUtils.INSTANCE.readFromClipboard();
    //            if (color != null && color.matches("#([0-9A-f]{6})")){
    //                pickerView.setColor(Color.parseColor(color));
    //            } else if (color != null && color.matches("#([0-9A-f]{8})")){
    //                pickerView.setColor(Color.parseColor(color));
    //            } else if (color != null && !color.matches("#([0-9A-f]{6})") && !color.matches("#([0-9A-f]{8})")){
    //                Toasty.error(getActivity(), getString(R.string.color_dosent_match_regexp), Toast.LENGTH_SHORT, true).show();
    //            } else {
    //                Toasty.error(getContext(), "NULL", Toast.LENGTH_LONG, true).show();
    //            }
    //        });
    //    }
    //    private void showColorBackgroundDialog(){
    //        ColorPickerView pickerView = new ColorPickerView(getActivity());
    //        pickerView.setColor(Preferences.getColorBackground());
    //        pickerView.showAlpha(false);
    //        AlertDialog dialog = new AlertDialog.Builder(getContext())
    //                .setTitle(R.string.backround_color)
    //                .setView(pickerView)
    //                .setPositiveButton(R.string.ok, (d, i) -> {
    //                    Preferences.setColorBackground(pickerView.getColor());
    //                    mBackgroundLayout.setBackgroundColor(pickerView.getColor());
    //                })
    //                .setNegativeButton(R.string.cancel, null)
    //                .setNeutralButton(R.string.paste, null)
    //                .show();
    //                dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(v1 -> {
    //                    String color = StringUtils.INSTANCE.readFromClipboard();
    //                    if (color != null && color.matches("#([0-9A-f]{6})")){
    //                        pickerView.setColor(Color.parseColor(color));
    //                    } else if (color != null && color.matches("#([0-9A-f]{8})")){
    //                        pickerView.setColor(Color.parseColor(color));
    //                    } else if (color != null && !color.matches("#([0-9A-f]{6})") && !color.matches("#([0-9A-f]{8})")){
    //                        Toasty.error(getActivity(), getString(R.string.color_dosent_match_regexp), Toast.LENGTH_SHORT, true).show();
    //                    } else {
    //                        Toasty.error(getContext(), "NULL", Toast.LENGTH_LONG, true).show();
    //                    }
    //        });
    //    }
}