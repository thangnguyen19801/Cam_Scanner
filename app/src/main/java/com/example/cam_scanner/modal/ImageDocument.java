package com.example.cam_scanner.modal;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import androidx.customview.widget.Openable;

import java.io.File;

public class ImageDocument {
    private Uri imageUri;
    String fileName;
    long fileSize;

    public ImageDocument(Uri imageUri, Context context) {
        this.imageUri = imageUri;
        String scheme = imageUri.getScheme();
        try {
            if(scheme.equals("file")) {
                fileName = imageUri.getLastPathSegment();
                File file = new File(imageUri.getPath());
                fileSize =file.length();
            } else if(scheme.equals("content")) {
                Cursor cursor = context.getContentResolver().query(imageUri, null, null, null, null);
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                cursor.moveToFirst();
                fileName = cursor.getString(nameIndex);
                fileSize = cursor.getLong(sizeIndex);
            }
        } catch (Exception e) {
            fileName = "No name";
        }
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }
}
