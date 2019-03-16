package com.example.guessthatname.utils;

import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;
import android.R;
import android.util.Log;
import android.util.Pair;

public class SpotifyUtil {
    private static Token token = null;

    public static void getAuthToken(){
        try {
            new TokenTask().execute().get();
            Log.d("SpotifyUtil", "finished getAuthToken");
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    static class Token{
        String access_token;
        String token_type;
        int expires_in;
        String scope;
    }
    public static void setToken(String s){
        Log.d("SpotifyUtil", "TOKEN SET");
        Gson gson = new Gson();
        token = gson.fromJson(s, Token.class);
        Log.d("SpotifyUtil", "access_token: " + token.access_token);
//        switch(pair.second){
//
//        }

    }


    static class CategoryList{
        Categories categories;
    }
    static class Categories{
        ArrayList<Category> items;
    }
    static class Category{
        String href;
        ArrayList<CategoryIcon> icons;
        String id;
        String name;
    }
    static class CategoryIcon{
        int height;
        int width;
        String url;
    }

    static class GetListCategories extends AsyncTask<Void, Void, CategoryList> {
        public interface AsyncCallback {
            void onCategoryListLoadFinished(CategoryList categoryList);
        }

        private AsyncCallback mCallback;

        GetListCategories(AsyncCallback callback) {
            mCallback = callback;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(token == null)
                getAuthToken();
        }





        @Override
        protected CategoryList doInBackground(Void... voids) {
            CategoryList results = null;
            try {
                Log.d("SpotifyUtil", "Attempting to connect to API");
                String categoryListJSON = NetworkUtils.doHTTPGet("https://api.spotify.com/v1/browse/categories", token.access_token);
                Gson gson = new Gson();
                results = gson.fromJson(categoryListJSON, CategoryList.class);
                Log.d("SpotifyUtil", "results: " + results.categories.items.size());
                return results;
            } catch (IOException e) {
                Log.d("SpotifyUtil", e.getStackTrace().toString());
            }

            return results;
        }

        @Override
        protected void onPostExecute(CategoryList result){
            if (result != null) {
                mCallback.onCategoryListLoadFinished(result);
            }
        }
    }


    static class PlayListList{
        MediaStore.Audio.Playlists playlists;
    }
    static class Playlists{
        ArrayList<Playlist> items;
    }
    static class Playlist{
        ArrayList<TrackLink> tracks;
    }
    static class TrackLink{
        String href;
        int total;
    }

    public static void getCategoriesPlaylist(){

    }
    static class PlayListTracks{
        ArrayList<PlayListTrack> items;
    }
    static class PlayListTrack{
        Track track;
    }
    static class Track{
        String preview_url;
        int popularity;
        String name;
    }
    public static void getPlaylistsTracks(){

    }

}
