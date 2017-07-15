package ru.SnowVolf.convertx.ui.fragments.extra;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ru.SnowVolf.convertx.R;
import ru.SnowVolf.convertx.other.Extras;
import ru.SnowVolf.convertx.settings.DefStrings;
import ru.SnowVolf.convertx.ui.activity.MainActivity;
import ru.SnowVolf.convertx.ui.fragments.base.BaseFragment;

public class ChangelistFragment extends BaseFragment {
    private TextView changelog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_changelist, container, false);
        changelog = (TextView) rootView.findViewById(R.id.text_changelog);
        return rootView;
    }

    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setHasOptionsMenu(true);
        Log.i(DefStrings.INSTANCE.getTAG(), "StartFragment : Changelist");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TITLE = R.string.changelog;
        ((MainActivity) getActivity()).setToolbarSubtitle(Extras.getBuildName(getContext()));

        final StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getContext().getAssets().open("CHANGELOG.javagirl"), "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Typeface Mono = Typeface.createFromAsset(getContext().getAssets(), "fonts/RobotoMono-Regular.ttf");
        changelog.setTypeface(Mono);
        changelog.setText(sb);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (menu != null) {
            menu.clear();
        } else
            menu = new MenuBuilder(getContext());
        menu.add("").setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            getActivity().finish();
        return true;
    }
}
