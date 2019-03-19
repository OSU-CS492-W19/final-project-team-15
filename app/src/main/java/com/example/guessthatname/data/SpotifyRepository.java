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
        category.postValue(null);
        playlist.postValue(null);
        tracks.postValue(null);
        loadingStatus.setValue(Status.SUCCESS);
    }

    public void loadCategory(String genre_key){
        Log.d("SpotifyUtil", "genre_key: " + genre_key);
        //Is category cached?
        loadingStatus.setValue(Status.LOADING);
        if(category.getValue() == null) {
            new SpotifyUtil.GetCategory(genre_key, this).execute();
        } else {
            loadPlaylist();
        }
    }
    public void loadPlaylist(){
        //Is playlist cached?
        if(playlist.getValue() == null){
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
        this.category.postValue(category);
    }

    @Override
    public void onPlayListListLoadFinished(SpotifyUtil.PlayListList playlistList){
        Random rand = new Random();
        int index = rand.nextInt(playlistList.playlists.items.size() - 1);
        this.playlist.postValue(playlistList.playlists.items.get(index));
    }

    @Override
    public void onPlayListTracksLoadFinished(SpotifyUtil.PlayListTracks tracks){
        loadingStatus.setValue(Status.SUCCESS);
        this.tracks.postValue(tracks.items);
    }

}
