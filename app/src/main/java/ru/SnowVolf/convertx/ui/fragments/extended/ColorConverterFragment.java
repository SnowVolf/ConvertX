package ru.SnowVolf.convertx.ui.fragments.extended;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.view.menu.MenuBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.SnowVolf.convertx.R;
import ru.SnowVolf.convertx.ui.Toasty;
import ru.SnowVolf.convertx.ui.fragments.base.BaseFragment;

/**
 * Created by Snow Volf on 04.03.2017, 6:35
 */

public class ColorConverterFragment extends BaseFragment {
    public EditText hexaVoid;
    public TextView smali;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_timestamp, container, false);
        hexaVoid = (EditText) rootView.findViewById(R.id.Timestamp);
        hexaVoid.setHint("hex");
        hexaVoid.setInputType(InputType.TYPE_CLASS_TEXT);
        hexaVoid.setFocusable(true);
        smali = (TextView) rootView.findViewById(R.id.humanDate);
        smali.setHint("smali");
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Typeface Mono = Typeface.createFromAsset(getActivity().getAssets(), "fonts/RobotoMono-Regular.ttf");
        hexaVoid.setTypeface(Mono);
        hexaVoid.setTypeface(Mono);
        hexaVoid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!hexaVoid.getText().toString().isEmpty()) {
                    try {
                        String source = hexaVoid.getText().toString().toLowerCase();
                        String digi2let =  source.replace("0", "f").replace("1", "e").replace("2", "d").replace("3", "c").replace("4","b").replace("5","a").replace("6","9").replace("7", "8").replace("8", "7").replace("9", "6").replace("a", "5").replace("b", "4").replace("c", "3").replace("d", "2").replace("e", "1").replace("f", "0");
                        smali.setText(digi2let);
                    } catch (NumberFormatException nfe) {
                        nfe.printStackTrace();
                    }

                }
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
                    ConvertHexaVoid();
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            getActivity().finish();
        return true;
    }

    public void clearAllText(){
        hexaVoid.setText("");
        smali.setText("");
        Toasty.info(getContext(), getString(R.string.cleared), Toast.LENGTH_SHORT, true).show();
    }

    public void ConvertHexaVoid(){
        if (!smali.getText().toString().isEmpty() && hexaVoid.getText().toString().isEmpty()) {
            String srcDate = smali.getText().toString();
            Date local;
            try {
                local = new SimpleDateFormat("dd.MM.YYYY HH:mm:ss", Locale.getDefault()).parse(srcDate);
                hexaVoid.setText(""+local.getTime());
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
