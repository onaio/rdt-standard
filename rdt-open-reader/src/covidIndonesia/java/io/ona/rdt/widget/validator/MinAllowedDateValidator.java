package io.ona.rdt.widget.validator;

import com.rengwuxian.materialedittext.validation.METValidator;
import com.vijay.jsonwizard.utils.FormUtils;
import com.vijay.jsonwizard.widgets.DatePickerFactory;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.util.Date;

import androidx.annotation.NonNull;
import io.ona.rdt.util.Utils;
import timber.log.Timber;

/**
 * Created by Vincent Karuri on 14/09/2020
 */
public class MinAllowedDateValidator extends METValidator {

    private String minAllowedDateStr;

    public MinAllowedDateValidator(@NonNull String errorMessage, String minAllowedDateStr) {
        super(errorMessage);
        this.minAllowedDateStr = minAllowedDateStr;
    }

    @Override
    public boolean isValid(@NonNull CharSequence charSequence, boolean b) {

        boolean isValid = true;
        if (StringUtils.isBlank(charSequence)) {
            return isValid;
        }

        try {
            Date minAllowedDate = FormUtils.getDate(minAllowedDateStr).getTime();
            Date selectedDate = Utils.convertDate(charSequence.toString(), DatePickerFactory.DATE_FORMAT.toPattern());
            isValid = selectedDate.before(minAllowedDate);
        } catch (ParseException e) {
            Timber.e(e);
        }

        return  isValid;
    }
}
