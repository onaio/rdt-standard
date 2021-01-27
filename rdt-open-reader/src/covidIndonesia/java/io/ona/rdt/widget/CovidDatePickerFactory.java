package io.ona.rdt.widget;

import android.content.Context;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.widgets.DatePickerFactory;

import org.json.JSONException;
import org.json.JSONObject;

import io.ona.rdt.R;
import io.ona.rdt.widget.validator.MinAllowedDateValidator;
import timber.log.Timber;

/**
 * Created by Vincent Karuri on 14/09/2020
 */
public class CovidDatePickerFactory extends DatePickerFactory {

    public static String V_MIN_ALLOWED_DATE = "v_min_allowed_date";

    @Override
    protected void attachLayout(String stepName, final Context context, JsonFormFragment formFragment,
                                JSONObject jsonObject, final MaterialEditText editText,
                                final TextView duration) {

        super.attachLayout(stepName, context, formFragment, jsonObject, editText, duration);
        try {
            if (jsonObject.has(V_MIN_ALLOWED_DATE)) {
                JSONObject maxAllowDateObj = jsonObject.getJSONObject(V_MIN_ALLOWED_DATE);
                editText.setId(R.id.edit_text);
                editText.addValidator(new MinAllowedDateValidator(maxAllowDateObj.getString(JsonFormConstants.ERR),
                        maxAllowDateObj.getString(JsonFormConstants.VALUE)));
            }
        } catch (JSONException e) {
            Timber.e(e);
        }
    }
}
