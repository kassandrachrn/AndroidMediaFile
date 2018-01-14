package com.example.kassandra.mediafileapp.activities;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.kassandra.mediafileapp.GetJSONFromURL;
import com.example.kassandra.mediafileapp.GridViewAdapter;
import com.example.kassandra.mediafileapp.JSONUtility;
import com.example.kassandra.mediafileapp.MediaFile;
import com.example.kassandra.mediafileapp.R;
import com.example.kassandra.mediafileapp.ResponseListener;
import com.example.kassandra.mediafileapp.SQLiteDatabaseHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import static com.example.kassandra.mediafileapp.JSONUtility.getListFromJSON;

public class ListFiles extends AppCompatActivity {

    private List<MediaFile> files = new ArrayList<>();
    private Menu menu;
    private GridView gridView;
    private GridViewAdapter gridViewAdapter;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private MediaFile fileSelected;
    private List<MediaFile> favourites = new ArrayList<>();
    String myJSONfromURL;
    public static final String mypreference = "favouritesList";
    public static final String JSONPref = "jsonPrefKey";
    List<MediaFile> mediaFromJSON;
    JSONObject jsonObjectURL = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_files);
        setTitle("Media Files");

        getSharedFavourites();

        String myJSON = loadJSON("mediaFiles.json");
        try {
            JSONObject object = new JSONObject(myJSON);
            files = getListFromJSON(object, "files");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        gridView = (GridView) findViewById(R.id.gridViewFiles);

        gridViewAdapter = new GridViewAdapter(files, this);
        gridView.setAdapter(gridViewAdapter);

        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, final int position, long arg3) {

                fileSelected = (MediaFile) arg0.getItemAtPosition(position);

                if (fileSelected.isSelected()) {
                    fileSelected.setSelected(false);
                    gridView.setItemChecked(position, false);
                } else {
                    fileSelected.setSelected(true);
                    gridView.setItemChecked(position, true);
                }

                radioButton1 = (RadioButton) findViewById(R.id.rad1);
                radioButton2 = (RadioButton) findViewById(R.id.rad2);
                radioButton3 = (RadioButton) findViewById(R.id.rad3);


                for (MediaFile file : files) {
                    if (file.getFileName().equals(fileSelected.getFileName())) {
                        file.setSelected(fileSelected.isSelected());
                    }
                }

                if (fileSelected.isSelected()) {

                    radioButton1.setClickable(true);
                    radioButton2.setClickable(true);
                    radioButton3.setClickable(true);

                    radioButton1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            favourites.add(fileSelected);
                            Toast.makeText(ListFiles.this,
                                    fileSelected.getFileName() + " added to Favourites",
                                    Toast.LENGTH_SHORT).show();
                            gridView.setItemChecked(position, false);
                            gridView.setAdapter(gridViewAdapter);
                        }
                    });

                    radioButton3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            delete().show();
                            gridView.setItemChecked(position, false);

                        }
                    });

                    gridViewAdapter.notifyDataSetChanged();
                }
                gridView.setAdapter(gridViewAdapter);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.details_tab: {
                if (fileSelected == null) {
                    Toast.makeText(this, "Please choose a file", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    Intent intent = new Intent(this, Details.class);
                    intent.putExtra("MediaFile", (Parcelable) fileSelected);
                    startActivity(intent);

                    fileSelected.setSelected(false);

                    break;
                }
            }
            case R.id.settings_tab: {
                Intent intent = new Intent(ListFiles.this, SettingsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.filter_search_tab: {
                Intent intent = new Intent(ListFiles.this, FilterSearch.class);
                startActivity(intent);
                break;
            }
            case R.id.gallery_tab: {
                Intent intent = new Intent(this, ImagePreview.class);
                startActivity(intent);
                break;
            }
            case R.id.favourites: {
                setSharedFavourites();
                Intent intent = new Intent(this, SharedPrefer.class);
                startActivity(intent);
                break;
            }
            case R.id.store_in_db: {
                storeInDB();
                break;
            }
            case R.id.maps_fct: {
                Intent intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
                break;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tab_menu, menu);


        getMenuInflater().inflate(R.menu.search_menu, menu);
        this.menu = menu;

        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();

        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                Intent intent = new Intent(ListFiles.this, SearchViewActivity.class);
                intent.putExtra("fileName", query);
                intent.putParcelableArrayListExtra("files", (ArrayList<MediaFile>) files);
                startActivity(intent);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                return false;
            }
        });
        return true;
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


    private AlertDialog delete() {

        AlertDialog alertDialogBuilder = new AlertDialog.Builder(this)
                .setTitle("Your call")
                .setMessage("Delete?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Bundle extras = new Bundle();
                        extras.putParcelable("fileToRemove", fileSelected);
                        extras.putParcelableArrayList("list", (ArrayList<MediaFile>) files);

                        Intent intent = new Intent(ListFiles.this, UpdatingAsyncActivity.class);
                        intent.putExtras(extras);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .create();

        return alertDialogBuilder;
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

    private void setSharedFavourites() {

        SharedPreferences sharedpref = getApplicationContext().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedpref.edit();
        Gson gson = new Gson();

        String jsonPref = gson.toJson(favourites);
        prefEditor.clear();
        prefEditor.putString(JSONPref, jsonPref);
        prefEditor.apply();
    }

    private void storeInDB() {

        GetJSONFromURL getJSON = new GetJSONFromURL();
        getJSON.execute();
        Log.d("Async", "Async started");

        mediaFromJSON = new ArrayList<>();
        getJSON.setOnResponseListener(new ResponseListener() {
            @Override
            public void onResponseReceive(String json) {
                try {
                    myJSONfromURL = json;
                    jsonObjectURL = new JSONObject(myJSONfromURL);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mediaFromJSON = getListFromJSON(jsonObjectURL, "files");
                SQLiteDatabaseHandler sqlDBHand = new SQLiteDatabaseHandler(ListFiles.this);

                for (MediaFile mf : mediaFromJSON) {
                    sqlDBHand.addFileToDB(mf);
                }

                List<MediaFile> filesFromDB = sqlDBHand.getFilesListDB();
                for (MediaFile mf : filesFromDB) {
                    Log.d("db", " File " + mf.getFileName());
                }
            }
        });
    }
}
