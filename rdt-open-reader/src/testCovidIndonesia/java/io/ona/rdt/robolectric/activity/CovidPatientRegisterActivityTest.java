package io.ona.rdt.robolectric.activity;

import android.app.Activity;
import android.widget.ListView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowAlertDialog;
import org.smartregister.util.LangUtils;

import androidx.appcompat.app.AlertDialog;
import io.ona.rdt.R;
import io.ona.rdt.activity.CovidPatientRegisterActivity;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.fragment.CovidPatientRegisterFragment;
import io.ona.rdt.presenter.CovidPatientRegisterActivityPresenter;
import io.ona.rdt.presenter.PatientRegisterActivityPresenter;
import io.ona.rdt.util.CovidRDTJsonFormUtils;
import io.ona.rdt.util.RDTJsonFormUtils;

public class CovidPatientRegisterActivityTest extends ActivityRobolectricTest {

    private CovidPatientRegisterActivity covidPatientRegisterActivity;
    private final String languageSwitcherDialogMethod = "languageSwitcherDialog";
    private final String[] locales = RDTApplication.getInstance().getResources().getStringArray(R.array.locales_value);

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
        Whitebox.invokeMethod(covidPatientRegisterActivity, languageSwitcherDialogMethod);
        Assert.assertNotNull(ShadowAlertDialog.getLatestDialog());
        Assert.assertTrue(ShadowAlertDialog.getLatestDialog() instanceof AlertDialog);
    }

    @Test
    public void testLanguageSwitcherDialogShouldSetCorrectLocale() throws Exception {
        Whitebox.invokeMethod(covidPatientRegisterActivity, languageSwitcherDialogMethod);
        AlertDialog languageSwitcher = (AlertDialog) ShadowAlertDialog.getLatestDialog();
        ListView listView = languageSwitcher.getListView();
        verifyLocaleIsSaved(listView, 1, locales[1]); // for Bahasa
        verifyLocaleIsSaved(listView, 0, locales[0]); // for English
    }

    private void verifyLocaleIsSaved(ListView listView, int position, String locale) {
        listView.performItemClick(null, position, listView.getItemIdAtPosition(position));
        Assert.assertEquals(locale, LangUtils.getLanguage(RDTApplication.getInstance()));
        Assert.assertEquals(locale, RDTApplication.getInstance().getResources().getConfiguration().locale.getLanguage());
    }

    @Test
    public void testRegisterLanguageSwitcherShouldVerifyLocaleIndex() throws Exception {
        verifySavedLanguageIndex(1);
        LangUtils.saveLanguage(covidPatientRegisterActivity, locales[0]);
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
