package ru.SnowVolf.convertx.fragments.extended;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.view.menu.MenuBuilder;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ru.SnowVolf.convertx.R;
import ru.SnowVolf.convertx.fragments.extended.hash.Adler;
import ru.SnowVolf.convertx.fragments.extended.hash.CRC;
import ru.SnowVolf.convertx.fragments.extended.hash.MdSha;

/**
 * Created by Snow Volf on 14.02.2017.
 */

public class ListCoders extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_ex_encoders, container, false);
        Button adler = (Button) rootView.findViewById(R.id.adler32);
        Button crc = (Button) rootView.findViewById(R.id.crc);
        Button md = (Button) rootView.findViewById(R.id.md5);
        Button xml = (Button) rootView.findViewById(R.id.unescape_xml);
        Button timestamp = (Button) rootView.findViewById(R.id.timestamp_converter);
        Button hex2smali = (Button) rootView.findViewById(R.id.color_converter);
        adler.setOnClickListener(this::startAdler);
        crc.setOnClickListener(this::startCRC);
        md.setOnClickListener(this::startMD);
        xml.setOnClickListener(this::startXML);
        hex2smali.setOnClickListener(this::startHex2Smali);
        timestamp.setOnClickListener(this::startTimestamp);
        return rootView;
    }
    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
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

    public void startAdler(View v){
        getActivity().getSupportFragmentManager().popBackStack();
        getActivity().getSupportFragmentManager()
                .beginTransaction().replace(R.id.frame_container, new Adler())
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }
    public void startCRC(View v){
        getActivity().getSupportFragmentManager().popBackStack();
        getActivity().getSupportFragmentManager()
                .beginTransaction().replace(R.id.frame_container, new CRC())
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }
    public void startMD(View v){
        getActivity().getSupportFragmentManager().popBackStack();
        getActivity().getSupportFragmentManager()
                .beginTransaction().replace(R.id.frame_container, new MdSha())
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }
    public void startXML(View v){
        getActivity().getSupportFragmentManager().popBackStack();
        getActivity().getSupportFragmentManager()
                .beginTransaction().replace(R.id.frame_container, new UnescaperFragment())
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }
    public void startTimestamp(View v){
        getActivity().getSupportFragmentManager().popBackStack();
        getActivity().getSupportFragmentManager()
                .beginTransaction().replace(R.id.frame_container, new TimestampFragment())
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }
    public void startHex2Smali(View v){
        getActivity().getSupportFragmentManager().popBackStack();
        getActivity().getSupportFragmentManager()
                .beginTransaction().replace(R.id.frame_container, new ColorConverterFragment())
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }
}
