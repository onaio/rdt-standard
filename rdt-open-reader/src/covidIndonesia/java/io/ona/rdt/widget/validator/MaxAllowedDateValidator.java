package io.ona.rdt.widget.validator;

import com.rengwuxian.materialedittext.validation.METValidator;
import com.vijay.jsonwizard.utils.FormUtils;
import com.vijay.jsonwizard.widgets.DatePickerFactory;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTimeComparator;

import java.text.ParseException;
import java.util.Date;

import androidx.annotation.NonNull;
import io.ona.rdt.util.Utils;
import timber.log.Timber;

/**
 * Created by Vincent Karuri on 14/09/2020
 */
public class MaxAllowedDateValidator extends METValidator {

    private String maxAllowedDateStr;

    public MaxAllowedDateValidator(@NonNull String errorMessage, String maxAllowedDateStr) {
        super(errorMessage);
        this.maxAllowedDateStr = maxAllowedDateStr;
    }

    @Override
    public boolean isValid(@NonNull CharSequence charSequence, boolean b) {

        boolean isValid = true;
        if (StringUtils.isBlank(charSequence)) {
            return isValid;
        }

        try {
            Date maxAllowedDate = FormUtils.getDate(maxAllowedDateStr).getTime();
            Date selectedDate = Utils.convertDate(charSequence.toString(), DatePickerFactory.DATE_FORMAT.toPattern());
            isValid = DateTimeComparator.getDateOnlyInstance().compare(selectedDate, maxAllowedDate) < 1;
        } catch (ParseException e) {
            Timber.e(e);
        }

        return  isValid;
    }
}
