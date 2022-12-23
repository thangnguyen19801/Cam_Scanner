package com.example.cam_scanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.cam_scanner.modal.ImageDocument;

import java.util.ArrayList;

public class PreviewPdfActivity extends AppCompatActivity {
    public static ArrayList<ImageDocument> documents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_pdf);
    }

    public void convertToPdf() {
        if(documents.size() < 1) {
            Toast.makeText(this, "Add at least 1 image file", Toast.LENGTH_SHORT).show();
        } else {

        }
    }
}