package com.example.techlink_maintenance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PCCC_Scan extends AppCompatActivity {
    Connection connect;
    String ConnectionResult = "";
    CodeScanner codeScanner;
    String datetime, type, remark, uuid;
    SimpleDateFormat simpleDateFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pccc_scan);
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            type = extras.getString("type");
            remark = extras.getString("remark");
        }
        scanCode();
    }

    private void scanCode() {
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        codeScanner = new CodeScanner(this, scannerView);
        codeScanner.setCamera(CodeScanner.CAMERA_BACK);
        codeScanner.setFormats(CodeScanner.ALL_FORMATS);

        codeScanner.setAutoFocusMode(AutoFocusMode.SAFE);
        codeScanner.setScanMode(ScanMode.SINGLE);
        codeScanner.setAutoFocusEnabled(true);
        codeScanner.setFlashEnabled(true);

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        InsertToSQL(result.getText());
                        Intent intent = new Intent(PCCC_Scan.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |  Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        PCCC_Scan.this.startActivity(intent);
                    }
                });
            }
        });
    }

    public void InsertToSQL(String result)
    {
        try {
            DatabaseConnector databaseConnector = new DatabaseConnector();
            connect = databaseConnector.connectionClass();
            if (connect!= null)
            {
                uuid = UUIDGenerator.getAscId();
                simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                datetime = simpleDateFormat.format(Calendar.getInstance().getTime()).toString();
                String query = "Insert into PCCC_Record (uuid, barcode, maintenance_type, remark,  update_date) values ('"+uuid+"', '"+ result.trim() +"', '"+ type +"', '"+remark+"', '"+datetime+"')";
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);
            }else {
                ConnectionResult = "Check Connection";
            }
        }
        catch (Exception ex)
        {
            Log.e("Error ", ex.getMessage());
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }
}