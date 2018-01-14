package com.example.kassandra.mediafileapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.w3c.dom.ProcessingInstruction;

import java.util.ArrayList;
import java.util.List;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {

    private static final int DB_VERSION = 2;
    private static final String DB_NAME = "media_files";
    private static final String TABLE_FILES = "files";
    private static final String TABLE_FOLDERS = "folders";

    private static final String KEY_NAME = "fileName";
    private static final String KEY_LOCATION = "fileLocation";
    private static final String KEY_SIZE = "fileSize";
    private static final String KEY_TYPE = "fileType";
    private static final String KEY_ID = "id";
    private static final String KEY_FOLDER_ID = "folderID";
    private static final String FOLDER_NAME = "folderName";
    private static final String FOLDER_PATH = "folderPath";

    public SQLiteDatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_FILES = "CREATE TABLE " + TABLE_FILES + "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_NAME + " TEXT, " + KEY_SIZE + " REAL, " +
                KEY_LOCATION + " TEXT, " + KEY_TYPE + " TEXT, " +
                KEY_FOLDER_ID + " INTEGER REFERENCES " +
                TABLE_FOLDERS + " );";

        String CREATE_TABLE_FOLDERS = "CREATE TABLE " + TABLE_FOLDERS + "(" +
                KEY_FOLDER_ID + " INTEGER PRIMARY KEY," +
                FOLDER_NAME + " TEXT NOT NULL," + FOLDER_PATH + " TEXT NOT NULL);";

        db.execSQL(CREATE_TABLE_FOLDERS);
        db.execSQL(CREATE_TABLE_FILES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOLDERS);
        onCreate(db);
    }

    public void addFileToDB(MediaFile mf) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues conVal = new ContentValues();
        conVal.put(KEY_ID, mf.getFileID());
        conVal.put(KEY_NAME, mf.getFileName());
        conVal.put(KEY_SIZE, mf.getFileSize());
        conVal.put(KEY_LOCATION, mf.getLocation());
        conVal.put(KEY_TYPE, mf.getFileType());

        db.insert(TABLE_FILES, null, conVal);
        db.close();

        Log.d("db ", "files inserted");

    }

    public MediaFile getFileFromDB(int id) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FILES, new String[]{KEY_ID, KEY_NAME, KEY_SIZE, KEY_LOCATION, KEY_TYPE},
                " id = ?", new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();

            MediaFile mf = new MediaFile(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                    Double.parseDouble(cursor.getString(2)),
                    cursor.getString(3), cursor.getString(4));

            cursor.close();
            return mf;
        }
        return null;
    }

    public List<MediaFile> getFilesListDB() {

        List<MediaFile> filesDB = new ArrayList<>();

        String querySelect = "SELECT * FROM " + TABLE_FILES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(querySelect, null);

        if (cursor.moveToFirst()) {
            do {
                MediaFile mf = new MediaFile(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                        Double.parseDouble(cursor.getString(2)),
                        cursor.getString(3), cursor.getString(4));

                filesDB.add(mf);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return filesDB;
    }
}
