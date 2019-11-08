package io.ona.rdt.interactor;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by Vincent Karuri on 08/11/2019
 */
public class RDTJsonFormInteractorTest {

    private RDTJsonFormInteractor interactor;

    @Before
    public void setUp() {
        interactor = new RDTJsonFormInteractor();
    }

    @Test
    public void testSaveFormShouldSaveForm() {
        PatientRegisterFragmentInteractor patientRegisterFragmentInteractor = mock(PatientRegisterFragmentInteractor.class);
        Whitebox.setInternalState(interactor, "patientRegisterFragmentInteractor", patientRegisterFragmentInteractor);

        JSONObject jsonForm = mock(JSONObject.class);
        interactor.saveForm(jsonForm);

        verify(patientRegisterFragmentInteractor).saveForm(eq(jsonForm), isNull());
    }
}
