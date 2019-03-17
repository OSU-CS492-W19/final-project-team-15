package com.example.guessthatname;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import static com.example.guessthatname.R.font.arcade_classic;
import android.view.Menu;
import android.view.MenuItem;

import com.example.guessthatname.data.Status;
import com.example.guessthatname.utils.SpotifyUtil;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
private static final String TAG = "GuessThatName";
private static final String DIALOG_TAG = "dialog";
private static final String SCORE_KEY = "currentScore";
private static final String testLink = "https://www.sageaudio.com/blog/wp-content/uploads/2014/04/album-art-300x300.png";
private static final String testSpotifyUri = "spotify:track:11dFghVXANMlKmJXsNCbNl";

private int score;
private TextView mScoreTV;
private TextView mPlaceholderTV;
private Choice[] mChoices;
private FragmentManager mFragmentManager;
private SharedPreferences mPreferences;
private SharedPreferences.OnSharedPreferenceChangeListener mPreferencesListener;
private MediaPlayer mMediaPlayer;
private ProgressBar mLoadingIndicatorPB;
private TextView mLoadingErrorMessageTV;
private GameViewModel mGameViewModel;
private Boolean mCanPlayMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentManager = getSupportFragmentManager();
        mMediaPlayer = new MediaPlayer();
        mCanPlayMusic = false;

        mPlaceholderTV = findViewById(R.id.tv_album_art_placeholder);
        mPlaceholderTV.setText("?");
        mPlaceholderTV.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (mMediaPlayer.isPlaying()){
                    mMediaPlayer.stop();
                }
                try{
                    mMediaPlayer.prepare(); // might take long! (for buffering, etc)
                } catch (IOException e){
                    e.printStackTrace();
                }
                mMediaPlayer.start();
            }
        });

        mLoadingErrorMessageTV = findViewById(R.id.tv_loading_error_message);

        mLoadingIndicatorPB = findViewById(R.id.pb_loading_indicator);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SCORE_KEY)) {
                score = savedInstanceState.getInt(SCORE_KEY);
            } else {
                score = 0;
            }
        }
        initChoices();

        mScoreTV = findViewById(R.id.tv_score);
        mScoreTV.setText(getString(R.string.score_pre)+" "+score);

        Typeface typeface = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            typeface = getResources().getFont(arcade_classic);
            mScoreTV.setTypeface(typeface);
        }


        mGameViewModel = ViewModelProviders.of(this).get(GameViewModel.class);

        mGameViewModel.getCategory().observe(this, new Observer<SpotifyUtil.Category>() {
            @Override
            public void onChanged(@Nullable SpotifyUtil.Category category) {
                Log.d("DEBUG", "CATEGORY HAS CHANGED");
                mGameViewModel.loadPlaylist();
            }
        });
        mGameViewModel.getPlaylist().observe(this, new Observer<SpotifyUtil.Playlist>() {
            @Override
            public void onChanged(@Nullable SpotifyUtil.Playlist playlist) {
                mGameViewModel.loadTracks();
            }
        });
        mGameViewModel.getTracks().observe(this, new Observer<ArrayList<SpotifyUtil.PlayListTrack>>() {
            @Override
            public void onChanged(@Nullable ArrayList<SpotifyUtil.PlayListTrack> tracks) {
                startGame();
            }
        });

        mGameViewModel.getLoadingStatus().observe(this, new Observer<Status>() {
            @Override
            public void onChanged(@Nullable Status status) {
                if(status == Status.LOADING) {
                    showLoadingScreen(true);
                } else if (status == Status.SUCCESS) {
                    showLoadingScreen(false);
                } else {
                    showLoadingScreen(true);
                    mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
                    mLoadingErrorMessageTV.setVisibility(View.VISIBLE);
                }
            }
        });

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mPreferencesListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                mGameViewModel.clearRepository();
                mGameViewModel.loadCategory(mPreferences.getString("genre_key", "___default___"));
            }
        };
        mPreferences.registerOnSharedPreferenceChangeListener(mPreferencesListener);

        mGameViewModel.loadCategory(mPreferences.getString("genre_key", "___default___"));

    }

    public void startGame(){
        // TODO: start the game
        mMediaPlayer = new MediaPlayer();
        playSongFromUrl("https://p.scdn.co/mp3-preview/3eb16018c2a700240e9dfb8817b6f2d041f15eb1?cid=774b29d4f13844c495f206cafdad9c86");
        showLoadingScreen(false);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mMediaPlayer != null && !mMediaPlayer.isPlaying() && mCanPlayMusic){
            try{
                mMediaPlayer.prepare(); // might take long! (for buffering, etc)
            } catch (IOException e){
                e.printStackTrace();
            }
            mMediaPlayer.start();
        }
    }

    /**
     * Sets all items on the main activity to either visible or visible
     * depending on bool value passed in. Displays loading icon on true.
     * @param show
     */
    public void showLoadingScreen(Boolean show) {
        int vis1, vis2;
        if(show) {
            vis1 = View.INVISIBLE;
            vis2 = View.VISIBLE;
        } else {
            vis1 = View.VISIBLE;
            vis2 = View.INVISIBLE;
        }

        //Score
        mScoreTV.setVisibility(vis1);
        // Album art
        mPlaceholderTV.setVisibility(vis1);
        // Buttons
        for(int i = 0; i < 4; i++) {
            mChoices[i].setVisibility(vis1);
        }

        // Loading indicator
        mLoadingIndicatorPB.setVisibility(vis2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_settings:
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (score > 0) {
            outState.putInt(SCORE_KEY, score);
        }
    }

    private void updateScore(int popularity) {
        score += (100 - (popularity / 2));
        mScoreTV.setText(getString(R.string.score_pre)+" "+score);
    }

    private void initChoices() {
        // Get buttons
        mChoices = new Choice[4];
        mChoices[0] = new Choice((Button) findViewById(R.id.button_0), false);
        mChoices[1] = new Choice((Button) findViewById(R.id.button_1), false);
        mChoices[2] = new Choice((Button) findViewById(R.id.button_2), false);
        mChoices[3] = new Choice((Button) findViewById(R.id.button_3), false);

        // TODO: Send the above statements into the for loop below

        for (int i = 0; i < 4; i++) {
            // Add a touch listener to each of the buttons
            Button b = mChoices[i].getButton();
            b.setTag(i);
            b.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    float x = event.getX();
                    float y = event.getY();
                    float height = (float)v.getHeight();
                    float width = (float)v.getWidth();

                    //Check if the touch event is within the bounds of the button layout
                    if((0 < x && x < width) && (0 < y && y < height)) {
                        if(action == MotionEvent.ACTION_DOWN) {
                            //Fade the button when pressed
                            v.getBackground().setAlpha(100);
                        } else if(action == MotionEvent.ACTION_UP) {
                            //Restore original button opacity
                            v.getBackground().setAlpha(255);
                            displayResults(mChoices[(Integer)v.getTag()].getCorrect());
                        }
                    } else {
                        //Restore original button opacity
                        v.getBackground().setAlpha(255);
                    }

                    return true;
                }

            });
        }
    }

    /**
     * Gets a list of 4 songs and an associated boolean value indicating correctness
     * of the song choice. Updates the button UI elements from argument data
     * 
     * @param songs
     */
    public void updateChoices(Pair<String, Boolean> songs[]) {
        for (int i = 0; i < 4; i++) {
            mChoices[i].updateText(songs[i].first);
            mChoices[i].setCorrectness(songs[i].second);
        }
    }

    private void displayResults(boolean correct) {
        Log.d(TAG, "Correct song? : " + correct);

        //point value for dialog, TODO replace with track.popularity
        int pop = 70;
        int pts = 100 - (pop / 2);
        Bundle args = new Bundle();
            //boolean representing whether answer is correct
            args.putBoolean(getString(R.string.answer_arg_key),correct);
            //correct song name
            args.putString(getString(R.string.songname_arg_key),"Darude - Sandstorm");
            //question point value
            args.putInt(getString(R.string.question_points_arg),pts);
            //spotify url for song
            args.putString(getString(R.string.song_url_arg_key),testSpotifyUri);
            //url for album art
            args.putString(getString(R.string.art_arg_url), testLink);

            //create dialog fragment
            DialogFragment mDialog = new AnswerDialogFragment();
            //pass arguments
            mDialog.setArguments(args);
            //display modal
            mDialog.show(mFragmentManager, DIALOG_TAG);
    }

    public void playSongFromUrl(String url){
        Log.d("Main Activity", "Streaming music");
        if(!mMediaPlayer.isPlaying()){
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mMediaPlayer.setDataSource(url);
            } catch (IOException e){
                e.printStackTrace();
            }
            try{
                mMediaPlayer.prepare(); // might take long! (for buffering, etc)
            } catch (IOException e){
                e.printStackTrace();
            }
            mCanPlayMusic = true;
            mMediaPlayer.start();
        }
    }

    public void togglePlayingPreview() {
        Log.d("Main Activity", "Toggling playing preview");
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }else{
            try{
                mMediaPlayer.prepare(); // might take long! (for buffering, etc)
            } catch (IOException e){
                e.printStackTrace();
            }
            mMediaPlayer.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
        }
    }
}
