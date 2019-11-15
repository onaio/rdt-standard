package io.ona.rdt.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.domain.ProfileImage;
import org.smartregister.domain.UniqueId;
import org.smartregister.exception.JsonFormMissingStepCountException;
import org.smartregister.repository.ImageRepository;
import org.smartregister.util.AssetHandler;
import org.smartregister.view.activity.DrishtiApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import edu.washington.cs.ubicomplab.rdt_reader.ImageUtil;
import edu.washington.cs.ubicomplab.rdt_reader.callback.OnImageSavedCallBack;
import io.ona.rdt.BuildConfig;
import io.ona.rdt.activity.RDTJsonFormActivity;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.callback.OnImageSavedCallback;
import io.ona.rdt.callback.OnUniqueIdFetchedCallback;
import io.ona.rdt.domain.CompositeImage;
import io.ona.rdt.domain.ParcelableImageMetadata;
import io.ona.rdt.domain.Patient;
import timber.log.Timber;

import static io.ona.rdt.util.Constants.BULLET_DOT;
import static io.ona.rdt.util.Constants.Form.RDT_ID;
import static io.ona.rdt.util.Constants.JSON_FORM_PARAM_JSON;
import static io.ona.rdt.util.Constants.MULTI_VERSION;
import static io.ona.rdt.util.Constants.REQUEST_CODE_GET_JSON;
import static io.ona.rdt.util.Constants.Test.CROPPED_IMAGE;
import static io.ona.rdt.util.Constants.Test.FULL_IMAGE;
import static org.smartregister.util.JsonFormUtils.ENTITY_ID;
import static org.smartregister.util.JsonFormUtils.KEY;
import static org.smartregister.util.JsonFormUtils.VALUE;
import static org.smartregister.util.JsonFormUtils.getMultiStepFormFields;
import static org.smartregister.util.JsonFormUtils.getString;

/**
 * Created by Vincent Karuri on 24/05/2019
 */
public class RDTJsonFormUtils {

    private static final String TAG = RDTJsonFormUtils.class.getName();

    public static void saveStaticImagesToDisk(final Context context, CompositeImage compositeImage, final OnImageSavedCallback onImageSavedCallBack) {

        Bitmap fullImage = compositeImage.getFullImage();
        ParcelableImageMetadata parcelableImageMetadata = compositeImage.getParcelableImageMetadata();
        String providerId = parcelableImageMetadata.getProviderId();
        String entityId = parcelableImageMetadata.getBaseEntityId();
        if (fullImage == null || StringUtils.isBlank(providerId) || StringUtils.isBlank(entityId)) {
            onImageSavedCallBack.onImageSaved(null);
            return;
        }

        class SaveImageTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                saveImage(entityId, parcelableImageMetadata, compositeImage, context);
                return null;
            }

            @Override
            protected void onPostExecute(Void voids) {
                onImageSavedCallBack.onImageSaved(compositeImage);
            }
        }

        new SaveImageTask().execute();
    }


    private static void saveImage(String entityId, ParcelableImageMetadata parcelableImageMetadata, CompositeImage compositeImage, Context context) {

        if (!StringUtils.isBlank(entityId)) {
            final String imgFolderPath = DrishtiApplication.getAppDir() + File.separator + entityId;
            final File imageFolder = new File(imgFolderPath);
            boolean success = true;
            if (!imageFolder.exists()) {
                success = imageFolder.mkdirs();
            }

            if (success) {
                parcelableImageMetadata.setImageToSave(FULL_IMAGE);
                saveImage(imgFolderPath, compositeImage.getFullImage(), context, parcelableImageMetadata);

                parcelableImageMetadata.setImageToSave(CROPPED_IMAGE);
                saveImage(imgFolderPath, compositeImage.getCroppedImage(), context, parcelableImageMetadata);
            } else {
                Timber.e(TAG, "Sorry, could not create fullImage folder!");
            }
        }
    }

    private static void saveImage(String imgFolderPath, Bitmap image, Context context, ParcelableImageMetadata parcelableImageMetadata) {
        ProfileImage profileImage = new ProfileImage();
        Pair<Boolean, String> writeResult = writeImageToDisk(imgFolderPath, image, context);
        boolean isSuccessfulWrite = writeResult.first;
        String absoluteFilePath = writeResult.second;
        if (isSuccessfulWrite) {
            saveImgDetails(absoluteFilePath, parcelableImageMetadata, profileImage);
            if (FULL_IMAGE.equals(parcelableImageMetadata.getImageToSave())) {
                parcelableImageMetadata.setFullImageId(profileImage.getImageid());
                parcelableImageMetadata.setImageTimeStamp(System.currentTimeMillis());
            } else {
                parcelableImageMetadata.setCroppedImageId(profileImage.getImageid());
            }
        }
    }

    private static void saveImgDetails(String absoluteFilePath, ParcelableImageMetadata parcelableImageMetadata, ProfileImage profileImage) {
        profileImage.setImageid(UUID.randomUUID().toString());
        profileImage.setAnmId(parcelableImageMetadata.getProviderId());
        profileImage.setEntityID(parcelableImageMetadata.getBaseEntityId());
        profileImage.setFilepath(absoluteFilePath);
        profileImage.setFilecategory(MULTI_VERSION);
        profileImage.setSyncStatus(ImageRepository.TYPE_Unsynced);

        ImageRepository imageRepo = RDTApplication.getInstance().getContext().imageRepository();
        imageRepo.add(profileImage);
    }

    private static Pair<Boolean, String> writeImageToDisk(String imgFolderPath, Bitmap image, Context context) {

        OutputStream outputStream = null;
        String absoluteFilePath = null;
        Pair<Boolean, String> result = new Pair<>(false, absoluteFilePath);
        try {
            absoluteFilePath = imgFolderPath + File.separator + UUID.randomUUID() + ".JPEG";
            File outputFile = new File(absoluteFilePath);

            outputStream = new FileOutputStream(outputFile);
            image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            if (BuildConfig.SAVE_IMAGES_TO_GALLERY) {
                saveImageToGallery(context, image);
            }
            result = new Pair<>(true, absoluteFilePath);
        } catch(FileNotFoundException e){
            Timber.e(TAG, e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    Timber.e(TAG, e);
                }
            }
        }

        return result;
    }

    private static void saveImageToGallery(Context context, Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        ImageUtil.saveImage(context, stream.toByteArray(), 0, false, new OnImageSavedCallBack() {
            @Override
            public void onImageSaved(String imageLocation) {
                // do nothing
            }
        });
    }

    public static Bitmap convertByteArrayToBitmap(final byte[] src) {
        return BitmapFactory.decodeByteArray(src, 0, src.length);
    }

    public void startJsonForm(JSONObject form, Activity context, int requestCode) {
        Intent intent = new Intent(context, RDTJsonFormActivity.class);
        try {
            intent.putExtra(JSON_FORM_PARAM_JSON, form.toString());
            context.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            Timber.e(TAG, e);
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

    public void launchForm(Activity activity, String formName, Patient patient, String rdtId) throws JSONException {
        try {
            JSONObject formJsonObject = getFormJsonObject(formName, activity);
            prePopulateFormFields(formJsonObject, patient, rdtId, 8);
            startJsonForm(formJsonObject, activity, REQUEST_CODE_GET_JSON);
        } catch (JsonFormMissingStepCountException e) {
            Timber.e(TAG, e);
        }
    }

    public void prePopulateFormFields(JSONObject jsonForm, Patient patient, String rdtId, int numFields) throws JSONException, JsonFormMissingStepCountException {
        jsonForm.put(ENTITY_ID, patient == null ? null : patient.getBaseEntityId());
        JSONArray fields = getMultiStepFormFields(jsonForm);
        int fieldsPopulated = 0;
        for (int i = 0; i < fields.length(); i++) {
            JSONObject field = fields.getJSONObject(i);
            // pre-populate rdt id labels
            if (Constants.Form.LBL_RDT_ID.equals(field.getString(KEY))) {
                field.put("text", "RDT ID: " + rdtId);
                fieldsPopulated++;
            }
            // pre-populate rdt id field
            if (RDT_ID.equals(field.getString(KEY))) {
                field.put(VALUE, rdtId);
                fieldsPopulated++;
            }
            // pre-populate patient fields
            if (patient != null) {
                if (Constants.Form.LBL_PATIENT_NAME.equals(field.getString(KEY))) {
                    field.put(VALUE, patient.getPatientName());
                    field.put("text", patient.getPatientName());
                    fieldsPopulated++;
                } else if (Constants.Form.LBL_PATIENT_GENDER_AND_ID.equals(field.getString(KEY))) {
                    field.put(VALUE, patient.getPatientSex());
                    field.put("text", patient.getPatientSex() + BULLET_DOT + "ID: " + patient.getBaseEntityId());
                    fieldsPopulated++;
                }
            }
            // save cpu time
            if (fieldsPopulated == numFields) {
                break;
            }
        }
    }

    public synchronized void getNextUniqueId(final FormLaunchArgs args, final OnUniqueIdFetchedCallback callBack) {
        class FetchUniqueIdTask extends AsyncTask<Void, Void, UniqueId> {
            @Override
            protected UniqueId doInBackground(Void... voids) {
                return RDTApplication.getInstance().getContext().getUniqueIdRepository().getNextUniqueId();
            }

            @Override
            protected void onPostExecute(UniqueId result) {
                if (callBack != null) {
                    callBack.onUniqueIdFetched(args, result == null ? new UniqueId() : result);
                }
            }
        }
        new FetchUniqueIdTask().execute();
    }

    public static String appendEntityId(JSONObject jsonForm) throws JSONException {
        String entityId = getString(jsonForm, Constants.ENTITY_ID);
        entityId = entityId == null ? UUID.randomUUID().toString() : entityId;
        jsonForm.put(Constants.ENTITY_ID, entityId);
        return entityId;
    }
}