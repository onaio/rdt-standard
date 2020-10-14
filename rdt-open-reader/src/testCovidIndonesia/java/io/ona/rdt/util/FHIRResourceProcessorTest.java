package io.ona.rdt.util;

import android.graphics.Bitmap;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.DeviceDefinition;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Element;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.pathevaluator.PathEvaluatorLibrary;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.ona.rdt.robolectric.RobolectricTest;
import timber.log.Timber;

/**
 * Created by Vincent Karuri on 08/10/2020
 */
public class FHIRResourceProcessorTest extends RobolectricTest {

    @Test
    public void testFHIRQueriesOnDeviceDefinitionResource() throws Exception {
        PathEvaluatorLibrary.init(null, null, null, null);
        InputStream stream = RuntimeEnvironment.application.getAssets().open("DeviceDefinition.json");
        Bundle deviceDefinitionBundle = FHIRParser.parser(Format.JSON).parse(stream);

        // extract instructions
        Assert.assertEquals("Collect blood sample", extractDeviceInstructions(deviceDefinitionBundle, "d3fdac0e-061e-b068-2bed-5a95e803636f"));
        // extractDeviceName
        Assert.assertEquals("Wondfo SARS-CoV-2 Antibody Test", extractDeviceName(deviceDefinitionBundle, "d3fdac0e-061e-b068-2bed-5a95e803636f"));
        // extract manufacturer name
        Assert.assertEquals("Guangzhou Wondfo Biotech", extractManufacturerName(deviceDefinitionBundle, "d3fdac0e-061e-b068-2bed-5a95e803636f"));
        // extract device assets
//        Assert.assertEquals("", extractDeviceReferenceImage(deviceDefinitionBundle, "cf4443a1-f582-74ea-be89-ae53b5fd7bfe"));
        // extract device config
        Assert.assertEquals("", extractDeviceConfig(deviceDefinitionBundle, "cf4443a1-f582-74ea-be89-ae53b5fd7bfe"));

        // extract device IDs - device name map
        Map<String, String> deviceIDToDeviceName = createDeviceIDToDeviceNameMap(deviceDefinitionBundle, extractDeviceIds(deviceDefinitionBundle));
        Assert.assertEquals("Wondfo SARS-CoV-2 Antibody Test", deviceIDToDeviceName.get("d3fdac0e-061e-b068-2bed-5a95e803636f"));
        Assert.assertEquals("Alltest 2019-nCoV IgG/IgM", deviceIDToDeviceName.get("cf4443a1-f582-74ea-be89-ae53b5fd7bfe"));
        Assert.assertEquals("Green Spring COVID-19 IgG / IgM Rapid Test Kit", deviceIDToDeviceName.get("bcd01a98-36b2-e316-cea1-537745ae3439"));
        Assert.assertEquals("Realy Tech 2019-nCOV IgG/IgM", deviceIDToDeviceName.get("22a46031-0b56-a237-044a-76904b9b193e"));
    }

    private String extractDeviceInstructions(Bundle deviceDefinitionBundle, String deviceId) {
        String expression = String.format("$this.entry.resource.where(identifier.where(value='%s')).capability.where(type.where(text='%s')).description.text",
                deviceId, CovidConstants.FHIRResource.INSTRUCTIONS);
        return PathEvaluatorLibrary.getInstance().extractStringFromBundle(deviceDefinitionBundle, expression);
    }

    private String extractManufacturerName(Bundle deviceDefinitionBundle, String deviceId) {
        String expression = String.format("$this.entry.resource.where(identifier.where(value='%s')))", deviceId);
        DeviceDefinition deviceDefinition =  (DeviceDefinition) PathEvaluatorLibrary.getInstance().extractResourceFromBundle(deviceDefinitionBundle, expression);
        return deviceDefinition.getManufacturer().as(com.ibm.fhir.model.type.String.class).getValue();
    }

    private Map<String, String> createDeviceIDToDeviceNameMap(Bundle bundle, List<String> deviceIDs) {
        Map<String, String> deviceIDToDeviceName = new HashMap<>();
        for (String deviceID : deviceIDs) {
            deviceIDToDeviceName.put(deviceID, extractDeviceName(bundle, deviceID));
        }
        return deviceIDToDeviceName;
    }

    private List<String> extractDeviceIds(Bundle deviceDefinitionBundle) {
        String expression = "$this.entry.resource.identifier.value";
        return PathEvaluatorLibrary.getInstance().extractStringsFromBundle(deviceDefinitionBundle, expression);
    }

    private String extractDeviceName(Bundle deviceDefinitionBundle, String deviceId) {
        String expression = String.format("$this.entry.resource.where(identifier.where(value='%s')).deviceName.name", deviceId);
        return PathEvaluatorLibrary.getInstance().extractStringFromBundle(deviceDefinitionBundle, expression);
    }

    private Map<String, String> extractDeviceAssets(Bundle deviceDefinitionBundle, String deviceId) {
        Map<String, String> conceptKeyToValueMap = new HashMap<>();
        String expression = String.format("$this.entry.resource.where(identifier.where(value='%s')).property.where(type.where(text='%s')).valueCode",
                deviceId, CovidConstants.FHIRResource.RDT_SCAN_CONFIGURATION);
        List<Element> configurationElements = PathEvaluatorLibrary.getInstance().extractElementsFromBundle(deviceDefinitionBundle, expression);
        for (Element configurableElement : configurationElements) {
            CodeableConcept codeableConcept = (CodeableConcept) configurableElement;
            String key = codeableConcept.getCoding().get(0).getCode().getValue();
            String value = codeableConcept.getText().getValue();
            conceptKeyToValueMap.put(key, value);
        }
        return conceptKeyToValueMap;
    }

    private Bitmap extractDeviceReferenceImage(Bundle deviceDefinitionBundle, String deviceId) throws Exception {
        String encodedRefImage = extractDeviceAssets(deviceDefinitionBundle, deviceId).get(CovidConstants.FHIRResource.REF_IMG);
        byte[] decodedString = Base64.getMimeDecoder().decode(encodedRefImage);

        FileOutputStream osf = new FileOutputStream(new File("/tmp/ref_img.jpeg"));
        osf.write(decodedString);
        osf.flush();

//        Bitmap bitmap = RDTJsonFormUtils.convertByteArrayToBitmap(decodedString);
//        RDTJsonFormUtils.writeImageToDisk("/tmp/", bitmap, RuntimeEnvironment.application);
//        RDTJsonFormUtils.writeImageToDisk("/tmp/", BitmapFactory.decodeFile("/tmp/ref_img.jpeg"), RuntimeEnvironment.application);
        return null;
    }

    private JSONObject extractDeviceConfig(Bundle deviceDefinitionBundle, String deviceId) throws JSONException {
        JSONObject deviceConfig = new JSONObject();
        Map<String, String> deviceAssets = extractDeviceAssets(deviceDefinitionBundle, deviceId);
        deviceConfig.put(CovidConstants.FHIRResource.REF_IMG,
                deviceAssets.get(CovidConstants.FHIRResource.REF_IMG));
        deviceConfig.put(CovidConstants.FHIRResource.MIDDLE_LINE_NAME,
                deviceAssets.get(CovidConstants.FHIRResource.MIDDLE_LINE_NAME));
        deviceConfig.put(CovidConstants.FHIRResource.VIEW_FINDER_SCALE,
                deviceAssets.get(CovidConstants.FHIRResource.VIEW_FINDER_SCALE));
        deviceConfig.put(CovidConstants.FHIRResource.RESULT_WINDOW_BOTTOM_RIGHT,
                convertStringToJsonArr(deviceAssets.get(CovidConstants.FHIRResource.RESULT_WINDOW_BOTTOM_RIGHT)));
        deviceConfig.put(CovidConstants.FHIRResource.RESULT_WINDOW_TOP_LEFT,
                convertStringToJsonArr(deviceAssets.get(CovidConstants.FHIRResource.RESULT_WINDOW_TOP_LEFT)));
        deviceConfig.put(CovidConstants.FHIRResource.MIDDLE_LINE_POSITION,
                convertStringToJsonArr(deviceAssets.get(CovidConstants.FHIRResource.MIDDLE_LINE_POSITION)));
        deviceConfig.put(CovidConstants.FHIRResource.LINE_INTENSITY,
                deviceAssets.get(CovidConstants.FHIRResource.LINE_INTENSITY));
        deviceConfig.put(CovidConstants.FHIRResource.BOTTOM_LINE_POSITION,
                convertStringToJsonArr(deviceAssets.get(CovidConstants.FHIRResource.BOTTOM_LINE_POSITION)));
        deviceConfig.put(CovidConstants.FHIRResource.BOTTOM_LINE_NAME,
                deviceAssets.get(CovidConstants.FHIRResource.BOTTOM_LINE_NAME));
        deviceConfig.put(CovidConstants.FHIRResource.TOP_LINE_POSITION,
                convertStringToJsonArr(deviceAssets.get(CovidConstants.FHIRResource.TOP_LINE_POSITION)));
        deviceConfig.put(CovidConstants.FHIRResource.TOP_LINE_NAME,
                deviceAssets.get(CovidConstants.FHIRResource.TOP_LINE_NAME));
        return deviceConfig;
    }

    private JSONArray convertStringToJsonArr(String str) {
        try {
            return new JSONArray(str);
        } catch (JSONException e) {
            Timber.e(e);
            return null;
        }
    }
}
