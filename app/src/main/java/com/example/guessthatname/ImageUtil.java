package com.example.guessthatname;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageUtil {

    public static void displayImageFromLink(ImageView imageView, String url){
        Glide.with(imageView)
                .load(url)
                .into(imageView);
    }

}
