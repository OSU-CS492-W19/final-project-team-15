package com.example.guessthatname.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.guessthatname.utils.SpotifyUtil;

import java.util.ArrayList;
import java.util.Random;

public class SpotifyRepository implements SpotifyUtil.GetCategory.AsyncCallback, SpotifyUtil.GetCategoriesPlaylist.AsyncCallback, SpotifyUtil.GetPlayListTracks.AsyncCallback {

    private MutableLiveData<SpotifyUtil.Category> category;
    private MutableLiveData<SpotifyUtil.Playlist> playlist;
    private MutableLiveData<ArrayList<SpotifyUtil.PlayListTrack>> tracks;
    private MutableLiveData<Status> loadingStatus;

    public SpotifyRepository() {
        category =  new MutableLiveData<>();
        playlist = new MutableLiveData<>();
        tracks = new MutableLiveData<>();

        loadingStatus = new MutableLiveData<>();
        loadingStatus.setValue(Status.SUCCESS);
    }

    public void clearRepository() {
        category.setValue(null);
        playlist.setValue(null);
        tracks.setValue(null);
        loadingStatus.setValue(Status.SUCCESS);
    }

    public void loadCategory(String genre_key){
        Log.d("SpotifyUtil", "genre_key: " + genre_key);
        //Is category cached?
        loadingStatus.setValue(Status.LOADING);
        if(category.getValue() == null) {
            Log.d("SpotifyUtil", "executing task");
            new SpotifyUtil.GetCategory(genre_key, this).execute();
        } else {
            Log.d("SpotifyUtil", "Loading playlist");
            loadPlaylist();
        }
    }
    public void loadPlaylist(){
        //Is playlist cached?
        Log.d("SpotifyUtil", "LoadPlaylists called");
        if(playlist.getValue() == null){
            String url = category.getValue().href;
            Log.d("SpotifyUtil", "Loaded Category: " + category.getValue().name);
            new SpotifyUtil.GetCategoriesPlaylist(url, this).execute();
        } else {
            Log.d("SpotifyUtil", "Using old playlist");
            loadTracks();
        }
    }
    public void loadTracks(){
        //Is tracks cached?
        if(tracks.getValue() == null) {
            new SpotifyUtil.GetPlayListTracks(playlist.getValue().tracks.href, this).execute();
        }
    }

    public LiveData<SpotifyUtil.Category> getCategory() {
        return category;
    }

    public LiveData<SpotifyUtil.Playlist> getPlaylist() {
        return playlist;
    }

    public LiveData<ArrayList<SpotifyUtil.PlayListTrack>> getTracks() {
        return tracks;
    }

    public LiveData<Status> getLoadingStatus() {
        return loadingStatus;
    }

    @Override
    public void onCategoryLoadFinished(SpotifyUtil.Category category) {
        this.category.setValue(category);
    }

    @Override
    public void onPlayListListLoadFinished(SpotifyUtil.PlayListList playlistList){
        if(playlistList.playlists.items.size() > 0) {
            Random rand = new Random(System.currentTimeMillis());
            int index = rand.nextInt(playlistList.playlists.items.size());
            this.playlist.setValue(playlistList.playlists.items.get(index));
        } else {
            this.playlist.setValue(null);
        }
    }

    @Override
    public void onPlayListTracksLoadFinished(SpotifyUtil.PlayListTracks tracks){
        loadingStatus.setValue(Status.SUCCESS);
        if(tracks.items.size() > 0) this.tracks.setValue(tracks.items);
        else this.tracks.setValue(null);
    }

}
