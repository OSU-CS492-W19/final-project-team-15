package com.example.guessthatname;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.example.guessthatname.utils.SpotifyUtil;

import java.util.ArrayList;

public class GameViewModel extends AndroidViewModel implements SpotifyUtil.GetCategory.AsyncCallback, SpotifyUtil.GetCategoriesPlaylist.AsyncCallback, SpotifyUtil.GetPlayListTracks.AsyncCallback {
    private int score;
    private MutableLiveData<SpotifyUtil.Category> category;
    private MutableLiveData<SpotifyUtil.Playlist> playlist;
    private MutableLiveData<ArrayList<SpotifyUtil.PlayListTrack>> tracks;

    public GameViewModel(Application application){
        super(application);
        category = new MutableLiveData<SpotifyUtil.Category>();
        playlist = new MutableLiveData<SpotifyUtil.Playlist>();
        tracks = new MutableLiveData<ArrayList<SpotifyUtil.PlayListTrack>>();
    }
    public LiveData<SpotifyUtil.Category> getCategory(){
        return category;
    }
    public LiveData<SpotifyUtil.Playlist> getPlaylist(){
        return playlist;
    }
    public LiveData<ArrayList<SpotifyUtil.PlayListTrack>> getTracks(){
        return tracks;
    }
    public void resetGame(){
        score = 0;
        category = null;
        playlist = null;
        tracks = null;
    }

    // callbacks
    public void onCategoryLoadFinished(SpotifyUtil.Category category){
        this.category.postValue(category);
    }
    public void onPlayListListLoadFinished(SpotifyUtil.PlayListList playlistlist){
        this.playlist.postValue(playlistlist.playlists.items.get(0));
    }
    public void onPlayListTracksLoadFinished(SpotifyUtil.PlayListTracks tracks){
        this.tracks.postValue(tracks.items);
    }
}
