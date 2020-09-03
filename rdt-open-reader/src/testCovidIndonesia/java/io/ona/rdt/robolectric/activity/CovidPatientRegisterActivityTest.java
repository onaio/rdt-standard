package io.ona.rdt.robolectric.activity;

import android.app.Activity;

import androidx.appcompat.app.AlertDialog;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowAlertDialog;
import org.smartregister.util.LangUtils;

import io.ona.rdt.activity.CovidPatientRegisterActivity;
import io.ona.rdt.fragment.CovidPatientRegisterFragment;
import io.ona.rdt.presenter.CovidPatientRegisterActivityPresenter;
import io.ona.rdt.presenter.PatientRegisterActivityPresenter;
import io.ona.rdt.util.CovidRDTJsonFormUtils;
import io.ona.rdt.util.RDTJsonFormUtils;

public class CovidPatientRegisterActivityTest extends ActivityRobolectricTest {

    private CovidPatientRegisterActivity covidPatientRegisterActivity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        covidPatientRegisterActivity = Robolectric.buildActivity(CovidPatientRegisterActivity.class)
                .create()
                .resume()
                .get();
    }

    @Test
    public void testGetRegisterFragmentShouldReturnCovidPatientRegisterFragment() {
        Assert.assertEquals(CovidPatientRegisterFragment.class.getSimpleName(), covidPatientRegisterActivity.getRegisterFragment().getClass().getSimpleName());
    }

    @Test
    public void testInitializeFormUtilsShouldReturnCovidRDTJsonFormUtils() throws Exception {
        RDTJsonFormUtils utils = Whitebox.invokeMethod(covidPatientRegisterActivity, "initializeFormUtils");
        Assert.assertEquals(CovidRDTJsonFormUtils.class.getSimpleName(), utils.getClass().getSimpleName());
    }

    @Test
    public void testCreatePatientRegisterActivityPresenterShouldReturnPatientRegisterActivityPresenter() throws Exception {
        PatientRegisterActivityPresenter presenter = Whitebox.invokeMethod(covidPatientRegisterActivity, "createPatientRegisterActivityPresenter");
        Assert.assertEquals(CovidPatientRegisterActivityPresenter.class.getSimpleName(), presenter.getClass().getSimpleName());
    }

    @Test
    public void testLanguageSwitcherDialogShowLocaleOptions() throws Exception {
        Whitebox.invokeMethod(covidPatientRegisterActivity, "languageSwitcherDialog");
        Assert.assertNotNull(ShadowAlertDialog.getLatestDialog());
        Assert.assertTrue(ShadowAlertDialog.getLatestDialog() instanceof AlertDialog);
    }

    @Test
    public void testRegisterLanguageSwitcherShouldVerifyLocaleIndex() throws Exception {

        verifySavedLanguageIndex(1);
        LangUtils.saveLanguage(covidPatientRegisterActivity, "en");
        verifySavedLanguageIndex(0);

    }

    private void verifySavedLanguageIndex(int expected) throws Exception {
        Whitebox.invokeMethod(covidPatientRegisterActivity, "registerLanguageSwitcher");
        int selectedLanguageIndex = Whitebox.getInternalState(covidPatientRegisterActivity, "selectedLanguageIndex");
        Assert.assertEquals(expected, selectedLanguageIndex);
    }

    @Override
    public Activity getActivity() {
        return covidPatientRegisterActivity;
    }
}
