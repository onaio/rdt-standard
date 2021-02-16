package io.ona.rdt.widget;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.utils.FormUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.robolectric.util.ReflectionHelpers;

import java.util.List;

import io.ona.rdt.R;
import io.ona.rdt.robolectric.widget.WidgetFactoryRobolectricTest;

public class CovidEditTextFactoryTest extends WidgetFactoryRobolectricTest {

    private static final String RIGHT_MARGIN = "right_margin";
    private static final String EXPECTED_MARGIN = "5dp";
    private static final String EXPECTED_PADDING = "10dp";

    CovidEditTextFactory covidEditTextFactory;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        covidEditTextFactory = new CovidEditTextFactory();
    }

    @Test
    public void testEditTextShouldReturnUpdatedProperties() throws Exception {
        RelativeLayout rootLayout = getRootLayout();
        MaterialEditText editText = getEditText(rootLayout);
        Context context = editText.getContext();

        // verify root layout minimum height
        Assert.assertEquals(0, rootLayout.getMinimumHeight());

        // verify floating label type
        Assert.assertFalse(ReflectionHelpers.getField(editText, "floatingLabelEnabled"));
        Assert.assertFalse(ReflectionHelpers.getField(editText, "highlightFloatingLabel"));

        // verify margins
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) editText.getLayoutParams();
        Assert.assertEquals(FormUtils.getValueFromSpOrDpOrPx(EXPECTED_MARGIN, context), layoutParams.leftMargin);
        Assert.assertEquals(FormUtils.getValueFromSpOrDpOrPx(EXPECTED_MARGIN, context), layoutParams.topMargin);
        Assert.assertEquals(FormUtils.getValueFromSpOrDpOrPx(EXPECTED_MARGIN, context), layoutParams.rightMargin);
        Assert.assertEquals(FormUtils.getValueFromSpOrDpOrPx(EXPECTED_MARGIN, context), layoutParams.bottomMargin);

        // verify floating label padding
        Assert.assertEquals(FormUtils.getValueFromSpOrDpOrPx(EXPECTED_PADDING, context), editText.getFloatingLabelPadding());
    }

    private RelativeLayout getRootLayout() throws Exception {
        List<View> views = covidEditTextFactory.getViewsFromJson(JsonFormConstants.STEP1, jsonFormActivity,
                Mockito.mock(JsonFormFragment.class), getWidget(), Mockito.mock(CommonListener.class));
        return (RelativeLayout) views.get(0);
    }

    private MaterialEditText getEditText(RelativeLayout rootLayout) throws Exception {
        return (MaterialEditText) ((RelativeLayout) rootLayout.findViewById(R.id.edit_text_layout)).getChildAt(0);
    }

    private JSONObject getWidget() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JsonFormConstants.KEY, "test_edit_text");
        jsonObject.put(JsonFormConstants.TYPE, "edit_text");
        jsonObject.put(JsonFormConstants.OPENMRS_ENTITY_PARENT, "");
        jsonObject.put(JsonFormConstants.OPENMRS_ENTITY_ID, "");
        jsonObject.put(JsonFormConstants.OPENMRS_ENTITY, "");
        jsonObject.put(JsonFormConstants.TOP_PADDING, EXPECTED_PADDING);
        jsonObject.put(JsonFormConstants.LEFT_MARGIN, EXPECTED_MARGIN);
        jsonObject.put(JsonFormConstants.TOP_MARGIN, EXPECTED_MARGIN);
        jsonObject.put(RIGHT_MARGIN, EXPECTED_MARGIN);
        jsonObject.put(JsonFormConstants.BOTTOM_MARGIN, EXPECTED_MARGIN);

        return jsonObject;
    }
}
