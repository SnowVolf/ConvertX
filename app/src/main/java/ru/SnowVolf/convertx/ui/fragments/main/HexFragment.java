package ru.SnowVolf.convertx.ui.fragments.main;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.view.menu.MenuBuilder;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import org.acra.ACRA;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import ru.SnowVolf.convertx.R;
import ru.SnowVolf.convertx.algorhitms.DecoderAlgs;
import ru.SnowVolf.convertx.settings.Preferences;
import ru.SnowVolf.convertx.ui.Toasty;
import ru.SnowVolf.convertx.ui.fragments.base.BaseMainFragment;

/**
 * Created by Snow Volf on 28.01.2017.
 */

public class HexFragment extends BaseMainFragment {
    private Spinner AlgSwitcher;
    private boolean isInt;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        AlgSwitcher = (Spinner) rootView.findViewById(R.id.decodeMode);
        mInputField = (TextInputEditText) rootView.findViewById(R.id.inputText);
        mOutputField = (TextInputEditText) rootView.findViewById(R.id.outputText);
        mCopyInput = (Button) rootView.findViewById(R.id.copy_btn2);
        mCopyOutput = (Button) rootView.findViewById(R.id.copy_btn);
        mClearOutput = (Button) rootView.findViewById(R.id.clear_btn);
        mPasteInput = (Button) rootView.findViewById(R.id.paste_btn);
        mPasteOutput = (Button) rootView.findViewById(R.id.paste_btn2);
        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        mOutputField.setTypeface(Mono);
        mInputField.setTypeface(Mono);
        mInputField.setHint("String");
        mOutputField.setHint(R.string.hint_hex);
        AlgSwitcher.setVisibility(View.VISIBLE);
        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.hexMode, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        AlgSwitcher.setAdapter(adapter);
        TITLE = R.string.dr_hex;
        AlgSwitcher.setSelection(Preferences.getSpinnerPosition("spinner.hex"));
        AlgSwitcher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View is, int position, long id) {
                switch (position){
                    case 0:
                        Preferences.setSpinnerPosition("spinner.hex", position);
                        addListener(0);
                        break;
                    case 1:
                        addListener(1);
                        Preferences.setSpinnerPosition("spinner.hex", position);
                        break;
                }
            }
            public void onNothingSelected(AdapterView<?> parent){
                addListener(0);
            }
        });
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (menu != null) {
            menu.clear();
        } else
            menu = new MenuBuilder(getContext());
        //добавляем пункты меню
        menu.add(R.string.convert)
                .setIcon(R.drawable.ic_menu_convert)
                .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                .setOnMenuItemClickListener(menuItem -> {
                    if (!isInt)
                    try {
                        ConvertHex();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (isInt){
                        ConvertFromHex();
                    }
                    return true;
                });
        menu.add(R.string.clear_all)
                .setIcon(R.drawable.ic_menu_clear_all)
                .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                .setOnMenuItemClickListener(menuItem -> {
                    clearAllText();
                    return true;
                });
    }

    private void addListener(int mode){
        if (mode == 0){
            isInt = false;
            mInputField.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            mInputField.setHint("String");
            mInputField.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String plainText = mInputField.getText().toString();

                    String hexGirl = String.format("%040X", new BigInteger(1, plainText.getBytes()));
                    mOutputField.setText(String.format("0x%s", hexGirl));
                    mOutputField.setSelection(mOutputField.getText().length());//ставим курсор в конец строки*/
                }
            });
    }
        if (mode == 1){
            isInt = true;
            mInputField.setHint("Integer");
            mOutputField.setKeyListener(DigitsKeyListener.getInstance("0123456789ABCDEFabcdefx"));
            mInputField.setInputType(InputType.TYPE_CLASS_PHONE);
            mInputField.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    try {
                        int value = Integer.parseInt(mInputField.getText().toString());
                        String integer = DecoderAlgs.ConvertInt2Hex(value);
                        mOutputField.setText(integer);

                    } catch (NumberFormatException ignore){}
                    mOutputField.setSelection(mOutputField.getText().length());//ставим курсор в конец строки*/
                }
            });
        }
    }

    private void ConvertHex() throws UnsupportedEncodingException {
        /**
         * Закомментироал код обработки, потому как он есть в TextWatcher-e
         * Удалять пока не буду, вдруг пригодится
         */
        if (mInputField.getText().toString().isEmpty() && !mOutputField.getText().toString().isEmpty()) {
            try {
            String text = mOutputField.getText().toString();
            text = text.replaceAll("\\W", "").toLowerCase();
            //убираем 0x в начале, если он есть
            if (text.startsWith("0x")){
                text = text.replaceFirst("0x", "");
            }
                byte[] bytes = DecoderAlgs.hexDecode(text);
                String result = new String(bytes, "UTF-8");
                mInputField.setText(result);

            mInputField.setSelection(mInputField.getText().length());
            } catch (Exception e){
                Toasty.error(getContext(), getString(R.string.hex_error), Toast.LENGTH_LONG, true).show();
            }
        }
    }

    private void ConvertFromHex(){
        if (mInputField.getText().toString().isEmpty() && !mOutputField.getText().toString().isEmpty()) {
            String text = mOutputField.getText().toString();
            //убираем 0x в начале, если он есть
            if (text.startsWith("0x")){
                text = text.replaceFirst("0x", "");
            }
            try {
                int dec = DecoderAlgs.ConvertHex2Int(text);
                String d = Integer.toString(dec);
                try {
                    mInputField.setText(d);
                } catch (Exception e) {
                    ACRA.getErrorReporter().handleException(e);
                }
            } catch (NumberFormatException nfe){
                Toasty.error(getContext(), getString(R.string.incorrect_hex_int), Toast.LENGTH_LONG, true).show();
            }
        }
    }

    public void clearAllText(){
        mInputField.setText("");
        mOutputField.setText("");
        Toasty.success(getContext(), getString(R.string.cleared), Toast.LENGTH_SHORT, true).show();
    }
}
