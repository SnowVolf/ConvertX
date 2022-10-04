package ru.SnowVolf.convertx.ui.fragments.base;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import ru.SnowVolf.convertx.R;

/**
 * Created by Snow Volf on 20.06.2017, 0:43
 */

public class BaseFragment extends Fragment {
    public View rootView;
    public Typeface Mono;
    public BaseFragment(){}
    public @StringRes
    int TITLE = R.string.app_name;


    @Override
    public void onDestroyView(){
        rootView = null;
        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(Bundle savedState){
        super.onActivityCreated(savedState);
        getActivity().setTitle(TITLE);
        Mono = Typeface.createFromAsset(getActivity().getAssets(), "fonts/RobotoMono-Regular.ttf");
    }

    @Override
    public void onResume(){
        super.onResume();
        try {
            getActivity().setTitle(TITLE);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
