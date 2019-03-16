package com.example.guessthatname;

import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Choice {
    private Button mButton;
    private boolean mCorrect;

    public Choice(Button b, boolean c) {
        mButton = b;
        mCorrect = c;
    }

    public void setCorrectness(boolean b) {
        mCorrect = b;
    }

    public void setButton(Button b) {
        mButton = b;
    }

    public Button getButton() {
        return mButton;
    }

    public boolean getCorrect() {
        return mCorrect;
    }

    public void updateText(String songName) {
        mButton.setText(songName);
    }

    public void setVisibility(int visibility) {
        mButton.setVisibility(visibility);
    }
}