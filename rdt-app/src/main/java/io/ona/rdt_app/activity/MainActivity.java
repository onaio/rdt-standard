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
import org.json.JSONObject;

import io.ona.rdt_app.R;
import io.ona.rdt_app.util.Constants;
import util.RDTCaptureJsonFormUtils;

import static io.ona.rdt_app.util.Constants.REQUEST_CODE_GET_JSON;

public class MainActivity extends AppCompatActivity {

    private RDTCaptureJsonFormUtils jsonFormUtils;
    private final String TAG = MainActivity.class.getName();
    private Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        jsonFormUtils = new RDTCaptureJsonFormUtils();
        context = this;

        findViewById(R.id.btn_launch_rdt_reader).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject formJsonObject = jsonFormUtils.getFormJsonObject("json.form/rdt-capture-form.json", context);
                    jsonFormUtils.startJsonForm(formJsonObject, context, REQUEST_CODE_GET_JSON);
                } catch (JSONException e) {
                    Log.e(TAG, e.getStackTrace().toString());
                }
            }
        });

        findViewById(R.id.btn_launch_register_activity).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(context, PatientRegisterActivity.class);
               startActivity(intent);
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