package com.example.techlink_maintenance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class PCCCActivity extends AppCompatActivity {

    TextView lbTypeIn, lbTypeOut;
    RadioButton rbtnTypeIn, rbtnTypeOut;
    EditText remarkTextEdit;
    Button scanSave;
    String remark, type, type_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pcccactivity);

        getViews();
        lbTypeIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!rbtnTypeIn.isChecked())
                    rbtnTypeIn.setChecked(true);
            }
        });
        lbTypeOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!rbtnTypeOut.isChecked())
                    rbtnTypeOut.setChecked(true);
            }
        });
        scanSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rbtnTypeIn.isChecked()) {
                    type = "0";
                    type_name = "Hoàn tất bảo trì/ Nhập mới";
                }
                if (rbtnTypeOut.isChecked())
                {
                    type = "1";
                    type_name = "Bảo trì/ Thay thế";
                }
                Intent i = new Intent(PCCCActivity.this, PCCC_Scan.class);
                remark = remarkTextEdit.getText().toString();
                i.putExtra("type", type);
                i.putExtra("remark", remark);
                i.putExtra("type_name", type_name);
                PCCCActivity.this.startActivity(i);
            }
        });
    }

    private void getViews()
    {
        lbTypeIn = findViewById(R.id.lbTypeIn);
        lbTypeOut = findViewById(R.id.lbTypeOut);
        rbtnTypeIn = findViewById(R.id.rbtnTypeIn);
        rbtnTypeOut = findViewById(R.id.rbtnTypeOut);
        remarkTextEdit = findViewById(R.id.editTextRemark);
        scanSave = findViewById(R.id.btnSaveScan);
    }
}