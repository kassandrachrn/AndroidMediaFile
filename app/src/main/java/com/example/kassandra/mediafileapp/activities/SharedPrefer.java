package com.example.kassandra.mediafileapp.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.kassandra.mediafileapp.ListViewAdapter;
import com.example.kassandra.mediafileapp.MediaFile;
import com.example.kassandra.mediafileapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPrefer extends AppCompatActivity {

    public static final String mypreference = "favouritesList";
    public static final String JSONPref = "jsonPrefKey";
    List<MediaFile> favourites;
    ListView listPref;
    ListViewAdapter listViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_prefer);

        getSharedFavourites();

        listPref = (ListView) findViewById(R.id.lvSharedPref);
        listViewAdapter = new ListViewAdapter(favourites, SharedPrefer.this);
        listPref.setAdapter(listViewAdapter);
    }

    private void getSharedFavourites() {

        SharedPreferences sharedpref = getApplicationContext().getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String jsonPref = sharedpref.getString(JSONPref, null);
        Type mediaF = new TypeToken<List<MediaFile>>() {
        }.getType();

        favourites = gson.fromJson(jsonPref, mediaF);

        if (favourites == null) {
            favourites = new ArrayList<>();
        }
    }
}

