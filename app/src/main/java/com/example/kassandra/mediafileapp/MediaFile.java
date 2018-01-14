package com.example.kassandra.mediafileapp;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;


public class MediaFile implements Parcelable {

    int fileID;
    String fileName;
    double fileSize;
    String fileType;
    String location;
    Date dateModified;
    int qualityRate;
    boolean isItPrivate;
    boolean selected;

    public int getFileID() {
        return fileID;
    }

    public void setFileID(int fileID) {
        this.fileID = fileID;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public double getFileSize() {
        return fileSize;
    }

    public void setFileSize(double fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    public int getQualityRate() {
        return qualityRate;
    }

    public void setQualityRate(int qualityRate) {
        this.qualityRate = qualityRate;
    }

    public boolean isItPrivate() {
        return isItPrivate;
    }

    public void setItPrivate(boolean itPrivate) {
        isItPrivate = itPrivate;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public MediaFile(int fileID, String fileName, double fileSize, String fileType, String location) {
        this.fileID = fileID;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.location = location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(fileID);
        out.writeString(fileName);
        out.writeDouble(fileSize);
        out.writeString(location);
        out.writeString(fileType);
        out.writeInt(qualityRate);
        out.writeString(String.valueOf(isItPrivate));
    }

    private MediaFile(Parcel in) {

        fileID = in.readInt();
        fileName = in.readString();
        fileSize = in.readDouble();
        location = in.readString();
        fileType = in.readString();
        qualityRate = in.readInt();
        isItPrivate = Boolean.valueOf(in.readString());
    }

    public static final Parcelable.Creator<MediaFile> CREATOR
            = new Parcelable.Creator<MediaFile>() {

        @Override
        public MediaFile createFromParcel(Parcel in) {
            return new MediaFile(in);
        }

        @Override
        public MediaFile[] newArray(int size) {
            return new MediaFile[size];
        }
    };
}
