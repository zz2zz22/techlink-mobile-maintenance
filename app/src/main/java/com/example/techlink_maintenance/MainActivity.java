package com.example.techlink_maintenance;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class MainActivity extends AppCompatActivity {
    Connection connect;
    String ConnectionResult = "";

    Button btn_pcccScan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_pcccScan = findViewById(R.id.btn_pcccScan);
        btn_pcccScan.setOnClickListener(v->
        {
            scanCode();
        });
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
       if (result.getContents() != null)
       {
           AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
           builder.setTitle("Result");
           builder.setMessage(result.getContents());
           builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int i) {
                   InsertToSQL(result.getContents());
                   dialogInterface.dismiss();
               }
           }).show();
       }
    });
    public void InsertToSQL(String result)
    {
        try {
            DatabaseConnector databaseConnector = new DatabaseConnector();
            connect = databaseConnector.connectionClass();
            ResultSet rs;
            if (connect!= null)
            {
                String query = "Insert into Test (barcode, text) values ('"+ result.trim() +"', 'test barcode')";
                Statement st = connect.createStatement();
                rs = st.executeQuery(query);
            }else {
                ConnectionResult = "Check Connection";
                rs = null;
            }
        }
        catch (Exception ex)
        {
            Log.e("Error ", ex.getMessage());
        }
    }
}