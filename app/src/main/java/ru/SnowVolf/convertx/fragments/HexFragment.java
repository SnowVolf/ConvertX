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

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import ru.SnowVolf.convertx.R;
import ru.SnowVolf.convertx.ui.Toasty;

import static ru.SnowVolf.convertx.utils.StringUtils.copyToClipboard;

/**
 * Created by Snow Volf on 28.01.2017.
 */

public class HexFragment extends Fragment {
    public TextInputEditText input;
    public TextInputEditText output;
    private Button copyInp;
    private Button copyOutp;
    private Button clearInp;
    private Button clearOutp;
    //private Spinner spinner;

    public static byte[] hexDecode(String text){
        int l = text.length();
        byte[] data = new byte[l/2];
        for (int i = 0; i < l; i+=2){
            data[i/2] = (byte) ((Character.digit(text.charAt(i), 16) << 4) + Character.digit(text.charAt(i + 1), 16));
        }
        return data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
       //spinner = (Spinner) rootView.findViewById(R.id.decodeMode);
        //spinner.setVisibility(View.INVISIBLE);
        input = (TextInputEditText) rootView.findViewById(R.id.inputText);
        input.setHint(R.string.hint_utf);
        output = (TextInputEditText) rootView.findViewById(R.id.outputText);
        output.setHint(R.string.hint_hex);
        copyInp = (Button) rootView.findViewById(R.id.copy_btn2);
        clearInp = (Button) rootView.findViewById(R.id.clear_btn2);
        clearInp.setVisibility(View.INVISIBLE);
        copyOutp = (Button) rootView.findViewById(R.id.copy_btn);
        clearOutp = (Button) rootView.findViewById(R.id.clear_btn);
        return rootView;
    }
    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
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
                String plainText = input.getText().toString();
                String hexGirl = String.format("%040X", new BigInteger(1, plainText.getBytes()));
                output.setText(String.format("0x%s", hexGirl));
                output.setSelection(output.getText().length());//ставим курсор в конец строки*/
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
                    try {
                        ConvertHex();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
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

    public void ConvertHex() throws UnsupportedEncodingException {
        /**
         * Закомментироал код обработки, потому как он есть в TextWatcher-e
         * Удалять пока не буду, вдруг пригодится
         */
        /*if (!input.getText().toString().isEmpty() && output.getText().toString().isEmpty()) {
            String plainText = input.getText().toString();
            String hexGirl = String.format("%040X", new BigInteger(1, plainText.getBytes()));
            output.setText(String.format("0x%activity_changelist", hexGirl));
            output.setSelection(output.getText().length());//ставим курсор в конец строки
        } else */if (input.getText().toString().isEmpty() && !output.getText().toString().isEmpty()) {
            String text = output.getText().toString();
            text = text.replaceAll("\\W", "").toLowerCase();
            //убираем 0x в начале, если он есть
            if (text.startsWith("0x")){
                text = text.replaceFirst("0x", "");
            }
            byte[] bytes = hexDecode(text);
            String result = new String(bytes, "UTF-8");
            input.setText(result);
            input.setSelection(input.getText().length());
        }
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
        clearAllText();//TW
    }
    public void clearOutput(){
        output.setText("");

    }
    public void clearAllText(){
        input.setText("");
        output.setText("");
        Toasty.success(getContext(), getString(R.string.cleared), Toast.LENGTH_SHORT, true).show();
    }
}
