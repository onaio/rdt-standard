package io.ona.rdt.robolectric.activity;

import android.app.Activity;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlertDialog;
import org.smartregister.util.LangUtils;

import io.ona.rdt.R;
import io.ona.rdt.activity.CovidLoginActivity;
import io.ona.rdt.activity.CovidPatientRegisterActivity;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.fragment.CovidPatientRegisterFragment;
import io.ona.rdt.presenter.CovidPatientRegisterActivityPresenter;
import io.ona.rdt.presenter.PatientRegisterActivityPresenter;
import io.ona.rdt.robolectric.shadow.MockCounter;
import io.ona.rdt.robolectric.shadow.UtilsShadow;
import io.ona.rdt.util.CovidRDTJsonFormUtils;
import io.ona.rdt.util.RDTJsonFormUtils;
import io.ona.rdt.util.Utils;

@Config(shadows = {UtilsShadow.class})
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
    public void testOnResumeShouldSetCurrentActivityToCovidPatientRegisterActivity() throws Exception {
        RDTApplication.getInstance().clearCurrActivityReference(covidPatientRegisterActivity);
        Whitebox.invokeMethod(covidPatientRegisterActivity, "onResume");
        Assert.assertThat(RDTApplication.getInstance().getCurrentActivity(), CoreMatchers.instanceOf(CovidPatientRegisterActivity.class));
    }

    @Test
    public void testOnPauseShouldClearCurrentActivity() throws Exception {
        Whitebox.invokeMethod(covidPatientRegisterActivity, "onPause");
        Assert.assertNull(RDTApplication.getInstance().getCurrentActivity());
    }

    @Test
    public void testUserAuthorizationVerificationTaskShouldVerifyMethodCalled() throws Exception {

        MockCounter counter = new MockCounter();
        UtilsShadow.setMockCounter(counter);
        Assert.assertEquals(0, UtilsShadow.getMockCounter().getCount());
        Whitebox.invokeMethod(covidPatientRegisterActivity, "onResume");
        Assert.assertEquals(2, UtilsShadow.getMockCounter().getCount());
        UtilsShadow.setMockCounter(null);
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
        String[] localesVal = covidPatientRegisterActivity.getResources().getStringArray(R.array.locales_value);
        LangUtils.saveLanguage(covidPatientRegisterActivity, localesVal[0]);
        Utils.updateLocale(covidPatientRegisterActivity);
        verifySavedLanguageIndex(0);
    }

    @Test
    public void testSelectDrawerItemShouldReturnRelevantBoolean() {

        UtilsShadow.setMockCounter(new MockCounter());
        MenuItem menuItem = Mockito.mock(MenuItem.class);

        Mockito.when(menuItem.getItemId()).thenReturn(R.id.menu_item_sync);
        Assert.assertTrue(covidPatientRegisterActivity.selectDrawerItem(menuItem));

        Mockito.when(menuItem.getItemId()).thenReturn(R.id.menu_item_create_shipment);
        Assert.assertTrue(covidPatientRegisterActivity.selectDrawerItem(menuItem));

        Mockito.when(menuItem.getItemId()).thenReturn(R.id.menu_item_switch_language);
        Assert.assertTrue(covidPatientRegisterActivity.selectDrawerItem(menuItem));

        Mockito.when(menuItem.getItemId()).thenReturn(R.id.menu_item_toggle_img_sync);
        Assert.assertFalse(covidPatientRegisterActivity.selectDrawerItem(menuItem));
    }

    @Test
    public void testGetLoginPageShouldReturnCovidLoginActivity() throws Exception {
        Class clazz = Whitebox.invokeMethod(covidPatientRegisterActivity, "getLoginPage");
        Assert.assertEquals(CovidLoginActivity.class.getName(), clazz.getName());
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
