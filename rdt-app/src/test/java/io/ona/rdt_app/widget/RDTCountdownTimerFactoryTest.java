package io.ona.rdt_app.widget;

import android.content.Context;
import android.os.Vibrator;
import android.widget.TextView;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.domain.WidgetArgs;
import com.vijay.jsonwizard.fragments.JsonFormFragment;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import static io.ona.rdt_app.widget.RDTCountdownTimerFactory.COUNTDOWN_TIMER_RESULT_READY_KEY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by Vincent Karuri on 13/08/2019
 */
public class RDTCountdownTimerFactoryTest {

    private RDTCountdownTimerFactory countdownTimerFactory;

    @Before
    public void setUp() {
        countdownTimerFactory = new RDTCountdownTimerFactory();
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
        Whitebox.invokeMethod(countdownTimerFactory, "performOnExpiredAction", context, tvInstructions);

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

    private Context setUpDummyVibrator() {
        Context context = mock(Context.class);
        Vibrator vibrator = mock(Vibrator.class);
        doNothing().when(vibrator).vibrate(any());
        doReturn(vibrator).when(context).getSystemService(Context.VIBRATOR_SERVICE);

        return context;
    }
}
