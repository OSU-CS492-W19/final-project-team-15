package com.example.guessthatname;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;
import static com.example.guessthatname.R.font.arcade_classic;

import java.io.IOException;
import java.util.List;

import static com.example.guessthatname.R.font.arcade_classic;

import com.example.guessthatname.utils.SpotifyUtil;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences mPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener mPreferencesListener;
    private int score;
    private Choice[] mChoices;
    private TextView mScoreTV;
    private ImageView mAlbumArtIV;
    private static final String TAG = "GuessThatName";
    private static final String SCORE_KEY = "currentScore";
    private MediaPlayer mMediaPlayer;
    private static final String testLink = "https://www.sageaudio.com/blog/wp-content/uploads/2014/04/album-art-300x300.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mPreferencesListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                // do something when prefs changed
            }
        };
        mPreferences.registerOnSharedPreferenceChangeListener(mPreferencesListener);
        mAlbumArtIV = findViewById(R.id.iv_album_art);

        ImageUtil.displayImageFromLink(mAlbumArtIV, testLink);
        mAlbumArtIV.setVisibility(View.VISIBLE);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SCORE_KEY)) {
                score = savedInstanceState.getInt(SCORE_KEY);
            } else {
                score = 0;
            }
        }
        initChoices();

        mScoreTV = findViewById(R.id.tv_score);
        mScoreTV.setText(getString(R.string.score_pre) + " " + score);

        Typeface typeface = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            typeface = getResources().getFont(arcade_classic);
            mScoreTV.setTypeface(typeface);
        }
        mMediaPlayer = new MediaPlayer();
        playSongFromUrl("https://p.scdn.co/mp3-preview/3eb16018c2a700240e9dfb8817b6f2d041f15eb1?cid=774b29d4f13844c495f206cafdad9c86");
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
        mScoreTV.setText(getString(R.string.score_pre) + " " + score);
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
                    float height = (float) v.getHeight();
                    float width = (float) v.getWidth();

                    // Check if the touch event is within the bounds of the button layout
                    if ((0 < x && x < width) && (0 < y && y < height)) {
                        if (action == MotionEvent.ACTION_DOWN) {
                            // Fade the button when pressed
                            ((ColorDrawable) v.getBackground()).setAlpha(100);
                        } else if (action == MotionEvent.ACTION_UP) {
                            // Restore original button opacity
                            ((ColorDrawable) v.getBackground()).setAlpha(255);
                            displayResults(mChoices[(Integer) v.getTag()].getCorrect());
                        }
                    } else {
                        // Restore original button opacity
                        ((ColorDrawable) v.getBackground()).setAlpha(255);
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
        if (correct) {
            // TODO: Success view
        } else {
            // TODO: Failure view
        }
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
            mMediaPlayer.start();
        }
    }

    public void playStoppedPreview(){
        if (!mMediaPlayer.isPlaying()){
            mMediaPlayer.start();
        }
    }

    public void pausePlayingPreview() {
        Log.d("Main Activity", "Pausing playing preview");
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }

    public void stopPlayingPreview(){
        Log.d("Main Activity", "Stopping playing preview");
        if (mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
        }
    }
}
