package com.example.final_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LogActivity extends AppCompatActivity {
    private EditText mIdEdit;
    private EditText mNameEdit;
    private Button mLogButton;

    private final static int LOG_SUCCESS_RESPONSE = 111;
    private final static String TAG = "inform";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        mIdEdit = (EditText) findViewById (R.id.id_edit);
        mNameEdit = (EditText) findViewById (R.id.name_edit);
        mLogButton = (Button) findViewById (R.id.log_button);

        mLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String studentId = mIdEdit.getText().toString();
                String userName = mNameEdit.getText().toString();
                if (studentId != "" && userName != "") {
                    Intent intent = new Intent (LogActivity.this, MainActivity.class);
                    intent.putExtra ("studentId", studentId);
                    intent.putExtra ("userName", userName);
                    LogActivity.this.setResult (LOG_SUCCESS_RESPONSE, intent);
                    LogActivity.this.finish();
                }else {
                    Toast.makeText(LogActivity.this, "未输入完全", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
