package com.example.kassandra.mediafileapp.activities;


import android.app.DatePickerDialog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.kassandra.mediafileapp.R;

import java.util.Calendar;


public class FilterSearch extends AppCompatActivity {

    private ProgressDialog progressDialog = null;
    String[] stringArr = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        setTitle("Filter Search");

        SeekBar sbSize = (SeekBar) findViewById(R.id.seekBar);
        int seekValue = sbSize.getProgress();

        final TextView sbValue = (TextView) findViewById(R.id.seekbarValue);

        sbSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                sbValue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void pickDate(View view) {

        Calendar c = Calendar.getInstance();
        int startYear = c.get(Calendar.YEAR);
        int startMonth = c.get(Calendar.MONTH);
        int startDay = c.get(Calendar.DAY_OF_MONTH);
        final EditText date = (EditText) findViewById(R.id.datePicker);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int month, int day) {

                        date.setText(year + "/" + (month + 1) + "/" + day);

                    }
                }, startYear, startMonth, startDay);
        datePickerDialog.show();
    }

    public void searchFile(View view) {

        EditText fileN = (EditText) findViewById(R.id.file_n);
        String fileName = fileN.getText().toString();

        SeekBar seekSize = (SeekBar) findViewById(R.id.seekBar);
        int fileSize = seekSize.getProgress();

        TextView fileLoc = (TextView) findViewById(R.id.file_loc);
        String fileLocation = fileLoc.getText().toString();

        stringArr = new String[]{fileName, String.valueOf(fileSize), fileLocation};

        SearchTask searchTask = new SearchTask();
        searchTask.execute(stringArr);
    }

    private class SearchTask extends AsyncTask<String[], Integer, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(FilterSearch.this);
            progressDialog.setMessage("Searching...");
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String[] doInBackground(String[]... strings) {

            Intent intent = new Intent(FilterSearch.this, SearchResult.class);
            intent.putExtra("arrayS", stringArr);
            startActivity(intent);

            return stringArr;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            progressDialog.dismiss();
        }
    }
}
