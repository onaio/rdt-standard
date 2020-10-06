package io.ona.rdt.robolectric.widget;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.domain.WidgetArgs;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.interfaces.OnActivityResultListener;
import com.vijay.jsonwizard.widgets.RDTCaptureFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLooper;

import io.ona.rdt.activity.RDTExpirationDateActivity;
import io.ona.rdt.activity.RDTJsonFormActivity;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.robolectric.shadow.ContextCompatShadow;
import io.ona.rdt.util.StepStateConfig;
import io.ona.rdt.widget.RDTExpirationDateReaderFactory;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.vijay.jsonwizard.constants.JsonFormConstants.RDT_CAPTURE_CODE;
import static io.ona.rdt.util.Constants.RDTType.ONA_RDT;
import static io.ona.rdt.util.Constants.Result.EXPIRATION_DATE;
import static io.ona.rdt.util.Constants.Result.EXPIRATION_DATE_RESULT;
import static io.ona.rdt.util.Constants.Step.PRODUCT_EXPIRED_PAGE;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.support.membermodification.MemberMatcher.methods;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;
import static org.smartregister.util.JsonFormUtils.ENTITY_ID;

/**
 * Created by Vincent Karuri on 13/08/2019
 */

@Config(shadows = {ContextCompatShadow.class})
public class RDTExpirationDateReaderFactoryTest extends WidgetFactoryRobolectricTest {

    private RDTExpirationDateReaderFactory readerFactory;
    private WidgetArgs widgetArgs;
    private View rootLayout;

    @Mock
    private StepStateConfig stepStateConfig;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        jsonFormActivity = Mockito.spy(jsonFormActivity);
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
        readerFactory.conditionallyMoveToNextStep(widgetArgs.getFormFragment(), "step1", false);
        verify(widgetArgs.getFormFragment()).next();
    }

    @Test
    public void testConditionallyMoveToNextStepShouldMoveToStep1() throws Exception {
        readerFactory.conditionallyMoveToNextStep(widgetArgs.getFormFragment(), "step1", true);
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
        Bundle extras = mock(Bundle.class);
        doReturn(true).when(extras).getBoolean(eq(EXPIRATION_DATE_RESULT));
        doReturn("3101999").when(extras).getString(eq(EXPIRATION_DATE));
        doReturn(extras).when(data).getExtras();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(PRODUCT_EXPIRED_PAGE, "step10");
        doReturn(jsonObject).when(stepStateConfig).getStepStateObj();

        Whitebox.setInternalState(readerFactory, "stepStateConfig", stepStateConfig);

        readerFactory.onActivityResult(RDT_CAPTURE_CODE, RESULT_OK, data);
        verify(jsonFormActivity, atLeastOnce()).writeValue(anyString(), anyString(), eq("3101999"), anyString(), anyString(), anyString(), eq(false));
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

    @Test
    public void testGetViewsFromJson() throws Exception {
        JSONObject jsonObject = widgetArgs.getJsonObject();
        suppress(methods(RDTCaptureFactory.class, "getViewsFromJson"));
        doReturn(jsonObject).when(jsonFormActivity).getmJSONObject();

        JsonFormFragment formFragment = mock(JsonFormFragment.class);
        doReturn(true).when(formFragment).isVisible();
        readerFactory.getViewsFromJson("step1", jsonFormActivity, formFragment,
                jsonObject, mock(CommonListener.class));

        WidgetArgs actualWidgetArgs =  Whitebox.getInternalState(readerFactory, "widgetArgs");
        assertEquals(formFragment, actualWidgetArgs.getFormFragment());
        assertEquals(jsonObject, actualWidgetArgs.getJsonObject());
        assertEquals(jsonFormActivity, actualWidgetArgs.getContext());
        assertEquals("step1", actualWidgetArgs.getStepName());

        // verify camera is launched
        Intent expectedIntent = new Intent(jsonFormActivity, RDTExpirationDateActivity.class);
        Intent actualIntent = Shadows.shadowOf(RDTApplication.getInstance()).getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());

        // verify camera closes out after timeout
        RDTExpirationDateActivity rdtExpirationDateActivity = mock(RDTExpirationDateActivity.class);
        RDTApplication.getInstance().setCurrentActivity(rdtExpirationDateActivity);
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
        verify(rdtExpirationDateActivity).setResult(eq(RESULT_OK));
        verify(rdtExpirationDateActivity).finish();
        verify(formFragment).transactThis(any(JsonFormFragment.class));
    }

    private void setWidgetArgs() throws JSONException {
        widgetArgs = new WidgetArgs();
        RDTJsonFormFragment formFragment = mock(RDTJsonFormFragment.class);
        doReturn("rdt_type").when(jsonFormActivity).getRdtType();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JsonFormConstants.OPENMRS_ENTITY_PARENT, "entity_parent");
        jsonObject.put(JsonFormConstants.OPENMRS_ENTITY, "entity");
        jsonObject.put(JsonFormConstants.OPENMRS_ENTITY_ID, "entity_id");
        jsonObject.put(JsonFormConstants.KEY, "key");
        jsonObject.put(ENTITY_ID, "entity_id");

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

    @Test
    public void testGetCustomTranslatableWidgetFieldsShouldReturnNonNullSet() {
        Assert.assertNotNull(readerFactory.getCustomTranslatableWidgetFields());
    }
}
