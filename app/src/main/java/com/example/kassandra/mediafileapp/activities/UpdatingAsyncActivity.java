package com.example.kassandra.mediafileapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.kassandra.mediafileapp.ListViewAdapter;
import com.example.kassandra.mediafileapp.MediaFile;
import com.example.kassandra.mediafileapp.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UpdatingAsyncActivity extends AppCompatActivity {

    private final static String TAG = "UpdatingActivity";
    private ProgressBar progressBar;
    private ProgressDialog progressDialog = null;
    List<MediaFile> list = new ArrayList<>();
    MediaFile fileToRemove;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updating_async);

        progressBar = (ProgressBar) findViewById(R.id.progressBar3);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        fileToRemove = extras.getParcelable("fileToRemove");
        list = extras.getParcelableArrayList("list");

        UpdateTask updateTask = new UpdateTask();
        updateTask.execute(list);
    }

    class UpdateTask extends AsyncTask<List<MediaFile>, Integer, List<MediaFile>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(UpdatingAsyncActivity.this);
            progressDialog.setMessage("Updating list...");
            progressDialog.show();
        }

        @Override
        protected List<MediaFile> doInBackground(List<MediaFile>[] arrayLists) {

            Iterator<MediaFile> iterator = list.iterator();
            while (iterator.hasNext()) {
                MediaFile file = iterator.next();
                if (file.getFileName().equals(fileToRemove.getFileName())) {
                    iterator.remove();
                }
            }

            for (int i = 0; i <= 100; i += 10) {
                publishProgress(i);
            }
            return list;
        }

        @Override
        protected void onPostExecute(final List<MediaFile> mediaFiles) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final ListView listView = (ListView) findViewById(R.id.listView_updated);
                    listView.setAdapter(new ListViewAdapter(list, UpdatingAsyncActivity.this));
                }
            });
            progressDialog.dismiss();

        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }

    }
}

