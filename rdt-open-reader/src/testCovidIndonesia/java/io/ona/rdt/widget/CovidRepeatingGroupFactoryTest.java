package io.ona.rdt.widget;

import android.view.View;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.vijay.jsonwizard.fragments.JsonFormFragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import io.ona.rdt.R;
import io.ona.rdt.robolectric.widget.WidgetFactoryRobolectricTest;

public class CovidRepeatingGroupFactoryTest extends WidgetFactoryRobolectricTest {

    @Test
    public void verifyEditTextListeners() throws Exception {
        CovidRepeatingGroupFactory covidRepeatingGroupFactory = new CovidRepeatingGroupFactory();
        JSONObject jsonObject = new JSONObject("{\"value\": [], \"key\": \"test\", \"reference_edit_text_hint\": \"\"}");
        List<View> views = covidRepeatingGroupFactory.getViewsFromJson("step1", jsonFormActivity, new JsonFormFragment(), jsonObject, null);
        MaterialEditText materialEditText = views.get(0).findViewById(R.id.reference_edit_text);
        materialEditText.getOnFocusChangeListener().onFocusChange(materialEditText, false);
        Assert.assertEquals(1, views.size());
    }

    @Override
    protected JSONObject getJsonObject() throws JSONException {
        JSONObject jsonObject = new JSONObject("{\"fields\": []}");
        return jsonObject;
    }
}
