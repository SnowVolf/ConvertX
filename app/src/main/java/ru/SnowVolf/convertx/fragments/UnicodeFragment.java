package ru.SnowVolf.convertx.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
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
import ru.SnowVolf.convertx.algorhitms.DecoderAlgs;
import ru.SnowVolf.convertx.other.Extras;
import ru.SnowVolf.convertx.ui.Toasty;

import static ru.SnowVolf.convertx.other.Extras.showToast;
import static ru.SnowVolf.convertx.utils.StringUtils.copyToClipboard;

/**
 * Created by Snow Volf on 28.01.2017.
 */

public class UnicodeFragment extends Fragment {
    public TextInputEditText input;
    public TextInputEditText output;
    private Button copyInp;
    private Button copyOutp;
    private Button clearInp;
    private Button clearOutp;
    //private Spinner spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        //spinner = (Spinner) rootView.findViewById(R.id.decodeMode);
        //spinner.setVisibility(View.INVISIBLE);
        input = (TextInputEditText) rootView.findViewById(R.id.inputText);
        input.setHint(R.string.hint_utf);
        output = (TextInputEditText) rootView.findViewById(R.id.outputText);
        output.setHint(R.string.hint_unicode);
        copyInp = (Button) rootView.findViewById(R.id.copy_btn2);
        clearInp = (Button) rootView.findViewById(R.id.clear_btn2);
        clearInp.setVisibility(View.INVISIBLE);
        copyOutp = (Button) rootView.findViewById(R.id.copy_btn);
        clearOutp = (Button) rootView.findViewById(R.id.clear_btn);
        return rootView;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        copyInp.setOnClickListener(this::copyInput);
        clearInp.setOnClickListener(view -> clearInput());
        copyOutp.setOnClickListener(this::copyOutput);
        clearOutp.setOnClickListener(view -> clearOutput());
        Typeface Mono = Typeface.createFromAsset(getActivity().getAssets(), "fonts/RobotoMono-Regular.ttf");
        output.setTypeface(Mono);
        output.setTypeface(Mono);

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String utfSource = input.getText().toString();
                StringBuilder unicodeValue = new StringBuilder();
                for (int i = 0; i < utfSource.length(); i++)
                    unicodeValue.append("\\u").append(Integer.toHexString(utfSource.charAt(i) | 0x10000).substring(1));
                output.setText(unicodeValue);
                //фикс неправильной установки курсора
                output.setSelection(output.getText().length());
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
                    ConvertUnicode();
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
    public void ConvertUnicode() {
        /**
         * Закомментироал код обработки, потому как он есть в TextWatcher-e
         * Удалять пока не буду, вдруг пригодится
         */
        if (!output.getText().toString().isEmpty() && input.getText().toString().isEmpty()) {
            String unicodeContent = output.getText().toString();

            //проверяем валидность String, и переносим его в нужное поле при необходимости
            if (!unicodeContent.contains("\\u")){
                input.setText(unicodeContent);
                Extras.showToast(getContext(), getString(R.string.text_moved_correct));
            }
            String text = DecoderAlgs.unescapeJava(unicodeContent);
            input.setText(text);
            input.setSelection(input.getText().length());
        } /*else if (output.getText().toString().isEmpty() && !input.getText().toString().isEmpty()) {
            String utfSource = input.getText().toString();
            StringBuilder unicodeValue = new StringBuilder();
            for (int i = 0; i < utfSource.length(); i++)
                unicodeValue.append("\\u").append(Integer.toHexString(utfSource.charAt(i) | 0x10000).substring(1));
            output.setText(unicodeValue);
            //output.setSelection(output.getText().length());
        }*/
    }
    public void copyInput(View v){
        copyToClipboard(getContext(), input.getText().toString());
        Toasty.info(getContext(), getString(R.string.copied2clipboard), Toast.LENGTH_SHORT, true).show();
    }
    public void copyOutput(View v){
        copyToClipboard(getContext(), output.getText().toString());
        Toasty.info(getContext(), getString(R.string.copied2clipboard), Toast.LENGTH_SHORT, true).show();
    }

    public void clearInput(){
        input.setText("");
    }
    public void clearOutput(){
        output.setText("");
    }
    public void clearAllText(){
        output.setText("");
        input.setText("");
        Toasty.info(getContext(), getString(R.string.cleared), Toast.LENGTH_SHORT, true).show();
    }

}
