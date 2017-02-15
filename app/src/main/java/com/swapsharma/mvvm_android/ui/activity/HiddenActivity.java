package com.swapsharma.mvvm_android.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.swapsharma.mvvm_android.model.Sources;

public class HiddenActivity extends Activity {
    public static final String IMAGE_SOURCE = "image_source";
    private static final int SELECT_PHOTO = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            handleIntent(getIntent());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            handleIntent(getIntent());
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_PHOTO:
                    handleGalleryResult(data);
                    break;
            }
        }
        finish();
    }

    private void handleGalleryResult(Intent data) {
        RxImagePicker.with(this).onImagePicked(data.getData());
    }

    private void handleIntent(Intent intent) {
        if (!checkPermission()) {
            return;
        }

        Sources sourceType = Sources.values()[intent.getIntExtra(IMAGE_SOURCE, 0)];
        int chooseCode = 0;
        Intent pictureChooseIntent = null;

        switch (sourceType) {
            case GALLERY:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    pictureChooseIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    pictureChooseIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                } else {
                    pictureChooseIntent = new Intent(Intent.ACTION_GET_CONTENT);
                }
                pictureChooseIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                pictureChooseIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pictureChooseIntent.setType("image/*");
                chooseCode = SELECT_PHOTO;
                break;
        }

        startActivityForResult(pictureChooseIntent, chooseCode);
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(HiddenActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HiddenActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0);
            return false;
        } else {
            return true;
        }
    }
}
