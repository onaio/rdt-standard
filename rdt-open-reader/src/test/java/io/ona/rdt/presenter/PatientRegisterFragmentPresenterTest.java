package io.ona.rdt.presenter;

import android.app.Activity;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.support.membermodification.MemberMatcher;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.smartregister.cursoradapter.SmartRegisterQueryBuilder;

import io.ona.rdt.contract.PatientRegisterFragmentContract;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.fragment.PatientRegisterFragment;
import io.ona.rdt.interactor.PatientRegisterFragmentInteractor;

import static io.ona.rdt.util.Constants.RDT_PATIENTS;
import static io.ona.rdt.util.Constants.RDT_TESTS;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;

/**
 * Created by Vincent Karuri on 12/11/2019
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest(PatientRegisterFragmentPresenter.class)
public class PatientRegisterFragmentPresenterTest {

    private PatientRegisterFragmentPresenter presenter;

    @Mock
    private PatientRegisterFragmentContract.View patientRegisterFragment;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        suppress(MemberMatcher.constructor(PatientRegisterFragmentInteractor.class));
        presenter = new PatientRegisterFragmentPresenter(patientRegisterFragment);
    }

    @Test
    public void testInitializeQueriesShouldInitializeQueries() {
        SmartRegisterQueryBuilder smartRegisterQueryBuilder = mock(SmartRegisterQueryBuilder.class);
        doReturn("main_condition").when(smartRegisterQueryBuilder).mainCondition(anyString());
        Whitebox.setInternalState(presenter, "smartRegisterQueryBuilder", smartRegisterQueryBuilder);
        presenter.initializeQueries("main_condition");
        verify(patientRegisterFragment).initializeAdapter();
        verify(patientRegisterFragment).countExecute();
        verify(patientRegisterFragment).filterandSortInInitializeQueries();
        verify(patientRegisterFragment).initializeQueryParams(eq(RDT_PATIENTS), eq("main_condition"), eq("main_condition"));
    }

    @Test
    public void testLaunchFormShouldLaunchForm() throws JSONException {
        PatientRegisterFragmentInteractor interactor = mock(PatientRegisterFragmentInteractor.class);
        Whitebox.setInternalState(presenter, "interactor", interactor);

        Activity activity = mock(Activity.class);
        String formName = "form_name";
        Patient patient = mock(Patient.class);
        presenter.launchForm(activity, formName, patient);

        verify(interactor).launchForm(eq(activity), eq(formName), eq(patient));
    }

    @Test
    public void testGetMainCondition() {
        assertEquals(" name != ''", presenter.getMainCondition());
    }

    @Test
    public void testMainColumns() throws Exception {
        assertEquals(new String[]{RDT_TESTS + "." + "relationalid", RDT_TESTS + ".name", RDT_TESTS
                + "." + "age", RDT_TESTS + "." + "sex"},
                Whitebox.invokeMethod(presenter, "mainColumns", RDT_TESTS));
    }
}
