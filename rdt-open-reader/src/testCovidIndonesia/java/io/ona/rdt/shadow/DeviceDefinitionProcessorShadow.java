package io.ona.rdt.shadow;

import android.content.Context;

import com.ibm.fhir.model.parser.exception.FHIRParserException;

import org.json.JSONException;
import org.json.JSONObject;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.ona.rdt.util.DeviceDefinitionProcessor;

/**
 * Created by Vincent Karuri on 22/10/2020
 */

@Implements(DeviceDefinitionProcessor.class)
public class DeviceDefinitionProcessorShadow {

    private static JSONObject jsonObject;

    @Implementation
    public static DeviceDefinitionProcessor getInstance(Context context) throws IOException, FHIRParserException {
        try {
            DeviceDefinitionProcessor deviceDefinitionProcessor = Mockito.mock(DeviceDefinitionProcessor.class);
            Mockito.doReturn(jsonObject).when(deviceDefinitionProcessor).extractDeviceConfig(ArgumentMatchers.anyString());
            Mockito.doReturn(getDeviceIdToNameMap()).when(deviceDefinitionProcessor).getDeviceIDToDeviceNameMap();
            return deviceDefinitionProcessor;
        } catch (JSONException e) {
            // do nothing
        }
        return null;
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
}
