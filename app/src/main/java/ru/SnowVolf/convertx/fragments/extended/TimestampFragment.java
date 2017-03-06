package ru.SnowVolf.convertx.fragments.extended;

import android.graphics.Typeface;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.SnowVolf.convertx.R;
import ru.SnowVolf.convertx.algorhitms.DecoderAlgs;
import ru.SnowVolf.convertx.other.Extras;
import ru.SnowVolf.convertx.ui.Toasty;


/**
 * Created by Snow Volf on 26.02.2017, 20:44
 */

public class TimestampFragment extends Fragment {
    public EditText Timestamp;
    public EditText normalDate;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_timestamp, container, false);
        Timestamp = (EditText) rootView.findViewById(R.id.Timestamp);
        Timestamp.setHint("Timestamp");
        Timestamp.setFocusable(true);
        normalDate = (EditText) rootView.findViewById(R.id.humanDate);
        normalDate.setHint("dd.MM.YYYY HH:mm:ss");
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
        Timestamp.setTypeface(Mono);
        Timestamp.setTypeface(Mono);
        Timestamp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!Timestamp.getText().toString().isEmpty()) {
                    try {
                        String source = Timestamp.getText().toString();
                        long timestamp = Long.parseLong(source);
                        String human = DecoderAlgs.getNormalDate(timestamp);
                        normalDate.setText(human);
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
        menu.add(R.string.cur_timestamp)
                .setIcon(R.drawable.ic_menu_insert_text)
                .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                .setOnMenuItemClickListener(menuItem -> {
                   InsertCurrentTimestamp();
                    return true;
                });
        menu.add(R.string.convert)
                .setIcon(R.drawable.ic_menu_convert)
                .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                .setOnMenuItemClickListener(menuItem -> {
                    ConvertDate2Timestamp();
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
        Timestamp.setText("");
        normalDate.setText("");
        Toasty.info(getContext(), getString(R.string.cleared), Toast.LENGTH_SHORT, true).show();
    }

    public void InsertCurrentTimestamp(){
        Long epoch = System.currentTimeMillis();
        Timestamp.setText(""+epoch.toString());
        Timestamp.setSelection(Timestamp.getText().length());
    }

    public void ConvertDate2Timestamp(){
        if (!normalDate.getText().toString().isEmpty() && Timestamp.getText().toString().isEmpty()) {
            String srcDate = normalDate.getText().toString();
            Date local;
            try {
                local = new SimpleDateFormat("dd.MM.YYYY HH:mm:ss", Locale.getDefault()).parse(srcDate);
                Timestamp.setText(""+local.getTime());
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
