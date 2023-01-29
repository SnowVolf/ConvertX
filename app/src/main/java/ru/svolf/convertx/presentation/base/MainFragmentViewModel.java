package ru.svolf.convertx.presentation.base;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

public class MainFragmentViewModel extends AndroidViewModel {
    //declare private variables
    private final SharedPreferences preferences;
    private final MutableLiveData<Integer> textSize = new MutableLiveData<Integer>();

    //constructor
    public MainFragmentViewModel(@NonNull Application application) {
        super(application);
        preferences = PreferenceManager.getDefaultSharedPreferences(application);

        //read the shared preferences and set the text size
        textSize.setValue(preferences.getInt("AppFontSize", 16));
    }

    //getter
    public LiveData<Integer> getTextSize() {
        return textSize;
    }

    //method to update the text size
    public void updateTextSize(int size) {
        //update the shared preference
        preferences.edit().putInt("AppFontSize", size).apply();
        //update the text size live data
        textSize.setValue(size);
    }
}