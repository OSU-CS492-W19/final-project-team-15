package com.example.guessthatname.utils;

import android.os.AsyncTask;
import android.util.Pair;

import java.io.IOException;

public class TokenTask extends AsyncTask<Void, Void, String> {

    @Override
    protected String doInBackground(Void... voids) {
        try {
            return NetworkUtils.getToken();
        } catch(IOException e){
            e.printStackTrace();
        }
        return "fail";

    }
    @Override
    protected void onPostExecute(String s) {
        SpotifyUtil.setToken(s);
    }
}