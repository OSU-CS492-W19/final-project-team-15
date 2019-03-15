package com.example.guessthatname;

import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.example.guessthatname.R.font.arcade_classic;

public class MainActivity extends AppCompatActivity {
private int score;

private static final String TAG = "GuessThatName";

private TextView mScoreTV;
private Choice[] mChoices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initChoices();

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

    private void initChoices() {
        // Get buttons
        mChoices = new Choice[4];
        mChoices[0] = new Choice((Button)findViewById(R.id.button_0), false);
        mChoices[1] = new Choice((Button)findViewById(R.id.button_1), false);
        mChoices[2] = new Choice((Button)findViewById(R.id.button_2), false);
        mChoices[3] = new Choice((Button)findViewById(R.id.button_3), false);

        // TODO: Send the above statements into the for loop below

        for(int i = 0; i < 4; i++) {
            //Add a touch listener to each of the buttons
            Button b = mChoices[i].getButton();
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
                            ((ColorDrawable)v.getBackground()).setAlpha(100);
                        } else if(action == MotionEvent.ACTION_UP) {
                            //Restore original button opacity
                            ((ColorDrawable)v.getBackground()).setAlpha(255);
                        }
                    } else {
                        //Restore original button opacity
                        ((ColorDrawable)v.getBackground()).setAlpha(255);
                    }

                    return true;
                }

            });
        }
    }

    private void displayResults(boolean correct) {
        if(correct) {
            // TODO: Success view
        } else {
            // TODO: Failure view
        }
    }
}
