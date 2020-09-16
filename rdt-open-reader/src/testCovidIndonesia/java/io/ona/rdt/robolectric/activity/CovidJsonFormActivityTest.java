package io.ona.rdt.robolectric.activity;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.robolectric.Robolectric;

import io.ona.rdt.activity.CovidJsonFormActivity;

/**
 * Created by Vincent Karuri on 16/09/2020
 */
public class CovidJsonFormActivityTest extends JsonFormActivityTest {

    private CovidJsonFormActivity jsonFormActivity;

    @Before
    public void setUp() throws JSONException {
        super.setUp();
        jsonFormActivity = Robolectric.buildActivity(CovidJsonFormActivity.class, intent).create().resume().get();
        jsonFormActivity = Mockito.spy(jsonFormActivity);
    }

    @Test
    public void testDispatchTouchEventShouldClearFocusOnMaterialEditTextWhenUserScrollsDown() {
        MotionEvent motionEvent = Mockito.mock(MotionEvent.class);
        Mockito.doReturn(MotionEvent.ACTION_DOWN).when(motionEvent).getAction();

        MaterialEditText editText = new MaterialEditText(jsonFormActivity);
        editText.requestFocus();
        ((ViewGroup) jsonFormActivity.findViewById(android.R.id.content).getRootView()).addView(editText);

        Assert.assertTrue(editText.hasFocus());

        Mockito.doReturn(editText).when(jsonFormActivity).getCurrentFocus();
        jsonFormActivity.dispatchTouchEvent(motionEvent);

        Assert.assertFalse(editText.hasFocus());
    }

    @Override
    public Activity getActivity() {
        return jsonFormActivity;
    }
}
