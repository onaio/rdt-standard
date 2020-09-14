package io.ona.rdt.widget;

import android.view.View;
import android.widget.EditText;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.utils.ValidationStatus;
import com.vijay.jsonwizard.widgets.EditTextFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.smartregister.util.JsonFormUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.ona.rdt.R;
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

    @Test
    public void testMinDateValidationShouldRejectInvalidDates() throws Exception {
        // expired before today
        MaterialEditText datePickerEditText = getDatePickerEditText("today-1d");
        datePickerEditText.setText(getTodaysDate());
        ValidationStatus validationStatus = EditTextFactory.validate(Mockito.mock(JsonFormFragment.class), datePickerEditText);
        Assert.assertFalse(validationStatus.isValid());

        // expired today
        datePickerEditText = getDatePickerEditText("today");
        datePickerEditText.setText(getTodaysDate());
        validationStatus = EditTextFactory.validate(Mockito.mock(JsonFormFragment.class), datePickerEditText);
        Assert.assertFalse(validationStatus.isValid());

        // not expired
        datePickerEditText = getDatePickerEditText("today+1d");
        datePickerEditText.setText(getTodaysDate());
        validationStatus = EditTextFactory.validate(Mockito.mock(JsonFormFragment.class), datePickerEditText);
        Assert.assertTrue(validationStatus.isValid());
    }

    private MaterialEditText getDatePickerEditText(String minAllowedDate) throws Exception {
        List<View> views = covidDatePickerFactory.getViewsFromJson(JsonFormConstants.STEP1, jsonFormActivity,
                Mockito.mock(JsonFormFragment.class), getWidget(minAllowedDate), Mockito.mock(CommonListener.class));
        View rootLayout = views.get(0);

        return rootLayout.findViewById(R.id.edit_text);
    }

    private String getTodaysDate() {
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
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

