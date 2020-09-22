package io.ona.rdt.widget;

import android.view.View;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.utils.ValidationStatus;
import com.vijay.jsonwizard.widgets.DatePickerFactory;
import com.vijay.jsonwizard.widgets.EditTextFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.smartregister.util.JsonFormUtils;

import java.util.List;

import io.ona.rdt.R;
import io.ona.rdt.TestUtils;
import io.ona.rdt.robolectric.widget.WidgetFactoryRobolectricTest;

/**
 * Created by Vincent Karuri on 14/09/2020
 */
public class CovidDatePickerFactoryTest extends WidgetFactoryRobolectricTest {

    private CovidDatePickerFactory covidDatePickerFactory;

    @Before
    public void setUp() throws JSONException {
        super.setUp();
        covidDatePickerFactory = new CovidDatePickerFactory();
    }

    private MaterialEditText getDatePickerEditText(String minAllowedDate) throws Exception {
        List<View> views = covidDatePickerFactory.getViewsFromJson(JsonFormConstants.STEP1, jsonFormActivity,
                Mockito.mock(JsonFormFragment.class), getWidget(minAllowedDate), Mockito.mock(CommonListener.class));
        View rootLayout = views.get(0);

        return rootLayout.findViewById(R.id.edit_text);
    }

    private JSONObject getWidget(String minAllowedDate) throws JSONException {
        JSONObject datePickerWidget = new JSONObject();
        JSONObject validator = new JSONObject();
        validator.put(JsonFormUtils.VALUE, minAllowedDate);
        validator.put(JsonFormConstants.ERR, "Expired VTM! Please use another.");
        datePickerWidget.put(CovidDatePickerFactory.V_MIN_ALLOWED_DATE, validator);
        return datePickerWidget;
    }
}

