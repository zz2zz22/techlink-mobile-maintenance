package com.example.techlink_maintenance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class PCCCActivity extends AppCompatActivity {

    RadioButton rbtnTypeIn, rbtnTypeOut;
    EditText remarkTextEdit;
    Button scanSave;
    String remark, type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pcccactivity);

        getViews();
        scanSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rbtnTypeIn.isChecked())
                    type = "0";
                if (rbtnTypeOut.isChecked())
                    type = "1";
                Intent i = new Intent(PCCCActivity.this, PCCC_Scan.class);
                remark = remarkTextEdit.getText().toString();
                i.putExtra("type", type);
                i.putExtra("remark", remark);
                PCCCActivity.this.startActivity(i);
            }
        });
    }

    private void getViews()
    {
        rbtnTypeIn = findViewById(R.id.rbtnTypeIn);
        rbtnTypeOut = findViewById(R.id.rbtnTypeOut);
        remarkTextEdit = findViewById(R.id.editTextRemark);
        scanSave = findViewById(R.id.btnSaveScan);
    }
}