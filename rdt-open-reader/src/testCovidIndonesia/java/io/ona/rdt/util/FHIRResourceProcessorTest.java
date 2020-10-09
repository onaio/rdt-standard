package io.ona.rdt.util;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.DeviceDefinition;

import org.junit.Assert;
import org.junit.Test;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.pathevaluator.PathEvaluatorLibrary;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.ona.rdt.robolectric.RobolectricTest;

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

        // extract device IDs - device name map
        Map<String, String> deviceIDToDeviceName = createDeviceIDToDeviceNameMap(deviceDefinitionBundle, extractDeviceIds(deviceDefinitionBundle));
        Assert.assertEquals("Wondfo SARS-CoV-2 Antibody Test", deviceIDToDeviceName.get("d3fdac0e-061e-b068-2bed-5a95e803636f"));
        Assert.assertEquals("Alltest 2019-nCoV IgG/IgM", deviceIDToDeviceName.get("cf4443a1-f582-74ea-be89-ae53b5fd7bfe"));
        Assert.assertEquals("Green Spring COVID-19 IgG / IgM Rapid Test Kit", deviceIDToDeviceName.get("bcd01a98-36b2-e316-cea1-537745ae3439"));
        Assert.assertEquals("Realy Tech 2019-nCOV IgG/IgM", deviceIDToDeviceName.get("22a46031-0b56-a237-044a-76904b9b193e"));
    }

    private String extractDeviceInstructions(Bundle deviceDefinitionBundle, String deviceId) {
        String expression = String.format("$this.entry.resource.where(identifier.where(value='%s')).capability.where(type.where(text='instructions')).description.text", deviceId);
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
}
