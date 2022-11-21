package com.example.techlink_maintenance;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.lang.reflect.Array;


public class MainActivity extends AppCompatActivity {
    ImageButton scanPCCC;
    int MY_CAMERA_REQUEST_CODE = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA}, 0);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getViews();
        scanPCCC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pcccIntent = new Intent(MainActivity.this, PCCCActivity.class);
                MainActivity.this.startActivity(pcccIntent);
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show();
                ((ActivityManager)MainActivity.this.getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData();
            }
        }
    }
    private void getViews()
    {
        scanPCCC = findViewById(R.id.scanPCCC);
    }
}