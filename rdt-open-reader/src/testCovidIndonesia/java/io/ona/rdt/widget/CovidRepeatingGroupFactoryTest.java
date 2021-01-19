package io.ona.rdt.widget;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.vijay.jsonwizard.domain.WidgetArgs;
import com.vijay.jsonwizard.fragments.JsonFormFragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.List;

import io.ona.rdt.R;
import io.ona.rdt.robolectric.widget.WidgetFactoryRobolectricTest;

public class CovidRepeatingGroupFactoryTest extends WidgetFactoryRobolectricTest {

    @Test
    public void verifyEditTextListenersShouldPerformFocusChangeListener() throws Exception {
        MockCovidRepeatingGroupFactory covidRepeatingGroupFactory = Mockito.spy(new MockCovidRepeatingGroupFactory());
        JSONObject jsonObject = new JSONObject("{\"value\": [], \"key\": \"test\", \"reference_edit_text_hint\": \"\"}");
        List<View> views = covidRepeatingGroupFactory.getViewsFromJson("step1", jsonFormActivity, new JsonFormFragment(), jsonObject, null);
        MaterialEditText materialEditText = views.get(0).findViewById(R.id.reference_edit_text);
        materialEditText.getOnFocusChangeListener().onFocusChange(materialEditText, false);
        Assert.assertEquals(1, views.size());
        Mockito.verify(covidRepeatingGroupFactory).addOnDoneAction(ArgumentMatchers.any(TextView.class), ArgumentMatchers.any(ImageButton.class), ArgumentMatchers.any(WidgetArgs.class));
    }

    @Override
    protected JSONObject getStepObject() throws JSONException {
        return new JSONObject("{\"fields\": []}");
    }

    private static class MockCovidRepeatingGroupFactory extends CovidRepeatingGroupFactory {
        @Override
        public void addOnDoneAction(TextView textView, ImageButton doneButton, WidgetArgs widgetArgs) {
            super.addOnDoneAction(textView, doneButton, widgetArgs);
        }
    }
}
