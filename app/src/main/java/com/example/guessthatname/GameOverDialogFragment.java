package com.example.guessthatname;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class GameOverDialogFragment extends DialogFragment {

    public interface GameOverClickListener{
        public void startNewGame();
    }

    private GameOverClickListener mListener;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            mListener = (GameOverClickListener) context;
        } catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle args = getArguments();
        builder.setMessage(getString(R.string.game_over_message, args.getInt(getString(R.string.score_arg)), args.getInt(getString(R.string.max_score_arg))));
        builder.setNegativeButton(getString(R.string.dialog_button_text), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mListener.startNewGame();
                dismiss();
            }
        });
        return builder.create();
    }
}