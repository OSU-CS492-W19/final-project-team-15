package com.example.guessthatname;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.example.guessthatname.data.SpotifyRepository;
import com.example.guessthatname.data.Status;
import com.example.guessthatname.utils.SpotifyUtil;

import java.util.ArrayList;

public class GameViewModel extends AndroidViewModel {
    private static final String TAG = "SpotifyUtil";

    private LiveData<SpotifyUtil.Category> category;
    private LiveData<SpotifyUtil.Playlist> playlist;
    private LiveData<ArrayList<SpotifyUtil.PlayListTrack>> tracks;
    private LiveData<Status> loadingStatus;

    private SpotifyRepository spotifyRepository;

    public GameViewModel(Application application){
        super(application);
        spotifyRepository = new SpotifyRepository();
        clearRepository();
    }

    public void clearRepository() {
        spotifyRepository.clearRepository();
        category = spotifyRepository.getCategory();
        Log.d(TAG, "clearRepository1: " + category.getValue());
        playlist = spotifyRepository.getPlaylist();
        Log.d(TAG, "clearRepository2: "+ playlist.getValue());
        tracks = spotifyRepository.getTracks();
        Log.d(TAG, "clearRepository3: "+ tracks.getValue());
        loadingStatus = spotifyRepository.getLoadingStatus();
        Log.d("SpotifyUtil", "Repo Cleared");
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

    public void loadCategory(String categoryID) {
        spotifyRepository.loadCategory(categoryID);
    }

    public void loadPlaylist() {
        spotifyRepository.loadPlaylist();
    }

    public void loadTracks() {
        spotifyRepository.loadTracks();
    }

}
