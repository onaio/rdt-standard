package io.ona.rdt.presenter;

import android.os.Build;

import org.junit.Before;
import org.junit.Test;
import org.smartregister.commonregistry.CommonFtsObject;

import java.util.Map;

import io.ona.rdt.BuildConfig;
import io.ona.rdt.util.Constants;

import static io.ona.rdt.TestUtils.setStaticFinalField;
import static io.ona.rdt.util.Constants.PhoneProperties.APP_VERSION;
import static io.ona.rdt.util.Constants.PhoneProperties.PHONE_MANUFACTURER;
import static io.ona.rdt.util.Constants.PhoneProperties.PHONE_MODEL;
import static io.ona.rdt.util.Constants.PhoneProperties.PHONE_OS_VERSION;
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
        String REGISTER_TABLE = "register_table";
        presenter.setRegisterTableName(REGISTER_TABLE);
        CommonFtsObject ftsObject = presenter.createCommonFtsObject();
        assertEquals(new String[]{Constants.DBConstants.FIRST_NAME, Constants.DBConstants.LAST_NAME, Constants.DBConstants.PATIENT_ID, Constants.DBConstants.LAST_INTERACTED_WITH}, ftsObject.getSortFields(REGISTER_TABLE));
        assertEquals( new String[]{Constants.DBConstants.FIRST_NAME, Constants.DBConstants.LAST_NAME, Constants.DBConstants.PATIENT_ID}, ftsObject.getSearchFields(REGISTER_TABLE));
        assertEquals(new String[]{REGISTER_TABLE}, ftsObject.getTables());
    }

    @Test
    public void testGetPhonePropertiesShouldGetCorrectPhoneProperties() throws Exception {
        setStaticFinalField(Build.class.getField("MANUFACTURER"),  "manufacturer");
        setStaticFinalField(Build.class.getField("MODEL"),  "phone_model");
        setStaticFinalField(Build.VERSION.class.getField("RELEASE"),  "phone_os_version");
        setStaticFinalField(BuildConfig.class.getField("VERSION_NAME"),  "version_name");
        Map<String, String> phoneProperties = presenter.getPhoneProperties();
        assertEquals("manufacturer", phoneProperties.get(PHONE_MANUFACTURER));
        assertEquals("phone_model", phoneProperties.get(PHONE_MODEL));
        assertEquals("phone_os_version", phoneProperties.get(PHONE_OS_VERSION));
        assertEquals(BuildConfig.VERSION_NAME, phoneProperties.get(APP_VERSION));
    }
}
