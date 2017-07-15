package ru.SnowVolf.convertx.ui.fragments.main;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import ru.SnowVolf.convertx.R;
import ru.SnowVolf.convertx.settings.Preferences;
import ru.SnowVolf.convertx.ui.Toasty;
import ru.SnowVolf.convertx.ui.fragments.base.BaseMainFragment;

/**
 * Created by Snow Volf on 28.01.2017.
 */

public class Base64Fragment extends BaseMainFragment {
    public Spinner AlgSwitcher;
    public int algorhitm;


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
        mInputField.setHint(R.string.hint_utf);
        mOutputField.setHint(R.string.hint_base64);
        AlgSwitcher.setVisibility(View.VISIBLE);
        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.baseAlgVal, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        AlgSwitcher.setAdapter(adapter);
        TITLE = R.string.dr_base64;
        int[] Algs = {Base64.NO_WRAP, Base64.NO_CLOSE, Base64.NO_PADDING, Base64.URL_SAFE, Base64.CRLF, Base64.DEFAULT};
        AlgSwitcher.setSelection(Preferences.getSpinnerPosition("spinner.base64"));
        AlgSwitcher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View is, int pos, long id) {
                for (int i = 0; i < AlgSwitcher.getCount(); i++) {
                    if (pos == i){
                        algorhitm = Algs[i];
                    }
                }
                int spinnerPosition1 = AlgSwitcher.getSelectedItemPosition();
                Preferences.setSpinnerPosition("spinner.base64", spinnerPosition1);
            }
            public void onNothingSelected(AdapterView<?> parent){
                algorhitm = Base64.NO_WRAP;
            }
        });
                mInputField.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        String baseContent = mInputField.getText().toString();
                        String codeGirl;
                        byte[] EBytes = Base64.encode(baseContent.getBytes(), algorhitm);
                        codeGirl = new String(EBytes);
                        mOutputField.setText(codeGirl);
                        mOutputField.setSelection(mOutputField.getText().length());//ставим курсор в конец строки
                    }
                });
    }

    public void Convert(){
        if (mInputField.getText().toString().isEmpty() && !mOutputField.getText().toString().isEmpty()) {
            String baseSoup = mOutputField.getText().toString();
            try {
                String decodeGirl;
                byte[] DBytes = Base64.decode(baseSoup.getBytes(), algorhitm);
                decodeGirl = new String(DBytes);
                mInputField.setText(decodeGirl);
            } catch (Exception ex){
                Toasty.error(getContext(), getString(R.string.base64_error), Toast.LENGTH_SHORT, true).show();
            }
            mInputField.setSelection(mInputField.getText().length());
        }
    }

    public void clearAllText(){
        mOutputField.setText("");
        mInputField.setText("");
        Toasty.success(getContext(), getString(R.string.cleared), Toast.LENGTH_SHORT, true).show();
    }

}
