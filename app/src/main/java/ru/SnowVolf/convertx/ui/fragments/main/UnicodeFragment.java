package ru.SnowVolf.convertx.ui.fragments.main;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import ru.SnowVolf.convertx.R;
import ru.SnowVolf.convertx.algorhitms.DecoderAlgs;
import ru.SnowVolf.convertx.ui.Toasty;
import ru.SnowVolf.convertx.ui.fragments.base.BaseMainFragment;

/**
 * Created by Snow Volf on 28.01.2017.
 */

public class UnicodeFragment extends BaseMainFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mInputField = (TextInputEditText) rootView.findViewById(R.id.inputText);
        mOutputField = (TextInputEditText) rootView.findViewById(R.id.outputText);
        mCopyInput = (Button) rootView.findViewById(R.id.copy_btn2);
        mCopyOutput = (Button) rootView.findViewById(R.id.copy_btn);
        mClearOutput = (Button) rootView.findViewById(R.id.clear_btn);
        mPasteInput = (Button) rootView.findViewById(R.id.paste_btn);
        mPasteOutput = (Button) rootView.findViewById(R.id.paste_btn2);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        mCopyInput.setOnClickListener(this::copyInput);
        mCopyOutput.setOnClickListener(this::copyOutput);
        mClearOutput.setOnClickListener(view -> clearOutput());
        mInputField.setHint(R.string.hint_utf);
        mOutputField.setHint(R.string.hint_unicode);
        mOutputField.setTypeface(Mono);
        mOutputField.setTypeface(Mono);
        TITLE = R.string.dr_unicode;

        mInputField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String utfSource = mInputField.getText().toString();
                StringBuilder unicodeValue = new StringBuilder();
                for (int i = 0; i < utfSource.length(); i++)
                    unicodeValue.append("\\u").append(Integer.toHexString(utfSource.charAt(i) | 0x10000).substring(1));
                mOutputField.setText(unicodeValue);
                //фикс неправильной установки курсора
                mOutputField.setSelection(mOutputField.getText().length());
            }
        });
    }

    @Override
    public void Convert() {
        if (!mOutputField.getText().toString().isEmpty() && mInputField.getText().toString().isEmpty()) {
            String unicodeContent = mOutputField.getText().toString();

            //проверяем валидность String, и переносим его в нужное поле при необходимости
            if (!unicodeContent.matches("(\\\\u[0-9]{4})+")){
                mInputField.setText(unicodeContent);
                Toasty.info(getContext(), getString(R.string.text_moved_correct), Toast.LENGTH_SHORT, true).show();
            }
            String text = DecoderAlgs.unescapeJava(unicodeContent);
            mInputField.setText(text);
            mInputField.setSelection(mInputField.getText().length());
        }
    }

    public void clearAllText(){
        mOutputField.setText("");
        mInputField.setText("");
        Toasty.success(getContext(), getString(R.string.cleared), Toast.LENGTH_SHORT, true).show();
    }

}
