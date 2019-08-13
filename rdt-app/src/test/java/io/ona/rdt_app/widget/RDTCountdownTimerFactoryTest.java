package io.ona.rdt_app.widget;

import android.content.Context;
import android.widget.TextView;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.fragments.JsonFormFragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import static io.ona.rdt_app.widget.RDTCountdownTimerFactory.COUNTDOWN_TIMER_RESULT_READY_KEY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JsonFormConstants.KEY, COUNTDOWN_TIMER_RESULT_READY_KEY);
        Whitebox.setInternalState(countdownTimerFactory, "stepObject", jsonObject);

        TextView tvInstructions = mock(TextView.class);
        Context context = mock(Context.class);
        doReturn("instructions").when(context).getString(anyInt());
        Whitebox.invokeMethod(countdownTimerFactory, "performOnExpiredAction", context, tvInstructions);

        verify(tvInstructions).setText(eq("instructions"));
    }

    @Test
    public void testOnCountdownFinishMovesToNextStep() throws Exception {
        JsonFormFragment formFragment = mock(JsonFormFragment.class);
        Whitebox.setInternalState(countdownTimerFactory, "formFragment", formFragment);

        JSONObject jsonObject = new JSONObject();
        Whitebox.setInternalState(countdownTimerFactory, "stepObject", jsonObject);

        Whitebox.invokeMethod(countdownTimerFactory, "performOnExpiredAction", mock(Context.class));
        verify(formFragment).next();
    }
}
