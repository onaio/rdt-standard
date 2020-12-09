package io.ona.rdt.shadow;

import org.json.JSONException;
import org.json.JSONObject;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

import java.util.HashMap;
import java.util.Map;

import io.ona.rdt.util.DeviceDefinitionProcessor;

/**
 * Created by Vincent Karuri on 22/10/2020
 */

@Implements(DeviceDefinitionProcessor.class)
public class DeviceDefinitionProcessorShadow {

    private static JSONObject jsonObject;
    public static final String DEVICE_ID = "device_id";
    public static final String MANUFACTURER = "manufacturer";
    public static final String DEVICE_NAME = "device_name";

    @Implementation
    public JSONObject extractDeviceConfig(String deviceId) throws JSONException {
        return jsonObject;
    }

    @Implementation
    public Map<String, String> getDeviceIDToDeviceNameMap() {
        return getDeviceIdToNameMap();
    }

    @Implementation
    public String getDeviceId(String productCode) {
        return DEVICE_ID;
    }

    @Implementation
    public String extractManufacturerName(String deviceId) {
        return MANUFACTURER;
    }

    @Implementation
    public String extractDeviceName(String deviceId) {
        return DEVICE_NAME;
    }

    public static Map<String, String> getDeviceIdToNameMap() {
        Map<String, String> deviceIdToName = new HashMap<>();
        deviceIdToName.put("rdtId1", "rdtName1");
        deviceIdToName.put("rdtId2", "rdtName2");
        deviceIdToName.put("rdtId3", "rdtName3");
        return deviceIdToName;
    }

    public static void setJSONObject(JSONObject jsonObject) {
        DeviceDefinitionProcessorShadow.jsonObject = jsonObject;
    }

    public static JSONObject getJsonObject() {
        return jsonObject;
    }
}
