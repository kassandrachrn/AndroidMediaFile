package com.example.kassandra.mediafileapp.activities;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kassandra.mediafileapp.JSONUtility;
import com.example.kassandra.mediafileapp.ListViewAdapter;
import com.example.kassandra.mediafileapp.MediaFile;
import com.example.kassandra.mediafileapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SearchResult extends AppCompatActivity {

    private ListView listView;
    private ListViewAdapter listViewAdapter;
    private List<MediaFile> files = new ArrayList<>();
    private MediaFile mediaFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        setTitle("Search Results");

        Bundle strings = getIntent().getExtras();
        String[] values = strings.getStringArray("arrayS");
        TextView tv = (TextView) findViewById(R.id.textView6);
        tv.setText("Results for " + values[0]);

        listView = (ListView) findViewById(R.id.listViewResult);

        String myJSON = loadJSON("mediaFiles.json");

        try {
            JSONObject object = new JSONObject(myJSON);
            files = JSONUtility.getListFromJSON(object, "files");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        listViewAdapter = new ListViewAdapter(filter(files, values), this);
        listView.setAdapter(listViewAdapter);
    }

    public List<MediaFile> filter(List<MediaFile> files, String[] values) {

        ArrayList<MediaFile> list = new ArrayList<>();

        for (MediaFile mf : files) {
            if (((mf.getFileName().contains(values[0]) && values[0] != null && !values[0].equals(""))
                    || (mf.getLocation().contains(values[2]) && values[2] != null && !values[2].equals("")))) {
                list.add(mf);
            } else if (list == null) {
                Toast.makeText(SearchResult.this, " No file matches the search", Toast.LENGTH_SHORT).show();
            }
        }
        return list;
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

