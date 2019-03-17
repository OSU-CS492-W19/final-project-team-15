package com.example.guessthatname;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guessthatname.utils.SpotifyUtil;

import java.net.URI;

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

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_fragment,null);
        TextView messagetv = view.findViewById(R.id.dialog_message);
        TextView songnametv = view.findViewById(R.id.song_name);
        TextView pointstv = view.findViewById(R.id.question_points);
        ImageView albumartiv = view.findViewById(R.id.iv_album_art);

        final String url = args.getString(getString(R.string.song_url_arg_key));
        //set correctness message
        if(isCorrect) {
            messagetv.setText(getString(R.string.correct_dialog));
        } else{
            messagetv.setText(R.string.incorrect_dialog);
        }
        //display album art
        if(args.containsKey(getString(R.string.art_arg_url))){
            SpotifyUtil.SpotifyImage img = (SpotifyUtil.SpotifyImage) args.getSerializable(getString(R.string.art_arg_url));
            albumartiv.setMaxHeight(img.height);
            albumartiv.setMaxWidth(img.width);
            ImageUtil.displayImageFromLink(albumartiv,img.url);
        }
        //set correct answer message
        if(args.containsKey(getString(R.string.songname_arg_key))){
            songnametv.setText(getString(R.string.dialog_correct_song_message,args.getString(getString(R.string.songname_arg_key))));
        }
        //set question point value
        if(args.containsKey(getString(R.string.question_points_arg))){
            pointstv.setText(getString(R.string.dialog_point_value_message, args.getInt(getString(R.string.question_points_arg))));
        }
        builder.setView(view);
        //add close button
        builder.setNegativeButton(R.string.dialog_button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });
        //add listen on spotify buttton
        builder.setPositiveButton(getString(R.string.listen_spot), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        return builder.create();
    }
}
