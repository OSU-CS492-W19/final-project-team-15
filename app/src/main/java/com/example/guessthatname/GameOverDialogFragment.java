package com.example.guessthatname;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class GameOverDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle args = getArguments();
        builder.setMessage(getString(R.string.game_over_message, args.getInt(getString(R.string.score_arg)), args.getInt(getString(R.string.max_score_arg))));
        builder.setNegativeButton(getString(R.string.dialog_button_text), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });
        return builder.create();
    }
}