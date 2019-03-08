package com.example.guessthatname;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import static com.example.guessthatname.R.font.arcade_classic;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences mPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener mPreferencesListener;
    private int score;
    private TextView mScoreTV;

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

        score = 0;
        mScoreTV = findViewById(R.id.tv_score);
        mScoreTV.setText(getString(R.string.score_pre) + " " + score);
        Typeface typeface = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            typeface = getResources().getFont(arcade_classic);
            mScoreTV.setTypeface(typeface);
        }
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

    private void updateScore(int popularity) {
        score += (100 - (popularity / 2));
        mScoreTV.setText(getString(R.string.score_pre) + " " + score);
    }
}
