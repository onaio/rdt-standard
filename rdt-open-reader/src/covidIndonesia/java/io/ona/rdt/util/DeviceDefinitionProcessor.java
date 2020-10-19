package io.ona.rdt.util;

import android.content.Context;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.DeviceDefinition;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Element;

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

    private DeviceDefinitionProcessor() {}

    public static DeviceDefinitionProcessor getInstance(Context context) throws IOException, FHIRParserException {
        if (deviceDefinitionProcessor == null) {
            deviceDefinitionProcessor = new DeviceDefinitionProcessor();
            PathEvaluatorLibrary.init(null, null, null, null);
        }
        // we need this to refresh the resource in case it changes in between two accesses
        deviceDefinitionBundle = getDeviceDefinitionBundle(context);

        return deviceDefinitionProcessor;
    }

    private static Bundle getDeviceDefinitionBundle(Context context) throws IOException, FHIRParserException  {
        InputStream stream = context.getAssets().open(CovidConstants.FHIRResource.DEVICE_RESOURCE_FILE);
        return FHIRParser.parser(Format.JSON).parse(stream);
    }

    public String extractDeviceInstructions(String deviceId) {
        String expression = String.format("$this.entry.resource.where(identifier.where(value='%s')).capability.where(type.where(text='%s')).description.text",
                deviceId, CovidConstants.FHIRResource.INSTRUCTIONS);
        return PathEvaluatorLibrary.getInstance().extractStringFromBundle(deviceDefinitionBundle, expression);
    }

    public String extractManufacturerName(String deviceId) {
        String expression = String.format("$this.entry.resource.where(identifier.where(value='%s')))", deviceId);
        DeviceDefinition deviceDefinition =  (DeviceDefinition) PathEvaluatorLibrary.getInstance().extractResourceFromBundle(deviceDefinitionBundle, expression);
        return deviceDefinition.getManufacturer().as(com.ibm.fhir.model.type.String.class).getValue();
    }

    public Map<String, String> createDeviceIDToDeviceNameMap() {
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
        deviceConfig.put(CovidConstants.FHIRResource.REF_IMG,
                deviceAssets.get(CovidConstants.FHIRResource.REF_IMG));
        deviceConfig.put(CovidConstants.FHIRResource.MIDDLE_LINE_NAME,
                deviceAssets.get(CovidConstants.FHIRResource.MIDDLE_LINE_NAME));
        deviceConfig.put(CovidConstants.FHIRResource.VIEW_FINDER_SCALE,
                deviceAssets.get(CovidConstants.FHIRResource.VIEW_FINDER_SCALE));
        deviceConfig.put(CovidConstants.FHIRResource.RESULT_WINDOW_BOTTOM_RIGHT,
                Utils.convertStringToJsonArr(deviceAssets.get(CovidConstants.FHIRResource.RESULT_WINDOW_BOTTOM_RIGHT)));
        deviceConfig.put(CovidConstants.FHIRResource.RESULT_WINDOW_TOP_LEFT,
                Utils.convertStringToJsonArr(deviceAssets.get(CovidConstants.FHIRResource.RESULT_WINDOW_TOP_LEFT)));
        deviceConfig.put(CovidConstants.FHIRResource.MIDDLE_LINE_POSITION,
                Utils.convertStringToJsonArr(deviceAssets.get(CovidConstants.FHIRResource.MIDDLE_LINE_POSITION)));
        deviceConfig.put(CovidConstants.FHIRResource.LINE_INTENSITY,
                deviceAssets.get(CovidConstants.FHIRResource.LINE_INTENSITY));
        deviceConfig.put(CovidConstants.FHIRResource.BOTTOM_LINE_POSITION,
                Utils.convertStringToJsonArr(deviceAssets.get(CovidConstants.FHIRResource.BOTTOM_LINE_POSITION)));
        deviceConfig.put(CovidConstants.FHIRResource.BOTTOM_LINE_NAME,
                deviceAssets.get(CovidConstants.FHIRResource.BOTTOM_LINE_NAME));
        deviceConfig.put(CovidConstants.FHIRResource.TOP_LINE_POSITION,
                Utils.convertStringToJsonArr(deviceAssets.get(CovidConstants.FHIRResource.TOP_LINE_POSITION)));
        deviceConfig.put(CovidConstants.FHIRResource.TOP_LINE_NAME,
                deviceAssets.get(CovidConstants.FHIRResource.TOP_LINE_NAME));
        return deviceConfig;
    }
}
