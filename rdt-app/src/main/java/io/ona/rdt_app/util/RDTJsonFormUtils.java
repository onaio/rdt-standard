package io.ona.rdt_app.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.domain.ProfileImage;
import org.smartregister.repository.ImageRepository;
import org.smartregister.util.AssetHandler;
import org.smartregister.view.activity.DrishtiApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import edu.washington.cs.ubicomplab.rdt_reader.callback.OnImageSavedCallBack;
import io.ona.rdt_app.activity.RDTJsonFormActivity;
import io.ona.rdt_app.application.RDTApplication;

import static io.ona.rdt_app.util.Constants.JSON_FORM_PARAM_JSON;
import static io.ona.rdt_app.util.Constants.PROFILE_PIC;

/**
 * Created by Vincent Karuri on 24/05/2019
 */
public class RDTJsonFormUtils {

    private static final String TAG = RDTJsonFormUtils.class.getName();

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

    public static void saveStaticImageToDisk(final Bitmap image, final String providerId, final String entityId, final OnImageSavedCallBack onImageSavedCallBack) {
        if (image == null || StringUtils.isBlank(providerId) || StringUtils.isBlank(entityId)) {
            onImageSavedCallBack.onImageSaved(null);
            return;
        }

        class SaveImageTask extends AsyncTask<Void, Void, ProfileImage> {

            @Override
            protected ProfileImage doInBackground(Void... voids) {

                ProfileImage profileImage = new ProfileImage();
                OutputStream os = null;
                try {
                    if (!StringUtils.isBlank(entityId)) {
                        final String absoluteFileName = DrishtiApplication.getAppDir() + File.separator + entityId + ".JPEG";

                        File outputFile = new File(absoluteFileName);
                        os = new FileOutputStream(outputFile);
                        Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
                        if (compressFormat != null) {
                            image.compress(compressFormat, 100, os);
                        } else {
                            throw new IllegalArgumentException("Failed to save static image, could not retrieve image compression format from name "
                                    + absoluteFileName);
                        }
                        // insert into the db
                        profileImage.setImageid(UUID.randomUUID().toString());
                        profileImage.setAnmId(providerId);
                        profileImage.setEntityID(entityId);
                        profileImage.setFilepath(absoluteFileName);
                        profileImage.setFilecategory(PROFILE_PIC);
                        profileImage.setSyncStatus(ImageRepository.TYPE_Unsynced);
                        ImageRepository imageRepo = RDTApplication.getInstance().getContext().imageRepository();
                        imageRepo.add(profileImage);
                    }
                } catch (FileNotFoundException e) {
                    Log.e(TAG, "Failed to save static image to disk");
                } finally {
                    if (os != null) {
                        try {
                            os.close();
                        } catch (IOException e) {
                            Log.e(TAG, "Failed to close static images output stream after attempting to write image");
                        }
                    }
                }
                return profileImage;
            }

            @Override
            protected void onPostExecute(ProfileImage profileImage) {
                onImageSavedCallBack.onImageSaved(profileImage.getFilepath());
            }
        }

        new SaveImageTask().execute();
    }

    public static Bitmap convertByteArrayToBitmap(byte[] src){
        return BitmapFactory.decodeByteArray(src, 0, src.length);
    }
}
