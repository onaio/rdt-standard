package io.ona.rdt.presenter;

import android.app.Activity;
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.smartregister.cursoradapter.SmartRegisterQueryBuilder;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.repository.UniqueIdRepository;
import org.smartregister.sync.ClientProcessorForJava;
import org.smartregister.util.AssetHandler;
import org.smartregister.util.JsonFormUtils;

import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.contract.PatientRegisterFragmentContract;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.interactor.PatientRegisterFragmentInteractor;
import io.ona.rdt.util.StepStateConfig;

import static io.ona.rdt.util.Constants.DBConstants.AGE;
import static io.ona.rdt.util.Constants.DBConstants.DOB;
import static io.ona.rdt.util.Constants.DBConstants.FIRST_NAME;
import static io.ona.rdt.util.Constants.DBConstants.LAST_NAME;
import static io.ona.rdt.util.Constants.DBConstants.PATIENT_ID;
import static io.ona.rdt.util.Constants.DBConstants.SEX;
import static io.ona.rdt.util.Constants.Step.RDT_ID_KEY;
import static io.ona.rdt.util.Constants.Table.RDT_PATIENTS;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created by Vincent Karuri on 12/11/2019
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({RDTApplication.class, ClientProcessorForJava.class, JsonFormUtils.class, AssetHandler.class, PatientRegisterFragmentInteractor.class})
public class PatientRegisterFragmentPresenterTest {
    @Mock
    private RDTApplication rdtApplication;
    @Mock
    private org.smartregister.Context applicationContext;
    @Mock
    private Context context;
    @Mock
    private EventClientRepository eventClientRepository;
    @Mock
    private ClientProcessorForJava clientProcessor;
    @Mock
    private UniqueIdRepository uniqueIdRepository;
    @Mock
    private StepStateConfig stepStateConfig;

    private PatientRegisterFragmentPresenter presenter;

    @Mock
    private PatientRegisterFragmentContract.View patientRegisterFragment;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockStaticMethods();
        presenter = new PatientRegisterFragmentPresenter(patientRegisterFragment);
    }

    @Test
    public void testInitializeQueriesShouldInitializeQueries() {
        SmartRegisterQueryBuilder smartRegisterQueryBuilder = mock(SmartRegisterQueryBuilder.class);
        doReturn("main_condition").when(smartRegisterQueryBuilder).mainCondition(anyString());
        Whitebox.setInternalState(presenter, "smartRegisterQueryBuilder", smartRegisterQueryBuilder);

        String REGISTER_TABLE = "register_table";
        doReturn(REGISTER_TABLE).when(patientRegisterFragment).getRegisterTableName();

        presenter.initializeQueries("main_condition");
        verify(patientRegisterFragment).initializeAdapter();
        verify(patientRegisterFragment).countExecute();
        verify(patientRegisterFragment).filterandSortInInitializeQueries();
        verify(patientRegisterFragment).initializeQueryParams(eq(REGISTER_TABLE), eq("main_condition"), eq("main_condition"));
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
    public void testGetMainConditionShouldReturnCorrectMainCondition() {
        assertEquals(" (first_name != '' or last_name != '' or patient_id != '')", presenter.getMainCondition());
    }

    @Test
    public void testMainColumnsShouldReturnCorrectMainColumns() throws Exception {
        assertEquals(new String[]{RDT_PATIENTS + "." + "relationalid", RDT_PATIENTS + "." + FIRST_NAME,
                RDT_PATIENTS + "." + LAST_NAME, RDT_PATIENTS + "." + AGE, RDT_PATIENTS + "." + SEX, RDT_PATIENTS + "." +  PATIENT_ID, RDT_PATIENTS + "." + DOB},
                Whitebox.invokeMethod(presenter, "mainColumns", RDT_PATIENTS));
    }

    private void mockStaticMethods() throws JSONException {
        // mock RDTApplication and Drishti context
        mockStatic(RDTApplication.class);
        PowerMockito.when(RDTApplication.getInstance()).thenReturn(rdtApplication);
        PowerMockito.when(rdtApplication.getContext()).thenReturn(applicationContext);

        RDTApplicationPresenter applicationPresenter = PowerMockito.mock(RDTApplicationPresenter.class);

        PowerMockito.when(rdtApplication.getPresenter()).thenReturn(applicationPresenter);

        PowerMockito.when(rdtApplication.getStepStateConfiguration()).thenReturn(stepStateConfig);

        JSONObject jsonObject = Mockito.mock(JSONObject.class);
        Mockito.doReturn("rdt_id").when(jsonObject).optString(eq(RDT_ID_KEY));
        Mockito.doReturn(jsonObject).when(stepStateConfig).getStepStateObj();

        // mock repositories
        PowerMockito.when(applicationContext.getEventClientRepository()).thenReturn(eventClientRepository);
        PowerMockito.when(applicationContext.getUniqueIdRepository()).thenReturn(uniqueIdRepository);
        PowerMockito.when(applicationContext.allSharedPreferences()).thenReturn(PowerMockito.mock(AllSharedPreferences.class));

        // mock client processor
        mockStatic(ClientProcessorForJava.class);
        PowerMockito.when(ClientProcessorForJava.getInstance(context)).thenReturn(clientProcessor);
    }
}
