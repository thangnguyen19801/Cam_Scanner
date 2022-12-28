package com.example.cam_scanner.utils;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;


import static androidx.core.app.ActivityCompat.requestPermissions;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.cam_scanner.MainActivity;

public class PermissionUtil {


    private static PermissionUtil INSTANCE = null;

    private PermissionUtil() {

    }

    public static PermissionUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PermissionUtil();
        }
        return INSTANCE;
    }

    public void askPermissions(Context context, ActivityResultLauncher<String[]> multiplePermissionLauncher, String[] permissions) {
        if(!hasPermissions(context, permissions)) {
            Log.d("PERMISSIONS", "Launching multiple contract permission launcher for ALL required permissions");
            multiplePermissionLauncher.launch(permissions);
        }
        else {
            Log.d("PERMISSIONS", "All permissions are already granted");
        }
    }

    private boolean hasPermissions(Context context, String[] permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    Log.d("PERMISSIONS", "Permission is not granted: " + permission);
                    return false;
                }
                Log.d("PERMISSIONS", "Permission already granted: " + permission);
            }
            return true;
        }
        return false;
    }
}
