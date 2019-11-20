package io.ona.rdt.widget;

import android.content.Context;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.domain.WidgetArgs;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.widgets.CountDownTimerFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import io.ona.rdt.R;
import io.ona.rdt.activity.RDTJsonFormActivity;
import io.ona.rdt.fragment.RDTJsonFormFragment;

import static io.ona.rdt.widget.RDTCountdownTimerFactory.COUNTDOWN_TIMER_RESULT_READY_KEY;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.support.membermodification.MemberMatcher.methods;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;
import static org.smartregister.util.JsonFormUtils.ENTITY_ID;

/**
 * Created by Vincent Karuri on 13/08/2019
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({RDTCountdownTimerFactory.class})
public class RDTCountdownTimerFactoryTest {

    private RDTCountdownTimerFactory countdownTimerFactory;

    @Mock
    private WidgetArgs widgetArgs;
    @Mock
    private RDTJsonFormActivity jsonFormActivity;
    @Mock
    private View rootLayout;

    @Before
    public void setUp() {
        countdownTimerFactory = new RDTCountdownTimerFactory();
    }

    @Test
    public void testOnCountdownFinishSetsText() throws Exception {
        suppress(methods(CountDownTimerFactory.class, "onCountdownFinish"));

        WidgetArgs widgetArgs = new WidgetArgs();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JsonFormConstants.KEY, COUNTDOWN_TIMER_RESULT_READY_KEY);
        Context context = setUpDummyVibrator();
        widgetArgs.withContext(context)
                .withJsonObject(jsonObject);

        Whitebox.setInternalState(countdownTimerFactory, "widgetArgs", widgetArgs);

        TextView tvInstructions = mock(TextView.class);
        doReturn("instructions").when(context).getString(anyInt());

        View rootLayout = mock(View.class);
        Whitebox.setInternalState(countdownTimerFactory, "rootLayout", rootLayout);
        doReturn(tvInstructions).when(rootLayout).findViewById(R.id.timerLabel);
        countdownTimerFactory.onCountdownFinish(context);

        verify(tvInstructions).setText(eq("instructions"));
    }

    @Test
    public void testOnCountdownFinishMovesToNextStep() throws Exception {
        WidgetArgs widgetArgs = new WidgetArgs();

        JsonFormFragment formFragment = mock(JsonFormFragment.class);
        JSONObject jsonObject = new JSONObject();

        Context context = setUpDummyVibrator();

        widgetArgs.withJsonObject(jsonObject)
                .withFormFragment(formFragment)
                .withContext(context);

        Whitebox.setInternalState(countdownTimerFactory, "widgetArgs", widgetArgs);
        Whitebox.invokeMethod(countdownTimerFactory, "performOnExpiredAction", mock(Context.class), mock(TextView.class));
        verify(formFragment).next();
    }

    @Test
    public void testGetViewsFromJson() throws Exception {
        suppress(methods(CountDownTimerFactory.class, "getViewsFromJson"));
        setWidgetArgs();
        countdownTimerFactory.getViewsFromJson("step1", jsonFormActivity, widgetArgs.getFormFragment(),
                widgetArgs.getJsonObject(), mock(CommonListener.class), false);

        WidgetArgs actualWidgetArgs =  Whitebox.getInternalState(countdownTimerFactory, "widgetArgs");
        assertEquals(widgetArgs.getFormFragment(), actualWidgetArgs.getFormFragment());
        assertEquals(widgetArgs.getJsonObject(), actualWidgetArgs.getJsonObject());
        assertEquals(jsonFormActivity, actualWidgetArgs.getContext());
    }

    private Context setUpDummyVibrator() {
        Context context = mock(Context.class);
        Vibrator vibrator = mock(Vibrator.class);
        doNothing().when(vibrator).vibrate(any());
        doReturn(vibrator).when(context).getSystemService(Context.VIBRATOR_SERVICE);

        return context;
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
        Whitebox.setInternalState(countdownTimerFactory, "rootLayout", rootLayout);
        Whitebox.setInternalState(countdownTimerFactory, "widgetArgs", widgetArgs);
    }
}
