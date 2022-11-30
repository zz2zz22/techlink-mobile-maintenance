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
import java.util.Date;

public class PCCC_Scan extends AppCompatActivity {
    Connection connect;
    String ConnectionResult = "";
    CodeScanner codeScanner;
    String datetime, type, remark, uuid, type_name;
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
            type_name = extras.getString("type_name");
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
                        InsertToPCCCRecord(result.getText());
                        if(type.equals("0"))
                        {
                            UpdateDeviceInfo(result.getText());
                        }
                        Toast toast = Toast.makeText(PCCC_Scan.this, "Lưu dữ liệu thành công", Toast.LENGTH_LONG);
                        toast.show();
                        Intent intent = new Intent(PCCC_Scan.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |  Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        PCCC_Scan.this.startActivity(intent);
                    }
                });
            }
        });
    }

    public void InsertToPCCCRecord(String result)
    {
        try {
            DatabaseConnector databaseConnector = new DatabaseConnector();
            connect = databaseConnector.connectionClass();
            if (connect!= null)
            {
                uuid = UUIDGenerator.getAscId();
                simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                datetime = simpleDateFormat.format(Calendar.getInstance().getTime()).toString();
                Statement st = connect.createStatement();
                String insertPcccRecord = "Insert into PCCC_Record (uuid, barcode, maintenance_type, maintenance_type_name, remark,  update_date) values ('"+uuid+"', '"+ result.trim() +"', '"+ type +"', N'"+type_name+"', N'"+remark+"', '"+datetime+"')";
                st.executeQuery(insertPcccRecord);
            }else {
                ConnectionResult = "Check Connection";
            }
        }
        catch (Exception ex)
        {
            Log.e("Error ", ex.getMessage());
        }
    }

    public void UpdateDeviceInfo(String result)
    {
        try {
            DatabaseConnector databaseConnector = new DatabaseConnector();
            connect = databaseConnector.connectionClass();
            if(connect!=null)
            {
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery("select * from Device_Info where device_uuid = '"+result.trim()+"' and device_group_uuid = '6GL4FSYCUO0BNO'");
                while (rs.next()) {
                    String updateDeviceInfo;

                    Date oldExpDate = simpleDateFormat.parse(rs.getString(8));
                    Calendar c = Calendar.getInstance();
                    c.setTime(oldExpDate);
                    c.add(Calendar.MONTH, Integer.parseInt(rs.getString(7)));
                    String newExpDate = simpleDateFormat.format(c.getTime());

                    updateDeviceInfo = "Update Device_Info set expire_date = '" + newExpDate + "', newest_maintenance_info = '" + uuid + "', newest_maintenance_date = '" + datetime + "' where device_uuid = '"+result.trim()+"'";
                    st.executeUpdate(updateDeviceInfo);
                }
            }else{
                ConnectionResult = "Check Connection";
            }
        }
        catch(Exception ex)
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