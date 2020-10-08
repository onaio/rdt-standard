package io.ona.rdt.util;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Bundle;

import org.junit.Assert;
import org.junit.Test;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.pathevaluator.PathEvaluatorLibrary;

import java.io.InputStream;

import io.ona.rdt.robolectric.RobolectricTest;

/**
 * Created by Vincent Karuri on 08/10/2020
 */
public class FHIRResourceProcessorTest extends RobolectricTest {

    @Test
    public void testFHIRQueriesOnDeviceDefinitionResource() throws Exception {
        PathEvaluatorLibrary.init(null, null, null, null);
        InputStream stream = RuntimeEnvironment.application.getAssets().open("DeviceDefinition.json");
        Bundle domainResource = FHIRParser.parser(Format.JSON).parse(stream);

        // extract instructions
        String expression = "$this.entry.resource.where(identifier.where(value='d3fdac0e-061e-b068-2bed-5a95e803636f')).capability.where(type.where(text='instructions')).description.text";
        Assert.assertEquals("Collect blood sample",
                PathEvaluatorLibrary
                        .getInstance()
                        .evaluateElementExpression(domainResource,
                                expression)
                        .getValue().asStringValue().string());
    }
}
