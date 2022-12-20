package com.example.cam_scanner;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.cam_scanner.databinding.ActivityMainBinding;

import com.example.cam_scanner.utils.PermissionUtil;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    Bitmap selectedImageBitmap;
    Button camera_open_id;
    Button BSelectImage;
    ImageView IVPreviewImage;
    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private ActivityResultContracts.RequestMultiplePermissions multiplePermissionsContract;
    private ActivityResultLauncher<String[]> multiplePermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_documents)
                .build();
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        multiplePermissionsContract = new ActivityResultContracts.RequestMultiplePermissions();
        multiplePermissionLauncher = registerForActivityResult(multiplePermissionsContract, isGranted -> {
            Log.d("PERMISSIONS", "Launcher result: " + isGranted.toString());
            if (isGranted.containsValue(false)) {
                Log.d("PERMISSIONS", "At least one of the permissions was not granted, launching again...");
                multiplePermissionLauncher.launch(permissions);
            }
        });

        PermissionUtil.getInstance().askPermissions(this, multiplePermissionLauncher, permissions);

        BSelectImage = findViewById(R.id.BSelectImage);
        IVPreviewImage = findViewById(R.id.IVPreviewImage);
        camera_open_id = findViewById(R.id.camera_button);

        // handle the Choose Image button to trigger
        // the image chooser function
        BSelectImage.setOnClickListener(v -> imageChooser());

        camera_open_id.setOnClickListener(v -> {
            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            launchCameraActivity.launch(camera_intent);
        });
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
                        IVPreviewImage.setImageBitmap(
                                selectedImageBitmap);
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
                        IVPreviewImage.setImageBitmap(selectedImageBitmap);
                    }
                }
            });
}
