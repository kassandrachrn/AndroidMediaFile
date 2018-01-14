package com.example.kassandra.mediafileapp;


import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GridViewAdapter extends BaseAdapter {

    private List<MediaFile> files;
    private Context context;
    private LayoutInflater layoutInflater;

    public GridViewAdapter(List<MediaFile> files, Context context) {
        this.context = context;
        this.files = files;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return files.size();
    }

    @Override
    public Object getItem(int position) {//the pos
        return files.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ItemHolder holder;
        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.grid_item, parent, false);
            holder = new ItemHolder();

            ImageView icon = (ImageView) convertView.findViewById(R.id.file_icon);
            TextView name = (TextView) convertView.findViewById(R.id.file_grid);

            holder.fileIcon = icon;
            holder.fileName = name;

            convertView.setTag(holder);
        } else {
            holder = (ItemHolder) convertView.getTag();
        }

        MediaFile file = files.get(position);


        holder.fileName.setText(file.getFileName());
        holder.fileIcon.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.id.file_icon));
        String number = "image" + (position + 1);
        holder.fileIcon.setImageResource(context.getResources().
                getIdentifier(number, "drawable", context.getPackageName()));

        holder.check = (CheckBox) convertView.findViewById(R.id.check);

        if (file.isSelected()) {
            holder.fileName.setBackgroundColor(Color.LTGRAY);
            holder.check.setChecked(true);
        } else {
            holder.fileName.setBackgroundColor(Color.WHITE);
            holder.check.setChecked(false);
        }

        return convertView;
    }

    private static class ItemHolder {
        public ImageView fileIcon;
        public TextView fileName;
        public CheckBox check;
    }

}
