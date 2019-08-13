package io.ona.rdt_app.widget;

import android.view.View;

import com.vijay.jsonwizard.domain.WidgetArgs;
import com.vijay.jsonwizard.interfaces.JsonApi;

import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * Created by Vincent Karuri on 13/08/2019
 */
public class RDTExpirationDateReaderFactoryTest {

    private RDTExpirationDateReaderFactory readerFactory;

    @Before
    public void setUp() {
        readerFactory = new RDTExpirationDateReaderFactory();
    }

    @Test
    public void testPopulateRelevantFieldsShouldPopulateCorrectFields() throws Exception {
        View rootLayout = mock(View.class);
        doReturn("key").when(rootLayout).getTag(eq(com.vijay.jsonwizard.R.id.key));
        doReturn("entity_parent").when(rootLayout).getTag(eq(com.vijay.jsonwizard.R.id.openmrs_entity_parent));
        doReturn("openmrs_entity").when(rootLayout).getTag(eq(com.vijay.jsonwizard.R.id.openmrs_entity));
        doReturn("openmrs_entity_id").when(rootLayout).getTag(eq(com.vijay.jsonwizard.R.id.openmrs_entity_id));
        Whitebox.setInternalState(readerFactory, "rootLayout", rootLayout);

        WidgetArgs widgetArgs = new WidgetArgs();
        widgetArgs.withStepName("step1").withPopup(false);
        Whitebox.setInternalState(readerFactory, "widgetArgs", widgetArgs);

        JsonApi jsonApi = spy(new TestJsonApi());
        Whitebox.invokeMethod(readerFactory, "populateRelevantFields", jsonApi, "value");

        verify(jsonApi).writeValue(eq("step1"), eq("key"), eq("value"), eq("entity_parent"), eq("openmrs_entity"), eq("openmrs_entity_id"), eq(false));
    }
}
