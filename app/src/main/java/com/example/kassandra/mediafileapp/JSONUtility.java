package com.example.kassandra.mediafileapp;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class JSONUtility {

    private static MediaFile mediaFile;

    public static List<MediaFile> getListFromJSON(JSONObject object, String listName) {

        List<MediaFile> filesTaken = new ArrayList<>();
        JSONArray filesJSON = object.optJSONArray(listName);
        int length = filesJSON.length();

        for (int i = 0; i < filesJSON.length(); i++) {

            JSONObject jsonObject = null;
            try {
                jsonObject = filesJSON.getJSONObject(i);

                Integer fileID = jsonObject.getInt("fileID");
                String fileName = jsonObject.getString("fileName");
                Double fileSize = jsonObject.getDouble("fileSize");
                String fileType = jsonObject.getString("fileType");
                String location = jsonObject.getString("location");
                Integer qualityRate = jsonObject.optInt("qualityRate");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date dateModified = dateFormat.parse(jsonObject.optString("dateModified"));
                Boolean filePrivate = jsonObject.getBoolean("isPrivate");
                Boolean selected = jsonObject.optBoolean("selected");

                mediaFile = new MediaFile(fileID, fileName, fileSize, fileType, location);
                mediaFile.setQualityRate(qualityRate);
                mediaFile.setItPrivate(filePrivate);

                if (dateModified != null) {
                    mediaFile.setDateModified(dateModified);
                }

                filesTaken.add(mediaFile);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        }
        return filesTaken;
    }
}
