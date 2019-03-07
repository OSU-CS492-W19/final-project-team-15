package com.example.guessthatname;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.example.guessthatname.R.font.arcade_classic;

public class MainActivity extends AppCompatActivity {
private int score;

private TextView mScoreTV;
private Button[] mButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initButtons();

        score = 0;
        mScoreTV = findViewById(R.id.tv_score);
        mScoreTV.setText(getString(R.string.score_pre)+" "+score);
        Typeface typeface = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            typeface = getResources().getFont(arcade_classic);
            mScoreTV.setTypeface(typeface);
        }
    }

    private void updateScore(int popularity){
        score += (100-(popularity/2));
        mScoreTV.setText(getString(R.string.score_pre)+" "+score);
    }

    private void initButtons() {
        // Get buttons
        mButtons = new Button[4];
        mButtons[0] = findViewById(R.id.button_0);
        mButtons[1] = findViewById(R.id.button_1);
        mButtons[2] = findViewById(R.id.button_2);
        mButtons[3] = findViewById(R.id.button_3);

        // TODO: Send the above statements into the for loop below

        for(int i = 0; i < 4; i++) {
            updateButtonText(i, "Empty Button");
            mButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View button) {
                    Log.d("SpotifyMain", "Button clicked");
                    // TODO: Check boolean value associated with button
                    // boolean correct = {get bool from button};
                    // updateScore({get popularity from button});
                    // displayResults(correct);
                }
            });
        }

        // setButtonsVisibility(View.INVISIBLE);
    }

    private void displayResults(boolean correct) {
        if(correct) {
            // TODO: Success view
        } else {
            // TODO: Failure view
        }
    }

    /**
     * Updates button text to reflect song name passed in
     * @param id
     * @param songName
     */
    private void updateButtonText(int id, String songName) {
        mButtons[id].setText(songName);
    }

    /**
     * Sets visibility of all buttons to passed in value
     * @param visibility
     */
    private void setButtonsVisibility(int visibility) {
        for(int i = 0; i < mButtons.length; i++) {
            mButtons[i].setVisibility(visibility);
        }
    }
}
