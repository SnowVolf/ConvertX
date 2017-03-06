package ru.SnowVolf.convertx.regexdragon;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuBuilder;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import ru.SnowVolf.convertx.R;
import ru.SnowVolf.convertx.other.Extras;

/**
 * A placeholder fragment containing a simple view.
 */
public class RegexDragonValidator extends Fragment {
    public TextInputEditText regexVal;
    public TextInputEditText sourceSoup;
    private TextView counter;
    private TextView getCounterResult;
    private TextView currentFlag;
    private Flags flags = new Flags();


    public RegexDragonValidator() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_regex_dragon, container, false);
        regexVal = (TextInputEditText) rootView.findViewById(R.id.regex_text);
        sourceSoup = (TextInputEditText) rootView.findViewById(R.id.plain_text);
        counter = (TextView) rootView.findViewById(R.id.regex_count);
        getCounterResult = (TextView) rootView.findViewById(R.id.regex_result);
        currentFlag = (TextView) rootView.findViewById(R.id.regex_flags);

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        currentFlag.setText(flags.getFlagString());

        regexVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               if (!sourceSoup.getText().toString().isEmpty()){
                   RegexEx();
               }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        sourceSoup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                /**
                 * Фикс бага htc 600
                 * Когда при пустом поле regex, все выражения были совпадающими
                 */
                /*if (!regexVal.getText().toString().isEmpty()) {
                    RegexEx();
                }*/
                RegexEx();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (menu != null) {
            menu.clear();
        } else
            menu = new MenuBuilder(getContext());
        menu.add(R.string.clear_all)
                .setIcon(R.drawable.ic_menu_clear_all)
                .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                .setOnMenuItemClickListener(menuItem -> {
                    clearAllText();
                    return true;
                });
        menu.add(R.string.flags)
                .setIcon(R.drawable.ic_menu_regex_flags)
                .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                .setOnMenuItemClickListener(menuItem -> {
                    showFlagsList();
                    return true;
                });
    }

    @SuppressLint("SetTextI18n")
    public void RegexEx(){
        String rV = regexVal.getText().toString();
        if (rV.equals("")){
            counter.setText("Совпадений: 0");
            getCounterResult.setText(sourceSoup.getText());
            return;
        }
        try {
            //noinspection WrongConstant
            Matcher m = Pattern.compile(rV, flags.getFlags()).matcher(sourceSoup.getText());
            Spannable spannable = new SpannableString(sourceSoup.getText());
            int i = 0;
            while (m.find()){
                spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), m.start(), m.end(), 33);
                i++;
            }
            getCounterResult.setText(spannable);
            if (i == 1) {
                counter.setText(i  + getString(R.string.one_match));
            } else counter.setText(getString(R.string.more_matches) + i);
        } catch (PatternSyntaxException pse) {
            getCounterResult.setTextColor(getResources().getColor(R.color.green));
            getCounterResult.setText(getString(R.string.illegal_syntax1)+ pse.getMessage() + getString(R.string.illegal_syntax2));
        }
    }

    private void showFlagsList(){
        new AlertDialog.Builder(getActivity())
                .setTitle("Флаги regex")
                .setMultiChoiceItems(flags.getOptions(), flags.getSelectedBooleans(), (dialogInterface, zoe, isChecked) -> {
                    if (isChecked){
                        flags.add(zoe);
                    } else {
                        flags.remove(zoe);
                    }
                }).setPositiveButton("ok", (dialogInterface, volf) -> {
            currentFlag.setText(flags.getFlagString());
            /**
             * Проверяем поля на пустоту, сделано для того чтобы не применял regex к пустому выражению "".
             * Иначе получается, если в {@param sourceSoup} пусто, то возникает коллапс
             * "" = ""  = 1 совпадение
             */
            if (!regexVal.getText().toString().isEmpty() && !sourceSoup.getText().toString().isEmpty()) {
                RegexEx();
            }
        }).create().show();

    }

    private void clearAllText(){
        sourceSoup.setText("");
        regexVal.setText("");
        counter.setText("");
        getCounterResult.setText("");
    }
}
