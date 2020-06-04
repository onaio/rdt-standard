package io.ona.rdt.widget;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.emredavarci.circleprogressbar.CircleProgressBar;
import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.domain.WidgetArgs;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.utils.FormUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import io.ona.rdt.PowerMockTest;
import io.ona.rdt.R;
import io.ona.rdt.activity.RDTJsonFormActivity;
import io.ona.rdt.fragment.RDTJsonFormFragment;

import static io.ona.rdt.widget.RDTCountdownTimerFactory.COUNTDOWN_TIMER_RESULT_READY_KEY;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.smartregister.util.JsonFormUtils.ENTITY_ID;

/**
 * Created by Vincent Karuri on 13/08/2019
 */
@PrepareForTest({RingtoneManager.class, LayoutInflater.class, FormUtils.class})
public class RDTCountdownTimerFactoryTest extends PowerMockTest {

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
        mockStaticClasses();
    }

    @Test
    public void testOnCountdownFinishSetsText() throws Exception {
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

    private void mockStaticClasses() {
        mockStatic(RingtoneManager.class);
        PowerMockito.when(RingtoneManager.getDefaultUri(anyInt())).thenReturn(mock(Uri.class));
        PowerMockito.when(RingtoneManager.getRingtone(any(Context.class), any(Uri.class))).thenReturn(mock(Ringtone.class));

        mockStatic(LayoutInflater.class);
        LayoutInflater layoutInflater = mock(LayoutInflater.class);
        PowerMockito.when(LayoutInflater.from(any(Context.class))).thenReturn(layoutInflater);
        View rootLayout = mock(View.class);
        doReturn(rootLayout).when(layoutInflater).inflate(anyInt(), isNull());
        doReturn(mock(TextView.class)).when(rootLayout).findViewById(R.id.timerLabel);
        doReturn(mock(CircleProgressBar.class)).when(rootLayout).findViewById(R.id.progressCircularBar);

        mockStatic(FormUtils.class);
        PowerMockito.when(FormUtils.spToPx(any(), anyInt())).thenReturn(0);
    }
}
