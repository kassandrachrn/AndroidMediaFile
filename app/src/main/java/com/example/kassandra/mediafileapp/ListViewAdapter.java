package com.example.kassandra.mediafileapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends ArrayAdapter<MediaFile> {

    Context context;
    private List<MediaFile> files = new ArrayList<MediaFile>();
    private List<MediaFile> list = null;


    public ListViewAdapter(List<MediaFile> files, Context context) {
        super(context, 0, files);
    }

    public View getView(final int position, View view, ViewGroup parent) {

        MediaFile mf = getItem(position);
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, null, false);
        }
        TextView name = (TextView) view.findViewById(R.id.list_name_view);
        TextView location = (TextView) view.findViewById(R.id.list_location);

        name.setText(mf.getFileName());
        location.setText(mf.getLocation());

        return view;
    }
}
