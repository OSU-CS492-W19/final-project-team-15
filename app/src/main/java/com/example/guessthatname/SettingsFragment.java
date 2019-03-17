package com.example.guessthatname;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.util.Pair;

import com.example.guessthatname.utils.SpotifyUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener,
        SpotifyUtil.GetListCategories.AsyncCallback {

    private CategoriesVar mCategories = new CategoriesVar();
    private Pair<ArrayList<String>, ArrayList<String>> mPrefEntries;

    public void onCategoryListLoadFinished(SpotifyUtil.CategoryList categoryList){
        mCategories.setCategories(categoryList);
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.prefs);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String genre = prefs.getString("genre_key", "___default___");
        Log.d("settings fragment", "Genre: " + genre);
        mPrefEntries = new Pair<>(new ArrayList<String>(), new ArrayList<String>());
        mCategories.setListener(new CategoriesVar.ChangeListener() {
            @Override
            public void onChange() {
                ListPreference lp = (ListPreference) findPreference("genre_key");
                mPrefEntries = getCategories();
                lp.setEntries(mPrefEntries.first.toArray(new CharSequence[mPrefEntries.first.size()]));
                lp.setEntryValues(mPrefEntries.second.toArray(new CharSequence[mPrefEntries.second.size()]));
                lp.setValueIndex(lp.findIndexOfValue(genre));
                lp.setSummary(mPrefEntries.first.get(lp.findIndexOfValue(genre)));
            }
        });
        try {
            new SpotifyUtil.GetListCategories(this).execute().get();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String genre = prefs.getString("genre_key", "___default___");
        ListPreference lp = (ListPreference) findPreference("genre_key");

        lp.setSummary(mPrefEntries.first.get(lp.findIndexOfValue(genre)));
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

    public Pair<ArrayList<String>, ArrayList<String>>getCategories(){

        Pair<ArrayList<String>, ArrayList<String>> categories = new Pair<>(new ArrayList<String>(), new ArrayList<String>());
        for(SpotifyUtil.Category genre : mCategories.getCats().categories.items){
            categories.first.add(genre.name);
            categories.second.add(genre.id);
        }

        return categories;
    }
}
