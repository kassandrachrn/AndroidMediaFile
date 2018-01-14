package com.example.kassandra.mediafileapp;


import android.app.ActivityManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.kassandra.mediafileapp.activities.ListFiles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetJSONFromURL extends AsyncTask<Void, Void, String> {

    String json = "";

    public String stringToPass;
    ResponseListener listener;

    public void setOnResponseListener(ResponseListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Void... voids) {

        try {
            URL url = new URL("http://www.json-generator.com/api/json/get/cgyPXowMSW?indent=2");

            HttpURLConnection hcon = (HttpURLConnection) url.openConnection();
            InputStream is = hcon.getInputStream();

            BufferedReader bufRed = new BufferedReader(new InputStreamReader(is));

            String line = "";
            while (line != null) {
                line = bufRed.readLine();
                json = json + line;
            }
            return json;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String string) {

        super.onPostExecute(string);
        stringToPass = string;
        listener.onResponseReceive(stringToPass);
    }

}
