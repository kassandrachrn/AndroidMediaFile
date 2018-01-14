package com.example.kassandra.mediafileapp.activities;


import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;

import com.example.kassandra.mediafileapp.JSONUtility;
import com.example.kassandra.mediafileapp.ListViewAdapter;
import com.example.kassandra.mediafileapp.MediaFile;
import com.example.kassandra.mediafileapp.R;

import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;


public class SearchViewActivity extends AppCompatActivity {

    private ListView listView;
    private ListViewAdapter listViewAdapter;
    String query = null;
    private List<MediaFile> files = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);

        listView = (ListView) findViewById(R.id.list_search_view);

        TextView textViewS = (TextView) findViewById(R.id.textView10);

        Intent intent = getIntent();
        query = intent.getStringExtra("fileName");
        files = intent.getParcelableArrayListExtra("files");

        List<MediaFile> temp = new ArrayList<>();

        for (MediaFile mf : files) {
            if (mf.getFileName().toLowerCase().indexOf(query) != -1) {
                temp.add(mf);
            }
        }

        if (temp.isEmpty()) {
            textViewS.setText("No match found");
        } else {
            textViewS.setText("Results");
        }

        listViewAdapter = new ListViewAdapter(temp, SearchViewActivity.this);
        listView.setAdapter(listViewAdapter);
    }

    public String loadJSON(String fileN) {

        String json = null;
        try {
            InputStream is = getAssets().open(fileN);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}

