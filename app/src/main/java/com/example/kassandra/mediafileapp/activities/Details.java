package com.example.kassandra.mediafileapp.activities;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kassandra.mediafileapp.MediaFile;
import com.example.kassandra.mediafileapp.R;

public class Details extends AppCompatActivity {

    MediaFile fileTaken = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        setTitle("Details");
        Intent intent = getIntent();
        fileTaken = (MediaFile) intent.getParcelableExtra("MediaFile");

        TextView textName = (TextView) findViewById(R.id.file_name_detail);
        textName.setText(fileTaken.getFileName());

        TextView tvSize = (TextView) findViewById(R.id.file_size_detail);
        tvSize.setText(String.valueOf(fileTaken.getFileSize()));

        TextView tvLocation = (TextView) findViewById(R.id.file_loca_detail);
        tvLocation.setText(fileTaken.getLocation());

        Switch permission = (Switch) findViewById(R.id.permit);
        permission.setClickable(false);

        permission.setChecked(fileTaken.isItPrivate());
    }
}
