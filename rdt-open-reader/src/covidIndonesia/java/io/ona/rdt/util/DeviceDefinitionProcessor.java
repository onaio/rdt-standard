package io.ona.rdt.util;

import android.content.Context;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.DeviceDefinition;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Element;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.pathevaluator.PathEvaluatorLibrary;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Vincent Karuri on 15/10/2020
 */
public class DeviceDefinitionProcessor {

    private static DeviceDefinitionProcessor deviceDefinitionProcessor;
    private static Bundle deviceDefinitionBundle;

    private DeviceDefinitionProcessor() {
    }

    public static DeviceDefinitionProcessor getInstance(Context context) throws IOException, FHIRParserException {
       return getInstance(context, true);
    }

    public static DeviceDefinitionProcessor getInstance(Context context, boolean refreshDeviceDefinitionBundle) throws IOException, FHIRParserException {
        if (deviceDefinitionProcessor == null) {
            deviceDefinitionProcessor = new DeviceDefinitionProcessor();
        }

        if (refreshDeviceDefinitionBundle) {
            // we may need this to refresh the resource in case it changes in between two accesses
            deviceDefinitionBundle = getDeviceDefinitionBundle(context);
        }

        return deviceDefinitionProcessor;
    }

    private static Bundle getDeviceDefinitionBundle(Context context) throws IOException, FHIRParserException {
        InputStream stream = context.getAssets().open(CovidConstants.FHIRResource.DEVICE_RESOURCE_FILE);
        return FHIRParser.parser(Format.JSON).parse(stream);
    }

    public String getDeviceId(String productCode) {
        String expression = String.format("$this.entry.resource.where(udiDeviceIdentifier.where(deviceIdentifier='%s')).identifier.value", productCode);
        return PathEvaluatorLibrary.getInstance().extractStringFromBundle(deviceDefinitionBundle, expression);
    }

    public String extractDeviceInstructions(String deviceId) {
        String expression = String.format("$this.entry.resource.where(identifier.where(value='%s')).capability.where(type.where(text='%s')).description.text",
                deviceId, CovidConstants.FHIRResource.INSTRUCTIONS);
        return PathEvaluatorLibrary.getInstance().extractStringFromBundle(deviceDefinitionBundle, expression);
    }

    public String extractManufacturerName(String deviceId) {
        String expression = String.format("$this.entry.resource.where(identifier.where(value='%s')))", deviceId);
        DeviceDefinition deviceDefinition = (DeviceDefinition) PathEvaluatorLibrary.getInstance().extractResourceFromBundle(deviceDefinitionBundle, expression);
        return deviceDefinition.getManufacturer().as(com.ibm.fhir.model.type.String.class).getValue();
    }

    public Map<String, String> getDeviceIDToDeviceNameMap() {
        Map<String, String> deviceIDToDeviceName = new LinkedHashMap<>();
        for (String deviceID : extractDeviceIds()) {
            deviceIDToDeviceName.put(deviceID, extractDeviceName(deviceID));
        }
        return deviceIDToDeviceName;
    }

    public List<String> extractDeviceIds() {
        String expression = "$this.entry.resource.identifier.value";
        return PathEvaluatorLibrary.getInstance().extractStringsFromBundle(deviceDefinitionBundle, expression);
    }

    public String extractDeviceName(String deviceId) {
        String expression = String.format("$this.entry.resource.where(identifier.where(value='%s')).deviceName.name", deviceId);
        return PathEvaluatorLibrary.getInstance().extractStringFromBundle(deviceDefinitionBundle, expression);
    }

    public Map<String, String> extractDeviceAssets(String deviceId) {
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

    public JSONObject extractDeviceConfig(String deviceId) throws JSONException {
        JSONObject deviceConfig = new JSONObject();
        Map<String, String> deviceAssets = extractDeviceAssets(deviceId);
        for (Map.Entry<String, String> keyValPair : deviceAssets.entrySet()) {
            String val = keyValPair.getValue();
            JSONArray valAsJsonArr = Utils.convertToJsonArr(val);
            deviceConfig.put(keyValPair.getKey(), valAsJsonArr == null ? val : valAsJsonArr);
        }
        return deviceConfig.length() == 0 ? null : deviceConfig;
    }
}
