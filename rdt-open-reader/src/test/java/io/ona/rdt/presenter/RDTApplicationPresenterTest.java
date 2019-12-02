package io.ona.rdt.presenter;

import android.os.Build;

import org.junit.Before;
import org.junit.Test;
import org.smartregister.commonregistry.CommonFtsObject;

import java.util.Map;

import io.ona.rdt.BuildConfig;
import io.ona.rdt.util.Constants;

import static io.ona.rdt.util.Constants.APP_VERSION;
import static io.ona.rdt.util.Constants.PHONE_MANUFACTURER;
import static io.ona.rdt.util.Constants.PHONE_MODEL;
import static io.ona.rdt.util.Constants.PHONE_OS_VERSION;
import static io.ona.rdt.util.Constants.RDT_PATIENTS;
import static org.junit.Assert.assertEquals;

/**
 * Created by Vincent Karuri on 12/11/2019
 */
public class RDTApplicationPresenterTest {

    private RDTApplicationPresenter presenter;

    @Before
    public void setUp() {
        presenter = new RDTApplicationPresenter();
    }

    @Test
    public void testCreateCommonFtsObjectShouldCreateCorrectFtsObject() {
        CommonFtsObject ftsObject = presenter.createCommonFtsObject();
        assertEquals(ftsObject.getSortFields(RDT_PATIENTS), new String[]{Constants.DBConstants.NAME});
        assertEquals(ftsObject.getSearchFields(RDT_PATIENTS), new String[]{Constants.DBConstants.NAME});
        assertEquals(ftsObject.getTables(), new String[]{RDT_PATIENTS});
    }

    @Test
    public void testGetPhonePropertiesShouldGetCorrectPhoneProperties() {
        Map<String, String> phoneProperties = presenter.getPhoneProperties();
        assertEquals(phoneProperties.get(PHONE_MANUFACTURER), Build.MANUFACTURER);
        assertEquals(phoneProperties.get(PHONE_MODEL), Build.MODEL);
        assertEquals(phoneProperties.get(PHONE_OS_VERSION), Build.VERSION.RELEASE);
        assertEquals(phoneProperties.get(APP_VERSION), BuildConfig.VERSION_NAME);
    }
}
