package com.app.avy.network;

import android.os.AsyncTask;
import okhttp3.*;

import java.io.IOException;

public class HttpGetRequest extends AsyncTask<String, Void, String> {
    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

    @Override
    protected String doInBackground(String... strings) {
        RequestBody body = RequestBody.create(JSON, strings[1]);
        Request request = new Request.Builder()
                .url(strings[0])
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}