package io.ona.rdt_app.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.crashlytics.android.Crashlytics;

import org.json.JSONException;
import org.json.JSONObject;

import io.fabric.sdk.android.Fabric;
import io.ona.rdt_app.R;
import util.RDTCaptureJsonFormUtils;

import static io.ona.rdt_app.activity.Constants.REQUEST_CODE_GET_JSON;

public class MainActivity extends AppCompatActivity {

    private RDTCaptureJsonFormUtils jsonFormUtils;
    private final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_main);

        jsonFormUtils = new RDTCaptureJsonFormUtils();

        findViewById(R.id.btn_launch_rdt_reader).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Context context = getApplicationContext();
                    JSONObject formJsonObject = jsonFormUtils.getFormJsonObject("json.form/rdt-capture-form.json", context);
                    jsonFormUtils.startJsonForm(formJsonObject, MainActivity.this, REQUEST_CODE_GET_JSON);
                } catch (JSONException e) {
                    Log.e(TAG, e.getStackTrace().toString());
                }
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