package com.uttej.oraclereadersclub.Utils;

import com.uttej.oraclereadersclub.Manifest;

/**
 * Created by Clean on 27-03-2018.
 */

public class Permissions {

    public static final String[] PERMISSIONS = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };

    public static final String CAMERA_PERMISSION = (
            android.Manifest.permission.CAMERA
    );

    public static final String WRITE_STORAGE_PERMISSION = (
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    );

    public static final String READ_STORAGE_PERMISSION = (
            android.Manifest.permission.READ_EXTERNAL_STORAGE
    );



}
