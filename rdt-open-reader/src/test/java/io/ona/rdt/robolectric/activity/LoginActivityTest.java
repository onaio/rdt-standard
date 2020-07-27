package io.ona.rdt.robolectric.activity;

import android.content.Intent;
import android.text.Html;
import android.widget.TextView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.Context;
import org.smartregister.commonregistry.CommonFtsObject;

import java.util.Locale;

import io.ona.rdt.BuildConfig;
import io.ona.rdt.R;
import io.ona.rdt.activity.LoginActivity;
import io.ona.rdt.activity.PatientRegisterActivity;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.presenter.LoginPresenter;
import io.ona.rdt.presenter.RDTApplicationPresenter;
import io.ona.rdt.robolectric.RobolectricTest;
import io.ona.rdt.robolectric.shadow.ClientCoreUtilsShadow;
import io.ona.rdt.robolectric.shadow.MockCounter;
import io.ona.rdt.util.Constants;

import static io.ona.rdt.util.Constants.Table.RDT_PATIENTS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by Vincent Karuri on 16/07/2020
 */

public class LoginActivityTest extends RobolectricTest {

    @Mock
    private  Context context;
    @Mock
    private RDTApplicationPresenter presenter;
    @Captor
    private ArgumentCaptor<CommonFtsObject> commonFtsObjectCaptor;

    private LoginActivity loginActivity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMethods();

        ReflectionHelpers.setField(RDTApplication.getInstance(), "presenter", presenter);
        ReflectionHelpers.setField(RDTApplication.getInstance(), "context", context);

        loginActivity = Robolectric.buildActivity(LoginActivity.class)
                .create()
                .resume()
                .get();
    }

    @Test
    public void testGetRegisterTableNameShouldGetCorrectRegisterTableName() {
        String registerTableName = ReflectionHelpers.callInstanceMethod(loginActivity, "getRegisterTableName");
        assertEquals(RDT_PATIENTS, registerTableName);
    }

    @Test
    public void testGetContentViewShouldGetCorrectLayout() {
        Integer contentViewId = ReflectionHelpers.callInstanceMethod(loginActivity, "getContentView");
        Assert.assertEquals(R.layout.activity_login, contentViewId.intValue());
    }

    @Test
    public void testGetLoginActivityClassShouldGetCorrectLoginActivityClass() {
        Class loginActivityClass = ReflectionHelpers.callInstanceMethod(loginActivity, "getLoginActivityClass");
        assertEquals(PatientRegisterActivity.class, loginActivityClass);
    }

    @Test
    public void testInitializePresenterShouldCorrectlyInitializePresenter() {
        ReflectionHelpers.callInstanceMethod(loginActivity, "initializePresenter");
        LoginPresenter loginPresenter = ReflectionHelpers.getField(loginActivity, "mLoginPresenter");
        assertNotNull(loginPresenter);
    }

    @Test
    public void testAddAttributionTextShouldSetCorrectAttributionText() {
        TextView tvAttributions = loginActivity.findViewById(R.id.tv_login_attributions);
        assertEquals(Html.fromHtml(loginActivity.getResources().getString(R.string.login_attributions)).toString(),
                tvAttributions.getText().toString());
    }

    @Test
    public void testUpdateFTSDetailShouldUpdateCorrectDetails() {
        verify(presenter).setRegisterTableName(eq(RDT_PATIENTS));
        verify(context).updateCommonFtsObject(commonFtsObjectCaptor.capture());

        CommonFtsObject actualCommonFtsObject = commonFtsObjectCaptor.getValue();
        assertEquals(presenter.createCommonFtsObject().getSortFields(RDT_PATIENTS), actualCommonFtsObject.getSortFields(RDT_PATIENTS));
        assertEquals(presenter.createCommonFtsObject().getSearchFields(RDT_PATIENTS), actualCommonFtsObject.getSearchFields(RDT_PATIENTS));
        assertEquals(presenter.createCommonFtsObject().getTables(), actualCommonFtsObject.getTables());
    }

    @Test
    public void testLocaleShouldMatchTheBuildConfigLocale() {
        assertEquals(new Locale(BuildConfig.LOCALE).getLanguage(), loginActivity.getResources().getConfiguration().locale.getLanguage());
    }

    @Test
    public void testGoToHomeShouldLaunchHomePageForLocalLogins() {
        LoginPresenter loginPresenter = mock(LoginPresenter.class);
        ReflectionHelpers.setField(loginActivity, "mLoginPresenter", loginPresenter);
        loginActivity.onResume();
        verify(loginPresenter).isUserLoggedOut();
        Intent expectedIntent = new Intent(loginActivity, PatientRegisterActivity.class);
        Intent actualIntent = shadowOf(RDTApplication.getInstance()).getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
    }

    @Test
    public void testGoToHomeShouldNotLaunchHomePageIfLoggedOut() {
        LoginPresenter loginPresenter = mock(LoginPresenter.class);
        doReturn(true).when(loginPresenter).isUserLoggedOut();
        ReflectionHelpers.setField(loginActivity, "mLoginPresenter", loginPresenter);
        loginActivity.onResume();
        verify(loginPresenter).isUserLoggedOut();
        Intent actualIntent = shadowOf(RDTApplication.getInstance()).getNextStartedActivity();
        assertNull(actualIntent);
    }

    @Test
    public void testGoToHomeShouldLaunchHomePageAndSyncUserDataForRemoteLogins() {
        MockCounter mockCounter = new MockCounter();
        assertEquals(0, mockCounter.getCount());

        ClientCoreUtilsShadow.setMockCounter(mockCounter);

        loginActivity.goToHome(true);
        Intent expectedIntent = new Intent(loginActivity, PatientRegisterActivity.class);
        Intent actualIntent = shadowOf(RDTApplication.getInstance()).getNextStartedActivity();

        assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
        assertEquals(1, mockCounter.getCount());
    }

    private void mockMethods() {
        String[] ftsTables = new String[]{RDT_PATIENTS};
        String[] sortAndSearchFields = new String[]{Constants.DBConstants.FIRST_NAME, Constants.DBConstants.LAST_NAME, Constants.DBConstants.PATIENT_ID};
        CommonFtsObject expectedCommonFtsObject = new CommonFtsObject(ftsTables);
        expectedCommonFtsObject.updateSearchFields(RDT_PATIENTS, sortAndSearchFields);
        expectedCommonFtsObject.updateSortFields(RDT_PATIENTS, sortAndSearchFields);
        doReturn(expectedCommonFtsObject).when(presenter).createCommonFtsObject();
    }
}
