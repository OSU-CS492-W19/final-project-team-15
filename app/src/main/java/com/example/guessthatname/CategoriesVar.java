package com.example.guessthatname;

import android.util.Log;

import com.example.guessthatname.utils.SpotifyUtil;

public class CategoriesVar {
    private SpotifyUtil.CategoryList categories = null;
    private ChangeListener listener;

    public SpotifyUtil.CategoryList getCats() {
        return categories;
    }

    public Boolean areCats(){
        Log.d("CategoriesVar", "is not null: " + (categories != null));
        return categories != null;
    }

    public void setCategories(SpotifyUtil.CategoryList cats) {
        this.categories = cats;
        if (listener != null) listener.onChange();
    }

    public ChangeListener getListener() {
        return listener;
    }

    public void setListener(ChangeListener listener) {
        this.listener = listener;
    }

    public interface ChangeListener {
        void onChange();
    }
}