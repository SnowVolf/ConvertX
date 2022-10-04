package ru.SnowVolf.convertx.regexdragon;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.view.menu.MenuBuilder;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import ru.SnowVolf.convertx.R;
import ru.SnowVolf.convertx.settings.DefStrings;

public class RegexDragonFragment extends Fragment{
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private View rootView;

    private int[] icTab = {
            R.drawable.dragon,
            R.drawable.ic_menu_spur,
            R.drawable.ic_toast_info_outline
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(DefStrings.INSTANCE.getTAG(), "RegexDragon");
        Log.w(DefStrings.INSTANCE.getTAG(), "onCreate");
        super.onCreate(savedInstanceState);
        (getActivity()).setTitle("RegexDragon");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.activity_regex_dragon, container, false);

        viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tablayout);
        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setIcons();
    }

    private void setIcons() {
            tabLayout.getTabAt(0).setIcon(icTab[0]);
            tabLayout.getTabAt(1).setIcon(icTab[1]);
            tabLayout.getTabAt(2).setIcon(icTab[2]);
        Log.i(DefStrings.INSTANCE.getTAG(), "Set Icons as Tab indicators");
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new RegexDragonValidator(), "Regex");
        adapter.addFragment(new SpurFragment(), "Шпора");
        adapter.addFragment(new TheoryFragment(), "Теория");
        Log.i(DefStrings.INSTANCE.getTAG(), "Bind Fragments : Total : " + adapter.getCount());
        viewPager.setAdapter(adapter);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (menu != null) {
            menu.clear();
        } else
            menu = new MenuBuilder(getContext());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            getActivity().finish();
        return true;

    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        rootView = null;
    }

}
