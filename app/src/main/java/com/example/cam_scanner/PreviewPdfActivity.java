package com.example.cam_scanner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import com.example.cam_scanner.modal.ImageDocument;

import java.io.IOException;
import java.util.ArrayList;

public class PreviewPdfActivity extends AppCompatActivity {
    Bitmap selectedImageBitmap;

    public static ArrayList<ImageDocument> documents;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_pdf);

        documents = new ArrayList<ImageDocument>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        Intent intent = getIntent();
        String message = intent.getStringExtra("ActivityAction");
        if(message.equals("FileSearch")) {
            performFileSearch();
        } else if(message.equals("CameraActivity")) {
            startActivityCamera();
        }



    }

    private void startActivityCamera() {
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        launchCameraActivity.launch(camera_intent);
    }

    private void performFileSearch() {
        imageChooser();s
    }

    private void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        launchSomeActivity.launch(i);
    }

    ActivityResultLauncher<Intent> launchSomeActivity
            = registerForActivityResult(
            new ActivityResultContracts
                    .StartActivityForResult(),
            result -> {
                if (result.getResultCode()
                        == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null
                            && data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        try {
                            selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> launchCameraActivity
            = registerForActivityResult(
            new ActivityResultContracts
                    .StartActivityForResult(),
            result -> {
                if (result.getResultCode()
                        == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getExtras() != null) {
                        selectedImageBitmap = (Bitmap) data.getExtras().get("data");
                    }
                }
            });
}