package ru.SnowVolf.convertx.fragments.extended.hash;

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
import android.widget.TextView;
import android.widget.Toast;

import ru.SnowVolf.convertx.R;
import ru.SnowVolf.convertx.algorhitms.ChecksumAlgs;
import ru.SnowVolf.convertx.ui.Toasty;

/**
 * Created by Snow Volf on 14.02.2017.
 */

public class MdSha extends Fragment {
    public EditText data;
    public TextView dataOut;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_ex, container, false);
        data = (EditText) rootView.findViewById(R.id.exData);
        data.setHint(R.string.hint_md_sha);
        data.setFocusable(true);
        dataOut = (TextView) rootView.findViewById(R.id.exText);
        return rootView;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Typeface Mono = Typeface.createFromAsset(getActivity().getAssets(), "fonts/RobotoMono-Regular.ttf");
        data.setTypeface(Mono);
        dataOut.setTypeface(Mono);
        data.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String source = data.getText().toString();
                /**
                 * Алгоритмы в ChecksumAlgs, чтоб код не заграждать
                 */
                dataOut.setText(new StringBuilder()
                        //фикс предупреждения Lint: [do not concentrate text displayed with setText. Use resources string with placeholders] в Android Studio
                        .append("MD5:\n").append(ChecksumAlgs.MD5(source))
                        + "\n\nSHA-1:\n" + ChecksumAlgs.SHA1(source)
                        + "\n\nSHA-224:\n" + ChecksumAlgs.SHA224(source)
                        + "\n\nSHA-384:\n" + ChecksumAlgs.SHA384(source)
                        + "\n\nSHA-512:\n" + ChecksumAlgs.SHA512(source));
            }
        });
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (menu != null) {
            menu.clear();
        } else
            menu = new MenuBuilder(getContext());
        //добавляем пункты меню
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
        data.setText("");
        dataOut.setText("");
        Toasty.success(getContext(), getString(R.string.cleared), Toast.LENGTH_SHORT, true).show();
    }
}
