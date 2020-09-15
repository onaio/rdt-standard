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
public class MaxAllowedDateValidatorTest {

    private MaxAllowedDateValidator maxAllowedDateValidator;

    @Before
    public void setUp() {
        maxAllowedDateValidator = new MaxAllowedDateValidator("error", "today-1d");
    }

    @Test
    public void testValidationShouldReturnCorrectStatus() {
        // yesterday was last valid day
        Assert.assertFalse(maxAllowedDateValidator.isValid(getOffSetDate(0), false)); // today
        Assert.assertTrue(maxAllowedDateValidator.isValid(getOffSetDate(1), false)); // yesterday
        Assert.assertTrue(maxAllowedDateValidator.isValid(getOffSetDate(2), false)); // the day before yesterday
    }

    private String getOffSetDate(int dayOffset) {
        Date date = new Date();
        return DatePickerFactory.DATE_FORMAT.format(new DateTime(date).minusDays(dayOffset).toDate());
    }
}