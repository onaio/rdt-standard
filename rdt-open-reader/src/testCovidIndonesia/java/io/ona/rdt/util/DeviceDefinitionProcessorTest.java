package io.ona.rdt.util;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Bundle;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.pathevaluator.PathEvaluatorLibrary;

import java.io.InputStream;
import java.util.Map;

import io.ona.rdt.robolectric.RobolectricTest;

/**
 * Created by Vincent Karuri on 08/10/2020
 */
public class DeviceDefinitionProcessorTest extends RobolectricTest {

    private DeviceDefinitionProcessor deviceDefinitionProcessor;

    @Before
    public void setUp() throws Exception {
        deviceDefinitionProcessor = DeviceDefinitionProcessor.getInstance(RuntimeEnvironment.application);
    }

    @Test
    public void testFHIRQueriesOnDeviceDefinitionResource() throws Exception {
        PathEvaluatorLibrary.init(null, null, null, null);
        InputStream stream = RuntimeEnvironment.application.getAssets().open("DeviceDefinition.json");
        Bundle deviceDefinitionBundle = FHIRParser.parser(Format.JSON).parse(stream);
        ReflectionHelpers.setField(deviceDefinitionProcessor, "deviceDefinitionBundle", deviceDefinitionBundle);

        // extract instructions
        Assert.assertEquals("Collect blood sample",
                deviceDefinitionProcessor.extractDeviceInstructions("d3fdac0e-061e-b068-2bed-5a95e803636f"));
        // extractDeviceName
        Assert.assertEquals("Wondfo SARS-CoV-2 Antibody Test",
                deviceDefinitionProcessor.extractDeviceName("d3fdac0e-061e-b068-2bed-5a95e803636f"));
        // extract manufacturer name
        Assert.assertEquals("Guangzhou Wondfo Biotech",
                deviceDefinitionProcessor.extractManufacturerName("d3fdac0e-061e-b068-2bed-5a95e803636f"));
        // extract device config
//        Assert.assertEquals("", deviceDefinitionProcessor.extractDeviceConfig("cf4443a1-f582-74ea-be89-ae53b5fd7bfe"));

        // extract device IDs - device name map
        Map<String, String> deviceIDToDeviceName =
                deviceDefinitionProcessor.createDeviceIDToDeviceNameMap(deviceDefinitionProcessor.extractDeviceIds());
        Assert.assertEquals("Wondfo SARS-CoV-2 Antibody Test", deviceIDToDeviceName.get("d3fdac0e-061e-b068-2bed-5a95e803636f"));
        Assert.assertEquals("Alltest 2019-nCoV IgG/IgM", deviceIDToDeviceName.get("cf4443a1-f582-74ea-be89-ae53b5fd7bfe"));
        Assert.assertEquals("Green Spring COVID-19 IgG / IgM Rapid Test Kit", deviceIDToDeviceName.get("bcd01a98-36b2-e316-cea1-537745ae3439"));
        Assert.assertEquals("Realy Tech 2019-nCOV IgG/IgM", deviceIDToDeviceName.get("22a46031-0b56-a237-044a-76904b9b193e"));
    }
}
