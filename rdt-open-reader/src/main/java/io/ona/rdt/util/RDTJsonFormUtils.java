package io.ona.rdt.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.domain.ProfileImage;
import org.smartregister.domain.UniqueId;
import org.smartregister.domain.tag.FormTag;
import org.smartregister.repository.AllSettings;
import org.smartregister.repository.ImageRepository;
import org.smartregister.util.AssetHandler;
import org.smartregister.util.JsonFormUtils;
import org.smartregister.view.activity.DrishtiApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import androidx.core.util.Pair;
import edu.washington.cs.ubicomplab.rdt_reader.utils.ImageUtil;
import io.ona.rdt.BuildConfig;
import io.ona.rdt.activity.RDTJsonFormActivity;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.callback.OnImageSavedCallback;
import io.ona.rdt.callback.OnUniqueIdsFetchedCallback;
import io.ona.rdt.domain.CompositeImage;
import io.ona.rdt.domain.ParcelableImageMetadata;
import io.ona.rdt.domain.Patient;
import timber.log.Timber;

import static com.vijay.jsonwizard.constants.JsonFormConstants.PERFORM_FORM_TRANSLATION;
import static com.vijay.jsonwizard.constants.JsonFormConstants.TEXT;
import static com.vijay.jsonwizard.utils.Utils.closeCloseable;
import static io.ona.rdt.util.Constants.Config.MULTI_VERSION;
import static io.ona.rdt.util.Constants.Form.RDT_TEST_FORM;
import static io.ona.rdt.util.Constants.FormFields.EVENT_TYPE;
import static io.ona.rdt.util.Constants.FormFields.LBL_PATIENT_GENDER_AND_ID;
import static io.ona.rdt.util.Constants.FormFields.LBL_PATIENT_NAME;
import static io.ona.rdt.util.Constants.Format.BULLET_DOT;
import static io.ona.rdt.util.Constants.RequestCodes.REQUEST_CODE_GET_JSON;
import static io.ona.rdt.util.Constants.Result.JSON_FORM_PARAM_JSON;
import static io.ona.rdt.util.Constants.Step.RDT_ID_KEY;
import static io.ona.rdt.util.Constants.Test.CROPPED_IMAGE;
import static io.ona.rdt.util.Constants.Test.FULL_IMAGE;
import static org.smartregister.util.JsonFormUtils.ENTITY_ID;
import static org.smartregister.util.JsonFormUtils.KEY;
import static org.smartregister.util.JsonFormUtils.VALUE;
import static org.smartregister.util.JsonFormUtils.getMultiStepFormFields;
import static org.smartregister.util.Utils.isEmptyCollection;

/**
 * Created by Vincent Karuri on 24/05/2019
 */
public class RDTJsonFormUtils {

    private static Set<String> formsToAddEntityIdToIfBlank = getFormsToAddEntityIdToIfBlank();
    private Set<String> formsThatShouldBePrepopulated = getFormsThatShouldBePrepopulated();

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
                String filePath = saveImage(imgFolderPath, compositeImage.getFullImage(), context, parcelableImageMetadata);
                compositeImage.getParcelableImageMetadata().setFullImageMD5Hash(getFileMD5Hash(filePath));
                compositeImage.withFullImageFilePath(filePath);

                parcelableImageMetadata.setImageToSave(CROPPED_IMAGE);
                filePath = saveImage(imgFolderPath, compositeImage.getCroppedImage(), context, parcelableImageMetadata);
                compositeImage.getParcelableImageMetadata().setCroppedImageMD5Hash(getFileMD5Hash(filePath));
                compositeImage.withCroppedImageFilePath(filePath);
            } else {
                Timber.e("Sorry, could not create image folder!");
            }
        }
    }

    private static String saveImage(String imgFolderPath, Bitmap image, Context context, ParcelableImageMetadata parcelableImageMetadata) {
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
        return absoluteFilePath;
    }

    private static void saveImgDetails(String absoluteFilePath, ParcelableImageMetadata parcelableImageMetadata, ProfileImage profileImage) {
        profileImage.setImageid(extractImageFileName(absoluteFilePath));
        profileImage.setAnmId(parcelableImageMetadata.getProviderId());
        profileImage.setEntityID(parcelableImageMetadata.getBaseEntityId());
        profileImage.setFilepath(absoluteFilePath);
        profileImage.setFilecategory(MULTI_VERSION);
        profileImage.setSyncStatus(ImageRepository.TYPE_Unsynced);

        ImageRepository imageRepo = RDTApplication.getInstance().getContext().imageRepository();
        imageRepo.add(profileImage);
    }

    private static String extractImageFileName(String filePath) {
        String[] fileLevels = filePath.split("/");
        return fileLevels[fileLevels.length - 1].split("\\.")[0];
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
            Timber.e(e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    Timber.e(e);
                }
            }
        }

        return result;
    }

    private static String getFileMD5Hash(String absoluteFilePath) {
        String md5Hash = "";
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(absoluteFilePath);
            md5Hash = new String(Hex.encodeHex(DigestUtils.md5(inputStream)));
        } catch (IOException e) {
            Timber.e(e);
        } finally {
            closeCloseable(inputStream);
        }
        return md5Hash;
    }

    private static void saveImageToGallery(Context context, Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        ImageUtil.saveImage(context, stream.toByteArray(), 0, false, imageLocation -> {
            // do nothing
        });
    }

    public static Bitmap convertByteArrayToBitmap(final byte[] src) {
        return BitmapFactory.decodeByteArray(src, 0, src.length);
    }

    public void startJsonForm(JSONObject form, Activity context, int requestCode) {
        Intent intent = new Intent(context, getJsonFormActivityClass());
        try {
            intent.putExtra(JSON_FORM_PARAM_JSON, form.toString());
            intent.putExtra(PERFORM_FORM_TRANSLATION, true);
            context.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    protected Class getJsonFormActivityClass() {
        return RDTJsonFormActivity.class;
    }

    public JSONObject getFormJsonObject(String formName, Context context) throws JSONException {
        String formString = AssetHandler.readFileFromAssetsFolder(formName, context);
        return new JSONObject(formString);
    }

    public void showToast(final Activity activity, final String text) {
        if (activity != null) {
            activity.runOnUiThread(() -> Toast.makeText(activity, text, Toast.LENGTH_LONG).show());
        }
    }

    public JSONObject launchForm(Activity activity, String formName, Patient patient, List<String> uniqueIDs) {
        JSONObject formJsonObject = null;
        try {
            formJsonObject = getFormJsonObject(formName, activity);
            if (formShouldBePrePopulated(formName)) {
                String uniqueId = isEmptyCollection(uniqueIDs) ? "" : uniqueIDs.get(0);
                prePopulateFormFields(formJsonObject, patient, uniqueId);
            }
            formJsonObject.put(ENTITY_ID, patient == null ? null : patient.getBaseEntityId());
            startJsonForm(formJsonObject, activity, REQUEST_CODE_GET_JSON);
        } catch (JSONException e) {
            Timber.e(e);
        }
        return formJsonObject;
    }


    private Set<String> getFormsThatShouldBePrepopulated() {
        if (formsThatShouldBePrepopulated ==  null) {
            formsThatShouldBePrepopulated =  initializeFormsThatShouldBePrepopulated();
        }
        return formsThatShouldBePrepopulated;
    }

    protected Set<String> initializeFormsThatShouldBePrepopulated() {
        return new HashSet<>(Arrays.asList(RDT_TEST_FORM));
    }

    private boolean formShouldBePrePopulated(String formName) {
        return getFormsThatShouldBePrepopulated().contains(formName);
    }

    public void prePopulateFormFields(JSONObject jsonForm, Patient patient, String uniqueID) throws JSONException {
        JSONArray fields = getMultiStepFormFields(jsonForm);
        for (int i = 0; i < fields.length(); i++) {
            JSONObject field = fields.getJSONObject(i);
            prePopulateRDTFormFields(field, uniqueID);
            prePopulateRDTPatientFields(patient, field);
        }
    }

    protected void prePopulateRDTFormFields(JSONObject field, String uniqueID) throws JSONException {
        // pre-populate rdt id labels
        if (Constants.FormFields.LBL_RDT_ID.equals(field.getString(KEY))) {
            field.put("text", "RDT ID: " + uniqueID);
        }
        // pre-populate rdt id field
        if (isRDTIdField(field)) {
            field.put(VALUE, uniqueID);
        }
    }

    protected void prePopulateRDTPatientFields(Patient patient, JSONObject field) throws JSONException {
        if (patient != null) {
            String key = field.getString(KEY);
            if (LBL_PATIENT_NAME.equals(key)) {
                String patientIdentifier = StringUtils.isBlank(patient.getPatientName())
                        ? patient.getPatientId() : patient.getPatientName();
                field.put(VALUE, patientIdentifier);
                field.put(TEXT, patientIdentifier);
            } else if (LBL_PATIENT_GENDER_AND_ID.equals(key)) {
                field.put(VALUE, patient.getPatientSex());
                field.put(TEXT, getPatientSexAndId(patient));
            }
        }
    }

    public static String getPatientSexAndId(Patient patient) {
        return patient.getPatientSex() + BULLET_DOT + "ID: " + patient.getBaseEntityId().split("-")[0];
    }

    public boolean isRDTIdField(JSONObject field) throws JSONException {
        return RDTApplication.getInstance().getStepStateConfiguration().getStepStateObj()
                .optString(RDT_ID_KEY).equals(field.getString(KEY));
    }

    public synchronized void getNextUniqueIds(final FormLaunchArgs args, final OnUniqueIdsFetchedCallback callBack, int numOfIDs) {
        class FetchUniqueIdTask extends AsyncTask<Void, Void, List<UniqueId>> {
            @Override
            protected  List<UniqueId> doInBackground(Void... voids) {
                return getUniqueIDs(numOfIDs);
            }

            @Override
            protected void onPostExecute(List<UniqueId> uniqueIds) {
                if (callBack != null) {
                    callBack.onUniqueIdsFetched(args, uniqueIds);
                }
            }
        }
        new FetchUniqueIdTask().execute();
    }

    private List<UniqueId> getUniqueIDs(int numOfIDs) {
        List<UniqueId> uniqueIds = new ArrayList<>();
        for (int i = 0; i < numOfIDs; i++) {
            UniqueId uniqueId = RDTApplication.getInstance().getContext().getUniqueIdRepository()
                    .getNextUniqueId();
            if (uniqueId != null) {
                uniqueIds.add(uniqueId);
            } else {
                uniqueIds.clear();
                break;
            }
        }
        return uniqueIds;
    }

    public static String appendEntityId(JSONObject jsonForm) throws JSONException {
        String entityId = JsonFormUtils.getString(jsonForm, Constants.FormFields.ENTITY_ID);

        if (!shouldAppendIDToForm(JsonFormUtils.getString(jsonForm, EVENT_TYPE))) { return entityId; }

        entityId = StringUtils.isBlank(entityId) ? UUID.randomUUID().toString() : entityId;
        jsonForm.put(Constants.FormFields.ENTITY_ID, entityId);
        return entityId;
    }

    private static Set<String> getFormsToAddEntityIdToIfBlank() {
        if (formsToAddEntityIdToIfBlank == null) {
            formsToAddEntityIdToIfBlank = initializeFormsThatRequireEntityId();
        }
        return formsToAddEntityIdToIfBlank;
    }

    protected static Set<String> initializeFormsThatRequireEntityId() {
       return new HashSet<>(Arrays.asList(Constants.Encounter.PATIENT_REGISTRATION));
    }

    private static boolean shouldAppendIDToForm(String eventType) {
        return getFormsToAddEntityIdToIfBlank().contains(eventType);
    }

    public static FormTag getFormTag() {
        AllSettings settings = RDTApplication.getInstance().getContext().allSettings();
        FormTag formTag = new FormTag();
        formTag.providerId = settings.fetchRegisteredANM();
        formTag.locationId = settings.fetchDefaultLocalityId(formTag.providerId);
        formTag.teamId = settings.fetchDefaultTeamId(formTag.providerId);;
        formTag.team = settings.fetchDefaultTeam(formTag.providerId);
        return formTag;
    }
}