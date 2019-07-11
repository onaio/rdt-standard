package io.ona.rdt_app.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.json.JSONException;

import io.ona.rdt_app.R;
import io.ona.rdt_app.application.RDTApplication;
import io.ona.rdt_app.util.Constants;
import io.ona.rdt_app.util.RDTJsonFormUtils;

import static io.ona.rdt_app.util.Constants.Form.RDT_TEST_FORM;

public class MainActivity extends AppCompatActivity {

    private RDTJsonFormUtils jsonFormUtils;
    private final String TAG = MainActivity.class.getName();
    private Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        jsonFormUtils = new RDTJsonFormUtils();
        context = this;

        findViewById(R.id.btn_launch_register_activity).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(context, PatientRegisterActivity.class);
               startActivity(intent);
           }
        });

        findViewById(R.id.btn_manual_sync).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RDTApplication.getInstance().scheduleJobsImmediately();
            }
        });

        requestPermissions();
    }

    public void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_STORAGE_PERMISSION);
        }
    }
}