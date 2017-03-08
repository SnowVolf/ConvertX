package ru.SnowVolf.convertx.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuBuilder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import ru.SnowVolf.convertx.R;
import ru.SnowVolf.convertx.ui.Toasty;

import static ru.SnowVolf.convertx.utils.StringUtils.copyToClipboard;

/**
 * Created by Snow Volf on 28.01.2017.
 */

public class Base64Fragment extends Fragment {

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
        /*spinner = (Spinner) rootView.findViewById(R.id.decodeMode);
        spinner.setVisibility(View.INVISIBLE);
        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.baseAlgVal, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);*/

        input = (TextInputEditText) rootView.findViewById(R.id.inputText);
        input.setHint(R.string.hint_utf);
        output = (TextInputEditText) rootView.findViewById(R.id.outputText);
        output.setHint(R.string.hint_base64);
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
        Typeface Mono = Typeface.createFromAsset(getActivity().getAssets(), "fonts/RobotoMono-Regular.ttf");
        output.setTypeface(Mono);
        output.setTypeface(Mono);
        /*spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View is, int pos, long id){*/
                input.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        String baseContent = input.getText().toString();
                        String codeGirl;
                        byte[] EBytes = Base64.encode(baseContent.getBytes(), Base64.NO_WRAP);
                        codeGirl = new String(EBytes);
                        output.setText(codeGirl);
                        output.setSelection(output.getText().length());//ставим курсор в конец строки
                    }
                });/*;
            }
            public void onNothingSelected(AdapterView<?> parent){}
        });*/
        copyInp.setOnClickListener(this::copyInput);
        clearInp.setOnClickListener(view -> clearInput());
        copyOutp.setOnClickListener(this::copyOutput);
        clearOutp.setOnClickListener(view -> clearOutput());


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
                    ConvertBase64();
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
    public void ConvertBase64(){
        /**
         * Закомментироал код обработки, потому как он есть в TextWatcher-e
         * Удалять пока не буду, вдруг пригодится
         */
        /*if (!input.getText().toString().isEmpty() && output.getText().toString().isEmpty()) {
            String baseContent = input.getText().toString();
            String codeGirl;
            byte[] EBytes = Base64.encode(baseContent.getBytes(), Base64.NO_WRAP);
            codeGirl = new String(EBytes);
            output.setText(codeGirl);
            output.setSelection(output.getText().length());//ставим курсор в конец строки
        } else*/ if (input.getText().toString().isEmpty() && !output.getText().toString().isEmpty()) {
            String baseSoup = output.getText().toString();
            String decodeGirl;
            byte[] DBytes = Base64.decode(baseSoup.getBytes(), Base64.NO_WRAP);
            decodeGirl = new String(DBytes);
            input.setText(decodeGirl);
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
        input.setText("");
    }
    public void clearOutput(){
        output.setText("");

    }
    public void clearAllText(){
        output.setText("");
        input.setText("");
        Toasty.success(getContext(), getString(R.string.cleared), Toast.LENGTH_SHORT, true).show();
    }

}
