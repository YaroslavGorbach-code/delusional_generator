package com.YaroslavGorbach.delusionalgenerator.Helpers;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

public class Permissions {

    private static final String recordPermission = Manifest.permission.RECORD_AUDIO;
    private static final int PERMISSION_CODE = 21;

    public boolean checkRecordPermission(Activity activity) {
        //Check permission
        if (ActivityCompat.checkSelfPermission(activity, recordPermission) == PackageManager.PERMISSION_GRANTED) {
            //Permission Granted
            return true;
        } else {
            //Permission not granted, ask for permission
            ActivityCompat.requestPermissions(activity, new String[]{recordPermission}, PERMISSION_CODE);
            return false;
        }
    }
}