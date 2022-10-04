package ru.SnowVolf.convertx.ui.fragments.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.AppCompatImageButton;

import com.google.android.material.textfield.TextInputEditText;

import ru.SnowVolf.convertx.R;
import ru.SnowVolf.convertx.settings.Preferences;
import ru.SnowVolf.convertx.ui.fragments.base.BaseFragment;

/**
 * Created by Snow Volf on 04.07.2017, 18:15
 */

public class FontsTesterFragment extends BaseFragment {
    private TextView mFont, mFontBold, mFontItalic, mFontBoldItalic, mFontCasual, mFontMonospace,
            mFontSerif, mFontSansSerif, mFontSansSerifCond;
    private RelativeLayout mBackgroundLayout, mEditLayout, mFontColorLayout, mFontBackground;
    private EditText mFontSizeEdit;
    private SeekBar mFontSeekBar;
    private AppCompatImageButton mFontPlus, mFontMinus;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.fragment_rgb_simple, container, false);
        /* TextViews */
        mFont = (TextView) rootView.findViewById(R.id.textView);
        mFontBold = (TextView) rootView.findViewById(R.id.textViewBold);
        mFontItalic = (TextView) rootView.findViewById(R.id.textViewItalic);
        mFontBoldItalic = (TextView) rootView.findViewById(R.id.textViewBoldItalic);
        mFontCasual = (TextView) rootView.findViewById(R.id.textViewCasual);
        mFontMonospace = (TextView) rootView.findViewById(R.id.textViewMono);
        mFontSerif = (TextView) rootView.findViewById(R.id.textViewSerif);
        mFontSansSerif = (TextView) rootView.findViewById(R.id.textViewSansSerif);
        mFontSansSerifCond = (TextView) rootView.findViewById(R.id.textViewSansSerifCond);
        /* Layouts */
        mBackgroundLayout = (RelativeLayout) rootView.findViewById(R.id.textBackground);
        mEditLayout = (RelativeLayout) rootView.findViewById(R.id.row_0);
        mFontColorLayout = (RelativeLayout) rootView.findViewById(R.id.row_2);
        mFontBackground = (RelativeLayout) rootView.findViewById(R.id.row_3);
        /* Widgets */
        mFontSizeEdit = (EditText) rootView.findViewById(R.id.textEdit);
        mFontSeekBar = (SeekBar) rootView.findViewById(R.id.textSeekBar);
        mFontPlus = (AppCompatImageButton) rootView.findViewById(R.id.textButtonPlus);
        mFontMinus = (AppCompatImageButton) rootView.findViewById(R.id.textButtonMinus);
        return rootView;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);
        TITLE = R.string.dr_fonts_tester;
        //mFontBackground.setOnClickListener(v -> showColorBackgroundDialog());
        mEditLayout.setOnClickListener(v -> showFontTextDialog());
        mBackgroundLayout.setBackgroundColor(Preferences.getColorBackground());
        //mFontColorLayout.setOnClickListener(v -> showFontColorDialog());
        setAllText(Preferences.getTextString());
        setAllTextColor(Preferences.getColorText());

        mFontSeekBar.setProgress(Preferences.getColorSize());
        setAllTextSize(mFontSeekBar.getProgress() + 1);
        mFontSizeEdit.setText(Integer.toString(mFontSeekBar.getProgress() + 1));
        mFontMinus.setOnClickListener(view -> {
            if (mFontSeekBar.getProgress() > 0) {
                int i = mFontSeekBar.getProgress() - 1;
                mFontSeekBar.setProgress(i);
                setAllTextSize(i + 1);
                mFontSizeEdit.setText(Integer.toString(i + 1));
                Preferences.setColorSize(mFontSeekBar.getProgress());
            }
        });
        mFontPlus.setOnClickListener(view -> {
            if (mFontSeekBar.getProgress() > 0) {
                int i = mFontSeekBar.getProgress() + 1;
                mFontSeekBar.setProgress(i);
                setAllTextSize(i + 1);
                mFontSizeEdit.setText(Integer.toString(i + 1));
                Preferences.setColorSize(mFontSeekBar.getProgress());
            }
        });

        mFontSizeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {
                    mFontSeekBar.setProgress(Integer.valueOf(s.toString()) - 1);
                    mFontSizeEdit.setSelection(s.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mFontSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setAllTextSize(i + 1);
                mFontSizeEdit.setText(Integer.toString(i + 1));
                Preferences.setColorSize(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mFontSizeEdit.selectAll();
                Preferences.setColorSize(seekBar.getProgress());
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (menu != null) {
            menu.clear();
        } else
            menu = new MenuBuilder(getContext());
        menu.add("").setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    private void setAllText(String text){
        mFont.setText(text);
        mFontBold.setText(text);
        mFontItalic.setText(text);
        mFontBoldItalic.setText(text);
        mFontCasual.setText(text);
        mFontMonospace.setText(text);
        mFontSerif.setText(text);
        mFontSansSerif.setText(text);
        mFontSansSerifCond.setText(text);
    }

    private void setAllTextColor(int color){
        mFont.setTextColor(color);
        mFontBold.setTextColor(color);
        mFontItalic.setTextColor(color);
        mFontBoldItalic.setTextColor(color);
        mFontCasual.setTextColor(color);
        mFontMonospace.setTextColor(color);
        mFontSerif.setTextColor(color);
        mFontSansSerif.setTextColor(color);
        mFontSansSerifCond.setTextColor(color);
    }

    private void setAllTextSize(int size){
        mFont.setTextSize(size);
        mFontBold.setTextSize(size);
        mFontItalic.setTextSize(size);
        mFontBoldItalic.setTextSize(size);
        mFontCasual.setTextSize(size);
        mFontMonospace.setTextSize(size);
        mFontSerif.setTextSize(size);
        mFontSansSerif.setTextSize(size);
        mFontSansSerifCond.setTextSize(size);
    }

    private void showFontTextDialog(){
        String string = Preferences.getTextString();
        @SuppressLint("InflateParams") View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_enter_text, null);
        TextInputEditText editText = (TextInputEditText) v.findViewById(R.id.enterText);
        editText.setText(string);
        new AlertDialog.Builder(getContext())
                .setView(v)
                .setPositiveButton(R.string.ok, (d, i) -> {
                    Preferences.setTextString(editText.getText().toString());
                    setAllText(editText.getText().toString());
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

//    private void showFontColorDialog(){
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
