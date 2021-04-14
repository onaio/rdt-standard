package io.ona.rdt.interactor;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.smartregister.CoreLibrary;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.util.NetworkUtils;
import org.smartregister.view.contract.BaseLoginContract;

import io.ona.rdt.presenter.LoginPresenter;
import io.ona.rdt.util.Utils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Utils.class, CoreLibrary.class, NetworkUtils.class})
public class LoginInteractorTest {

    private LoginInteractor loginInteractor;
    private final String dummyUser = "dummyUser";

    @Before
    public void setUp() {
        loginInteractor = new LoginInteractor(mock(LoginPresenter.class));
    }

    @Test
    public void testScheduleJobsPeriodicallyShouldScheduleJobsPeriodically() {
        mockStaticClasses();
        loginInteractor.scheduleJobsPeriodically();
        verifyStatic(Utils.class, times(1));
        Utils.scheduleJobsPeriodically();
    }

    @Test
    public void testScheduleJobsImmediatelyShouldScheduleJobsImmediately() {
        mockStaticClasses();
        loginInteractor.scheduleJobsImmediately();
        verifyStatic(Utils.class, times(1));
        Utils.scheduleJobsImmediately();
    }

    @Test
    public void testLoginShouldBeRemote() {

        PowerMockito.mockStatic(CoreLibrary.class);
        PowerMockito.mockStatic(NetworkUtils.class);

        loginInteractor = Mockito.spy(loginInteractor);

        AllSharedPreferences allSharedPreferences = Mockito.mock(AllSharedPreferences.class);
        PowerMockito.doReturn(allSharedPreferences).when(loginInteractor).getSharedPreferences();

        BaseLoginContract.View loginView = Mockito.mock(BaseLoginContract.View.class);
        PowerMockito.doReturn(loginView).when(loginInteractor).getLoginView();

        CoreLibrary coreLibrary = Mockito.mock(CoreLibrary.class);
        PowerMockito.when(CoreLibrary.getInstance()).thenReturn(coreLibrary);

        Context context = Mockito.mock(Context.class);
        PowerMockito.doReturn(context).when(loginInteractor).getApplicationContext();

        PowerMockito.when(NetworkUtils.isNetworkAvailable()).thenReturn(true);

        loginInteractor.login(null, dummyUser, new char[]{'a'});
        Mockito.verify(allSharedPreferences, Mockito.times(1)).saveForceRemoteLogin(true, dummyUser);
    }

    private void mockStaticClasses() {
        mockStatic(Utils.class);
    }
}
