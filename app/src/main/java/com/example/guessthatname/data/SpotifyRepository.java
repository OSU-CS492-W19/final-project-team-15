package com.example.guessthatname.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.guessthatname.utils.SpotifyUtil;

import java.util.ArrayList;

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
    }

    public void loadCategory(String genre_key){
        if(genre_key == "___default___")
            genre_key = "toplists";
        Log.d("SpotifyUtil", "genre_key: " + genre_key);
        //Is category cached?
        if(category.getValue() == null) {
            loadingStatus.setValue(Status.LOADING);
            new SpotifyUtil.GetCategory(genre_key, this).execute();
        } else {
            loadPlaylist();
        }
    }
    public void loadPlaylist(){
        //Is playlist cached?
        if(playlist.getValue() == null){
            loadingStatus.setValue(Status.LOADING);
            String url = category.getValue().href;
            Log.d("SpotifyUtil", "Loaded Category: " + category.getValue().name);
            new SpotifyUtil.GetCategoriesPlaylist(url, this).execute();
        } else {
            loadTracks();
        }
    }
    public void loadTracks(){
        //Is tracks cached?
        if(tracks.getValue() == null) {
            loadingStatus.setValue(Status.LOADING);
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
        loadingStatus.setValue(Status.SUCCESS);
        this.category.postValue(category);
    }

    @Override
    public void onPlayListListLoadFinished(SpotifyUtil.PlayListList playlistList){
        loadingStatus.setValue(Status.SUCCESS);
        this.playlist.postValue(playlistList.playlists.items.get(0));
    }

    @Override
    public void onPlayListTracksLoadFinished(SpotifyUtil.PlayListTracks tracks){
        loadingStatus.setValue(Status.SUCCESS);
        this.tracks.postValue(tracks.items);
    }

}
