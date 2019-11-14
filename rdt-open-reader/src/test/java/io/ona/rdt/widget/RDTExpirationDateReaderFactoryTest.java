package io.ona.rdt.widget;

import android.view.View;

import com.vijay.jsonwizard.domain.WidgetArgs;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.JsonApi;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.stub.JsonApiStub;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created by Vincent Karuri on 13/08/2019
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({RDTJsonFormFragment.class})
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

        JsonApi jsonApi = spy(new JsonApiStub());
        Whitebox.invokeMethod(readerFactory, "populateRelevantFields", jsonApi, "value");

        verify(jsonApi).writeValue(eq("step1"), eq("key"), eq("value"), eq("entity_parent"), eq("openmrs_entity"), eq("openmrs_entity_id"), eq(false));
    }

    @Test
    public void testConditionallyMoveToNextStepShouldMoveToNextStep() throws Exception {
        JsonFormFragment formFragment = mock(JsonFormFragment.class);
        WidgetArgs args = new WidgetArgs();
        args.withFormFragment(formFragment);
        Whitebox.setInternalState(readerFactory, "widgetArgs", args);

        Whitebox.invokeMethod(readerFactory, "conditionallyMoveToNextStep", true);
        verify(formFragment).next();
    }

    @Test
    public void testConditionallyMoveToNextStepShouldMoveToStep1() throws Exception {
        JsonFormFragment formFragment = mock(JsonFormFragment.class);
        WidgetArgs args = new WidgetArgs();
        args.withFormFragment(formFragment);
        Whitebox.setInternalState(readerFactory, "widgetArgs", args);

        Whitebox.setInternalState(readerFactory, "jsonObject", new JSONObject());

        mockStatic(RDTJsonFormFragment.class);
        when(RDTJsonFormFragment.getFormFragment(anyString())).thenReturn(formFragment);

        Whitebox.invokeMethod(readerFactory, "conditionallyMoveToNextStep", false);
        verify(formFragment).transactThis(eq(formFragment));
    }
}
