package com.example.kassandra.mediafileapp.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.kassandra.mediafileapp.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImagePreview extends AppCompatActivity {

    private ImageView phototaken;
    private String photoPath;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int RESULT_LOAD_IMAGE = 1;
    Bitmap photo;
    RadioButton blacknwhite;
    RadioButton savePic;
    static final int MY_PERMISSION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        takePicture();

        phototaken = findViewById(R.id.photo_taken);
        blacknwhite = findViewById(R.id.rbBlackWhite);
        savePic = findViewById(R.id.rbSave);


        blacknwhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photo = processingBitmap(photo);
                phototaken.setImageBitmap(photo);
            }
        });

        savePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(ImagePreview.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(ImagePreview.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                        Toast.makeText(ImagePreview.this, " App needs permission to store files ", Toast.LENGTH_SHORT).show();

                    } else {

                        ActivityCompat.requestPermissions(ImagePreview.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSION);
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveImage();

                } else {
                    Toast.makeText(ImagePreview.this, "Permission to save file not granted", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK & null != data) {

                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                    try {
                        photo = (Bitmap) data.getExtras().get("data");
                        phototaken.setImageBitmap(photo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    private void saveImage() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HH").format(new Date());
        String imageTitle = "image_" + timeStamp + ".jpg";

        View content = findViewById(R.id.photo_taken);
        content.setDrawingCacheEnabled(true);
        Bitmap bitmap = content.getDrawingCache();

        File root = Environment.getExternalStorageDirectory();
        File cachePath = new File(root.getAbsolutePath() + "/DCIM/" + imageTitle);

        try {
            cachePath.createNewFile();
            FileOutputStream ostream = new FileOutputStream(cachePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
            ostream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (bitmap != null) {
            Toast.makeText(ImagePreview.this, " Picture saved in "
                    + cachePath.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap processingBitmap(Bitmap src) {

        Bitmap dest = Bitmap.createBitmap(
                src.getWidth(), src.getHeight(), src.getConfig());

        for (int x = 0; x < src.getWidth(); x++) {
            for (int y = 0; y < src.getHeight(); y++) {
                int pixelColor = src.getPixel(x, y);
                int pixelAlpha = Color.alpha(pixelColor);
                int pixelRed = Color.red(pixelColor);
                int pixelGreen = Color.green(pixelColor);
                int pixelBlue = Color.blue(pixelColor);

                int pixelBW = (pixelRed + pixelGreen + pixelBlue) / 3;
                int newPixel = Color.argb(
                        pixelAlpha, pixelBW, pixelBW, pixelBW);

                dest.setPixel(x, y, newPixel);
            }
        }

        return dest;
    }
}
