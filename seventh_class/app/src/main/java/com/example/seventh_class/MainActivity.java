package com.example.seventh_class;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button toSystemCamera;
    private Button toCustomCamera;

    private final static int PERMISSION_REQUEST_CODE = 101;
    private static final String TAG = "inform";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toSystemCamera = (Button) findViewById (R.id.system);
        toCustomCamera = (Button) findViewById (R.id.camera);

        toSystemCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity (new Intent (MainActivity.this, sysCameraActivity.class));
            }
        });

        toCustomCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean camera_per = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
                boolean record_per = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
                if (!camera_per || !record_per){
                    List<String> permission = new ArrayList<String>();
                    if (!camera_per) {
                        permission.add(Manifest.permission.CAMERA);
                    }
                    if (!record_per) {
                        permission.add(Manifest.permission.RECORD_AUDIO);
                    }
                    ActivityCompat.requestPermissions(MainActivity.this, permission.toArray(new String[permission.size()]), PERMISSION_REQUEST_CODE);
                }else {
                    startActivity (new Intent (MainActivity.this, customCameraActivity.class));
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult (requestCode, permissions, grantResults);

        boolean hasPermission = true;
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                hasPermission = false;
                break;
            }
        }
        if (hasPermission) {
            Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "权限获取失败", Toast.LENGTH_SHORT).show();
        }
    }

}