package com.example.guessthatname;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.util.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener, SpotifyUtil.o {
    private CategoryList categories;


    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.prefs);
        ListPreference lp = (ListPreference) findPreference("genre_key");
        Log.d(this.getClass().toString(), lp.toString());
        Pair<ArrayList<String>, ArrayList<String>> Entries = getCategories();
        lp.setEntries(Entries.first.toArray(new String[0]));
        lp.setEntryValues(Entries.second.toArray(new String[0]));
        lp.setValueIndex(0);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    public Pair<ArrayList<String>, ArrayList<String>> getCategories(){
        Pair<ArrayList<String>, ArrayList<String>> categories = new Pair<>(new ArrayList<String>(), new ArrayList<String>());
        categories.first.add("one");
        categories.first.add("two");

        categories.second.add("1");
        categories.second.add("2");

        return categories;
    }
}
