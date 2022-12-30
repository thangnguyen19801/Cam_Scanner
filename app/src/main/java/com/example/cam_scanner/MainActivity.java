package com.example.cam_scanner;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cam_scanner.adapter.MainAdapter;
import com.example.cam_scanner.databinding.ActivityMainBinding;

import com.example.cam_scanner.utils.PermissionUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    Button camera_open_id;
    Button BSelectImage;
    ImageView IVPreviewImage;
    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private ActivityResultContracts.RequestMultiplePermissions multiplePermissionsContract;
    private ActivityResultLauncher<String[]> multiplePermissionLauncher;

    private final static int Convert_Request_CODE = 42;
    private MainAdapter mainAdapter;
    private ActionMode actionMode;
    private ActionModeCallback actionModeCallback;
    private RecyclerView recyclerView;
    List<File> items = null;

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
        BSelectImage.setOnClickListener(v -> startConvertActivity("FileSearch"));

        camera_open_id.setOnClickListener(v -> {
            startConvertActivity("CameraActivity");
        });

        // set data and list history
        recyclerView = findViewById(R.id.mainRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        CreateDataSource();
    }

    private void CreateDataSource() {
        items = new ArrayList<File>();

        File root = getFilesDir();
        File myDir = new File(root + "/ImageToPdf");
        if(!myDir.exists()) {
            myDir.mkdirs();
        }
        File[] files = myDir.listFiles();

        for(int i = 0; i < files.length; i++) {
            items.add(files[i]);
        }
        mainAdapter = new MainAdapter(this, items);
        actionModeCallback = new ActionModeCallback();
        mainAdapter.setOnItemClickListener(new mOnItemClickListener(actionModeCallback));
        recyclerView.setAdapter(mainAdapter);
    }

    private void startConvertActivity(String message) {
        Intent intent = new Intent(getApplicationContext(), PreviewPdfActivity.class);
        intent.putExtra("ActivityAction", message);
        launchAddImage.launch(intent);
    }


    ActivityResultLauncher<Intent> launchAddImage =
            registerForActivityResult(
                    new ActivityResultContracts
                            .StartActivityForResult(),
                    result -> {
                        if(result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            if(data != null) {
                                CreateDataSource();
                                mainAdapter.notifyItemInserted(items.size()-1);
                            }
                        }
                    }
            );


    private class mOnItemClickListener implements MainAdapter.OnItemClickListener {
        private ActionModeCallback actionModeCallback;

        public mOnItemClickListener(ActionModeCallback actionModeCallback) {
            this.actionModeCallback = actionModeCallback;
        }

        @Override
        public void onItemClick(View view, File obj, int position) {
            if(mainAdapter.getSelectedItemsCount() > 0) {
                if(actionMode == null) {
                    actionMode = startActionMode(actionModeCallback);
                }
            } else {
                Toast.makeText(MainActivity.this, "Nothing to show", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onLongItemClick(View view, File obj, int position) {
            actionMode = startActionMode(actionModeCallback);
            mainAdapter.toggleSelection(position);
            int count = mainAdapter.getSelectedItemsCount();
            if(count == 0) {
                actionMode.finish();
            } else {
                actionMode.setTitle(String.valueOf(count) + "selected");
                actionMode.invalidate();
            }
        }
    }

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_main_action_mode, menu);
            return false;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int id = item.getItemId();
            if(id == R.id.action_share) {
                shareItems();
                return true;
            }

            if(id == R.id.action_delete) {
                showDeleteDialog();
                return true;
            }

            if(id == R.id.action_select_all) {
                selectAll();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mainAdapter.deleteSelections();
            actionMode = null;
        }
    }

    public void shareItems() {
        ShareCompat.IntentBuilder shareIntentBuilder = new ShareCompat.IntentBuilder(this);
        Intent intent = shareIntentBuilder.getIntent();
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        ArrayList<Uri> files = new ArrayList<Uri>();
        List<Integer> selectedItemPositions = mainAdapter.getSelectedItems();
        for(int i = 0; i < selectedItemPositions.size(); i++) {
            File file = items.get(i);
            Uri selectedFileUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
            files.add(selectedFileUri);
        }
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
        intent.setType("application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if(intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        actionMode.finish();
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure want to delete the selected files?");
        builder.setPositiveButton(R.string.OK, ((dialog, which) -> {
            deleteItems();
            actionMode.finish();
        }));
        builder.setNegativeButton(R.string.Cancel, ((dialog, which) -> {}));
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteItems() {
        List<Integer> selectedItemPositions = mainAdapter.getSelectedItems();
        for(int i = 0; i < selectedItemPositions.size(); i++) {
            File file = items.get(i);
            file.delete();
            mainAdapter.removeItem(selectedItemPositions.get(i));
        }
        mainAdapter.notifyDataSetChanged();
    }

    private void selectAll() {
        mainAdapter.selectAll();
        int count = mainAdapter.getSelectedItemsCount();
        if(count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count) + " selected");
            actionMode.invalidate();
        }
    }

}
