package util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.util.AssetHandler;

import io.ona.rdt_app.activity.RDTJsonFormActivity;

import static io.ona.rdt_app.util.Constants.JSON_FORM_PARAM_JSON;

/**
 * Created by Vincent Karuri on 24/05/2019
 */
public class RDTCaptureJsonFormUtils {

    private static final String TAG = RDTCaptureJsonFormUtils.class.getName();

    public void startJsonForm(JSONObject form, Activity context, int requestCode) {
        Intent intent = new Intent(context, RDTJsonFormActivity.class);
        try {
            intent.putExtra(JSON_FORM_PARAM_JSON, form.toString());
            context.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public JSONObject getFormJsonObject(String formName, Context context) throws JSONException {
        String formString = AssetHandler.readFileFromAssetsFolder(formName, context);
        return new JSONObject(formString);
    }

    public void showToast(final Activity activity, final String text) {
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, text, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
