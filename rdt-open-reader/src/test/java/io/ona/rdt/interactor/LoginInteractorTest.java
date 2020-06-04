package io.ona.rdt.interactor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import io.ona.rdt.presenter.LoginPresenter;
import io.ona.rdt.util.Utils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Utils.class})
public class LoginInteractorTest {

    private LoginInteractor loginInteractor;

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

    private void mockStaticClasses() {
        mockStatic(Utils.class);
    }
}
