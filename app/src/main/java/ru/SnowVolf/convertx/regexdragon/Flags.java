package ru.SnowVolf.convertx.regexdragon;

import java.util.ArrayList;

/**
 * Created by Snow Volf on 26.02.2017, 11:29
 */

public class Flags {
    private ArrayList<Boolean> mData = new ArrayList<>();
    private CharSequence[] mOptions = new CharSequence[]{"Case insensitive [i]", "Multiline [m]", "Comments [x]", "Dotall [s]", "Literal [l]", "Unicode Case [u]", "Unix Lines [d]"};

    public Flags(){
        for (int i = 0; i < mOptions.length; i++){
            mData.add(i, false);
        }
    }

    public CharSequence[] getOptions(){
        return mOptions;
    }

    public void add(int id){
        mData.set(id, true);
    }

    public void remove(int id){
        mData.set(id, false);
    }

    public boolean[] getSelectedBooleans(){
        boolean[] data = new boolean[mOptions.length];
        for (int i = 0; i < mData.size(); i++){
            data[i] = mData.get(i);
        }
        return data;
    }
    /**
     * Чтобы не забыть
     * CANON_EQ (no effect on Android) == 128
     * CASE_INSENSITIVE == 2
     * COMMENTS == 4
     * DOTALL == 32
     * LITERAL == 16
     * MULTILINE == 8
     * UNICODE_CASE == 64
     * UNICODE_CHARACTER_CLASS (no effect on Android) == 256
     * UNIX_LINES == 1
     *
     * все константы можно посмотреть на developer.android.com
     */

    public int getFlags(){
        int data = 0;
        if (mData.get(0)){
            data = 2;
        }
        if (mData.get(1)){
            return data | 8;
        }
        if (mData.get(2)){
            return data | 4;
        }
        if (mData.get(3)){
            return data | 32;
        }
        if (mData.get(4)){
            return data | 16;
        }
        if (mData.get(5)){
            return data | 64;
        }
        if (mData.get(6)){
            return data | 1;
        }
        return data;
    }

    public String getFlagString(){
        String data = "/";
        if (mData.get(0)){
            data = data + "i";
        }
        if (mData.get(1)){
            data = data + "m";
        }
        if (mData.get(2)){
            data = data + "x";
        }
        if (mData.get(3)){
            data = data + "s";
        }
        if (mData.get(4)){
            data = data + "l";
        }
        if (mData.get(5)){
            data = data + "u";
        }
        if (mData.get(6)){
            data = data + "d";
        }
        if (data.equals("/")){
            return "Флаги";
        }
        return data;
    }
}
