package io.ona.rdt.widget;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.domain.WidgetArgs;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.JsonApi;
import com.vijay.jsonwizard.interfaces.OnActivityResultListener;
import com.vijay.jsonwizard.widgets.RDTCaptureFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import io.ona.rdt.activity.RDTJsonFormActivity;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.stub.JsonApiStub;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.vijay.jsonwizard.constants.JsonFormConstants.RDT_CAPTURE_CODE;
import static io.ona.rdt.util.Constants.EXPIRATION_DATE;
import static io.ona.rdt.util.Constants.EXPIRATION_DATE_RESULT;
import static io.ona.rdt.util.Constants.ONA_RDT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.support.membermodification.MemberMatcher.methods;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;

/**
 * Created by Vincent Karuri on 13/08/2019
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({RDTJsonFormFragment.class})
public class RDTExpirationDateReaderFactoryTest {

    private RDTExpirationDateReaderFactory readerFactory;
    private WidgetArgs widgetArgs;
    private RDTJsonFormActivity jsonFormActivity;
    private View rootLayout;

    @Before
    public void setUp() throws JSONException {
        readerFactory = new RDTExpirationDateReaderFactory();
        setWidgetArgs();
    }

    @Test
    public void testPopulateRelevantFieldsShouldPopulateCorrectFields() throws Exception {
        Whitebox.invokeMethod(readerFactory, "populateRelevantFields", "value");
        verify(jsonFormActivity).writeValue(eq("step1"), eq("key"), eq("value"), eq("entity_parent"), eq("openmrs_entity"), eq("openmrs_entity_id"), eq(false));
    }

    @Test
    public void testConditionallyMoveToNextStepShouldMoveToNextStep() throws Exception {
        Whitebox.invokeMethod(readerFactory, "conditionallyMoveToNextStep", true);
        verify(widgetArgs.getFormFragment()).next();
    }

    @Test
    public void testConditionallyMoveToNextStepShouldMoveToStep1() throws Exception {
        Whitebox.invokeMethod(readerFactory, "conditionallyMoveToNextStep", false);
        verify(widgetArgs.getFormFragment()).transactThis(any(RDTJsonFormFragment.class));
    }

    @Test
    public void testSetUpRDTExpirationDateActivity() throws JSONException {
        suppress(methods(RDTCaptureFactory.class, "setUpRDTCaptureActivity"));
        readerFactory.setUpRDTExpirationDateActivity();
        verify(jsonFormActivity).addOnActivityResultListener(eq(RDT_CAPTURE_CODE), any(OnActivityResultListener.class));
    }

    @Test
    public void testAddWidgetTags() throws Exception {
        Whitebox.invokeMethod(readerFactory, "addWidgetTags");
        verify(rootLayout).setTag(com.vijay.jsonwizard.R.id.key, "key");
        verify(rootLayout).setTag(com.vijay.jsonwizard.R.id.openmrs_entity_parent, "entity_parent");
        verify(rootLayout).setTag(com.vijay.jsonwizard.R.id.openmrs_entity, "entity");
        verify(rootLayout).setTag(com.vijay.jsonwizard.R.id.openmrs_entity_id, "entity_id");
    }

    @Test
    public void testOnActivityResultShouldPerformAppropriateActions() throws JSONException {
        Intent data = mock(Intent.class);
        Bundle extras = new Bundle();
        extras.putBoolean(EXPIRATION_DATE_RESULT, false);
        extras.putString(EXPIRATION_DATE, "31012999");
        doReturn(extras).when(data).getExtras();
        readerFactory.onActivityResult(RDT_CAPTURE_CODE, RESULT_OK, data);
        verify(jsonFormActivity, atLeastOnce()).writeValue(anyString(), anyString(), isNull(), anyString(), anyString(), anyString(), eq(false));
        verify(widgetArgs.getFormFragment()).transactThis(any(RDTJsonFormFragment.class));
    }

    @Test
    public void testOnActivityResultShouldResetRDTTypeAndMoveBackOneStep() throws JSONException {
        Intent data = mock(Intent.class);
        Bundle extras = new Bundle();
        extras.putBoolean(EXPIRATION_DATE_RESULT, false);
        extras.putString(EXPIRATION_DATE, "31012999");
        doReturn(extras).when(data).getExtras();
        readerFactory.onActivityResult(RDT_CAPTURE_CODE, RESULT_CANCELED, data);
        verify((RDTJsonFormFragment) widgetArgs.getFormFragment()).setMoveBackOneStep(eq(true));
        verify((RDTJsonFormActivity) widgetArgs.getContext()).setRdtType(eq(ONA_RDT));
    }

    private void setWidgetArgs() throws JSONException {
        widgetArgs = new WidgetArgs();
        RDTJsonFormFragment formFragment = mock(RDTJsonFormFragment.class);
        jsonFormActivity = mock(RDTJsonFormActivity.class);
        doReturn("rdt_type").when(jsonFormActivity).getRdtType();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JsonFormConstants.OPENMRS_ENTITY_PARENT, "entity_parent");
        jsonObject.put(JsonFormConstants.OPENMRS_ENTITY, "entity");
        jsonObject.put(JsonFormConstants.OPENMRS_ENTITY_ID, "entity_id");
        jsonObject.put(JsonFormConstants.KEY, "key");

        widgetArgs.withFormFragment(formFragment)
                .withContext(jsonFormActivity)
                .withStepName("step1")
                .withJsonObject(jsonObject);

        rootLayout = mock(View.class);
        doReturn("key").when(rootLayout).getTag(eq(com.vijay.jsonwizard.R.id.key));
        doReturn("entity_parent").when(rootLayout).getTag(eq(com.vijay.jsonwizard.R.id.openmrs_entity_parent));
        doReturn("openmrs_entity").when(rootLayout).getTag(eq(com.vijay.jsonwizard.R.id.openmrs_entity));
        doReturn("openmrs_entity_id").when(rootLayout).getTag(eq(com.vijay.jsonwizard.R.id.openmrs_entity_id));
        Whitebox.setInternalState(readerFactory, "rootLayout", rootLayout);

        Whitebox.setInternalState(readerFactory, "widgetArgs", widgetArgs);
    }
}
