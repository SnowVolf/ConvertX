package ru.SnowVolf.convertx.ui.fragments.base;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.view.menu.MenuBuilder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import ru.SnowVolf.convertx.R;
import ru.SnowVolf.convertx.settings.Preferences;
import ru.SnowVolf.convertx.ui.Toasty;
import ru.SnowVolf.convertx.utils.StringUtils;

/**
 * Created by Snow Volf on 20.06.2017, 11:35
 */

public class BaseMainFragment extends BaseFragment {
    public TextInputEditText mInputField, mOutputField;
    public Button mCopyInput, mCopyOutput, mClearOutput, mPasteInput, mPasteOutput;
    public BaseMainFragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mInputField.setHint(R.string.hint_utf);
        mOutputField.setHint(R.string.hint_unicode);
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
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        mCopyInput.setOnClickListener(this::copyInput);
        mCopyOutput.setOnClickListener(this::copyOutput);
        mClearOutput.setOnClickListener(view -> clearOutput());
        mPasteInput.setOnClickListener(view -> pasteInput());
        mPasteOutput.setOnClickListener(view -> pasteOutput());
        mOutputField.setTypeface(Mono);
        mOutputField.setTypeface(Mono);
        mOutputField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")){
                    mPasteOutput.setVisibility(View.VISIBLE);
                    mClearOutput.setVisibility(View.INVISIBLE);
                } else {
                    mPasteOutput.setVisibility(View.INVISIBLE);
                    mClearOutput.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        if (mInputField != null && mOutputField != null){
            try {
                mInputField.setTextSize(Preferences.getFontSize());
                mOutputField.setTextSize(Preferences.getFontSize());
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
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
                    Convert();
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

    public void Convert() {
    }

    public void copyInput(View v){
        StringUtils.INSTANCE.copyToClipboard(getContext(), mInputField.getText().toString());
        Toasty.info(getContext(), getString(R.string.copied2clipboard), Toast.LENGTH_SHORT, true).show();
    }
    public void copyOutput(View v){
        StringUtils.INSTANCE.copyToClipboard(getContext(), mOutputField.getText().toString());
        Toasty.info(getContext(), getString(R.string.copied2clipboard), Toast.LENGTH_SHORT, true).show();
    }

    public void clearOutput(){
        mOutputField.setText("");
    }

    public void pasteInput(){
        try{ mInputField.setText(StringUtils.INSTANCE.readFromClipboard()); } catch (NullPointerException ignored){}
    }

    public void pasteOutput(){
        try{ mOutputField.setText(StringUtils.INSTANCE.readFromClipboard()); } catch (NullPointerException ignored){}
    }

    public void clearAllText(){
        mOutputField.setText("");
        mInputField.setText("");
        Toasty.success(getContext(), getString(R.string.cleared), Toast.LENGTH_SHORT, true).show();
    }
}
