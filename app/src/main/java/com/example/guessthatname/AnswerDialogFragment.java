package com.example.guessthatname;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class AnswerDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //get creation arguments
        Bundle args = getArguments();
        boolean isCorrect = false;
        //check arguments exist and contain info about guess correctness
        if(args != null && args.containsKey(getString(R.string.answer_arg_key))) {
            isCorrect = args.getBoolean(getString(R.string.answer_arg_key));
        }
        //build dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //set correctness message
        if(isCorrect) {
            builder.setMessage(R.string.correct_dialog);
        } else{
            builder.setMessage(R.string.incorrect_dialog);
        }
        //add close button
        builder.setPositiveButton(R.string.dialog_button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });
        builder.setView(R.layout.dialog_fragment);
        return builder.create();
    }
}
