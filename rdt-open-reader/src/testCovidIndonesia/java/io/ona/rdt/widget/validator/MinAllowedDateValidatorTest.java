package io.ona.rdt.widget.validator;

import com.vijay.jsonwizard.widgets.DatePickerFactory;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

/**
 * Created by Vincent Karuri on 15/09/2020
 */
public class MinAllowedDateValidatorTest {

    private MinAllowedDateValidator minAllowedDateValidator;

    @Before
    public void setUp() {
        minAllowedDateValidator = new MinAllowedDateValidator("error", "today+1d");
    }

    @Test
    public void testValidationShouldReturnCorrectStatus() {
        Assert.assertFalse(minAllowedDateValidator.isValid(getOffSetDate(-1), false)); // expired yesterday
        Assert.assertFalse(minAllowedDateValidator.isValid(getOffSetDate(0), false)); // expires today
        Assert.assertTrue(minAllowedDateValidator.isValid(getOffSetDate(1), false)); // expires tomorrow
        Assert.assertTrue(minAllowedDateValidator.isValid(getOffSetDate(2), false)); // expires day after tomorrow
    }

    private String getOffSetDate(int dayOffset) {
        Date date = new Date();
        return DatePickerFactory.DATE_FORMAT.format(new DateTime(date).plusDays(dayOffset).toDate());
    }
}

