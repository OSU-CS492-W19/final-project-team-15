package com.example.guessthatname.utils;

import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import android.R;
import android.util.Log;
import android.util.Pair;

public class SpotifyUtil {
    private static Token token = null;
    private static long token_expiration;

    public static void getAuthToken() {
        if (token == null || new Date().getTime() >= token_expiration) {
            try {
                setToken(NetworkUtils.getToken());
                // new TokenTask().execute().get();
                Log.d("SpotifyUtil", "finished getAuthToken");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class Token {
        String access_token;
        String token_type;
        int expires_in;
        String scope;
    }

    public static void setToken(String s) {
        Log.d("SpotifyUtil", "TOKEN SET");
        Gson gson = new Gson();
        token = gson.fromJson(s, Token.class);
        Log.d("SpotifyUtil", "access_token: " + token.access_token);
        token_expiration = new Date().getTime() + (token.expires_in * 1000);
    }


    public static class CategoryList{
        public Categories categories;
    }
    public static class Categories{
        public ArrayList<Category> items;
    }
    public static class Category{
        public String href;
        public ArrayList<CategoryIcon> icons;
        public String id;
        public String name;
    }
    public static class CategoryIcon{
        public int height;
        public int width;
        public String url;
    }

    public static class GetListCategories extends AsyncTask<Void, Void, CategoryList> {
        public interface AsyncCallback {
            void onCategoryListLoadFinished(CategoryList categoryList);
        }

        private AsyncCallback mCallback;

        public GetListCategories(AsyncCallback callback) {
            mCallback = callback;
        }

        @Override
        protected CategoryList doInBackground(Void... voids) {
            getAuthToken();
            CategoryList results = null;
            try {
                Log.d("SpotifyUtil", "Attempting to connect to API");
                String categoryListJSON = NetworkUtils.doHTTPGet("https://api.spotify.com/v1/browse/categories",
                        token.access_token);
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
        protected void onPostExecute(CategoryList result) {
            if (result != null) {
                mCallback.onCategoryListLoadFinished(result);
            }
        }
    }

    public static class GetCategory extends AsyncTask<Void, Void, Category> {
        public interface AsyncCallback {
            void onCategoryLoadFinished(Category category);
        }

        private AsyncCallback mCallback;
        private String mCategoryId;

        public GetCategory(String categoryId, AsyncCallback callback) {
            mCallback = callback;
            mCategoryId = categoryId;
        }

        @Override
        protected Category doInBackground(Void... voids) {
            getAuthToken();
            Category results = null;
            try {
                Log.d("SpotifyUtil", "Attempting to connect to API");
                String categoryJSON = NetworkUtils
                        .doHTTPGet("https://api.spotify.com/v1/browse/categories/" + mCategoryId, token.access_token);
                Gson gson = new Gson();
                results = gson.fromJson(categoryJSON, Category.class);
                return results;
            } catch (IOException e) {
                Log.d("SpotifyUtil", e.getStackTrace().toString());
            }

            return results;
        }

        @Override
        protected void onPostExecute(Category result) {
            Log.d("SpotifyUtilGetCategory", "Category name: " + result.name);
            if (result != null) {
                mCallback.onCategoryLoadFinished(result);
            }
        }
    }


    public static class PlayListList{
        public Playlists playlists;
    }
    public static class Playlists{
        public ArrayList<Playlist> items;
    }
    public static class Playlist{
        public ArrayList<TrackLink> tracks;
    }
    public static class TrackLink{
        public String href;
        public int total;
    }

    public static class GetCategoriesPlaylist extends AsyncTask<Void, Void, PlayListList> {
        public interface AsyncCallback {
            void onPlayListListLoadFinished(PlayListList playListList);
        }

        private AsyncCallback mCallback;
        private String mUrl;

        public GetCategoriesPlaylist(String url, AsyncCallback callback) {
            Log.d("SpotifyUtil", "GetCategoriesPlaylist url: " + url);
            mUrl = url;
            mCallback = callback;
        }

        @Override
        protected PlayListList doInBackground(Void... voids) {
            getAuthToken();
            PlayListList results = null;
            try {
                Log.d("SpotifyUtil", "Attempting to connect to API");
                String categoriesPlaylistJSON = NetworkUtils.doHTTPGet(mUrl, token.access_token);
                Gson gson = new Gson();
                results = gson.fromJson(categoriesPlaylistJSON, PlayListList.class);
                return results;
            } catch (IOException e) {
                Log.d("SpotifyUtil", e.getStackTrace().toString());
            }

            return results;
        }

        @Override
        protected void onPostExecute(PlayListList result) {
            if (result != null) {
                mCallback.onPlayListListLoadFinished(result);
            }
        }
    }


    public static class PlayListTracks{
        public ArrayList<PlayListTrack> items;
    }
    public static class PlayListTrack{
        public Track track;
    }
    public static class Track{
        public String preview_url;
        public int popularity;
        public String name;
        public String uri;
        public Album album;
    }
    public static class Album{
        public ArrayList<SpotifyImage> images;
    }
    public static class SpotifyImage{
        public int height;
        public int width;
        public String url;
    }

    public static class GetPlayListTracks extends AsyncTask<Void, Void, PlayListTracks> {
        public interface AsyncCallback {
            void onPlayListTracksLoadFinished(PlayListTracks playListTracks);
        }

        private AsyncCallback mCallback;
        private String mUrl;

        public GetPlayListTracks(String url, AsyncCallback callback) {
            mUrl = url;
            mCallback = callback;
        }

        @Override
        protected PlayListTracks doInBackground(Void... voids) {
            getAuthToken();
            PlayListTracks results = null;
            try {
                Log.d("SpotifyUtil", "Attempting to connect to API");
                String playListTracksJSON = NetworkUtils.doHTTPGet(mUrl, token.access_token);
                Gson gson = new Gson();
                results = gson.fromJson(playListTracksJSON, PlayListTracks.class);
                return results;
            } catch (IOException e) {
                Log.d("SpotifyUtil", e.getStackTrace().toString());
            }

            return results;
        }

        @Override
        protected void onPostExecute(PlayListTracks result) {
            if (result != null) {
                mCallback.onPlayListTracksLoadFinished(result);
            }
        }
    }

}
