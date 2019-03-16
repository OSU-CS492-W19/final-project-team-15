package com.example.guessthatname.utils;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.util.Xml;

import java.io.IOException;
import java.util.prefs.BackingStoreException;

import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkUtils {

    private static final OkHttpClient mHTTPClient = new OkHttpClient();
    public static final String client_secret = "61f03839c7eb427285916862a226058f";
    public static final String client_id = "550e72b9edba4d4a96e4f903436e1130";

    public static String doHTTPGet(String url, String token) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + token)
                .build();
        Response response = mHTTPClient.newCall(request).execute();

        try {
            return response.body().string();
        } finally {
            response.close();
        }
    }
    public static String getToken() throws IOException {
        byte[] clientsecret = (client_id + ":" + client_secret).getBytes();
        int flags = Base64.NO_WRAP | Base64.URL_SAFE | Base64.NO_PADDING;
        String encodedSecret = Base64.encodeToString(clientsecret, flags);
        Log.d("SpotifyUtil", "encoded secret: " + encodedSecret);
        RequestBody requestBody = new FormBody.Builder()
                .addEncoded("grant_type", "client_credentials")
                .build();
        Request request = new Request.Builder()
                .url("https://accounts.spotify.com/api/token")
                .post(requestBody)
                .addHeader("Authorization", "Basic " + encodedSecret)
                .build();
        Response response = mHTTPClient.newCall(request).execute();

        Log.d("SpotifyUtil", "successful request: " + response.isSuccessful());
        try {
            return response.body().string();
        } finally {
            response.close();
        }
    }
}
