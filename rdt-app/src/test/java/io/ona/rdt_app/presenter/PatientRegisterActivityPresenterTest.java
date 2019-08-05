package io.ona.rdt_app.presenter;

import android.app.Activity;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import io.ona.rdt_app.contract.PatientRegisterActivityContract;

import static io.ona.rdt_app.interactor.PatientRegisterFragmentInteractorTest.JSON_FORM;
import static org.mockito.Mockito.mock;

/**
 * Created by Vincent Karuri on 02/08/2019
 */
public class PatientRegisterActivityPresenterTest {

    private PatientRegisterActivityContract.View activity;

    private PatientRegisterActivityPresenter presenter;

    @Before
    public void setUp() {
        activity = new PatientRegisterActivityStub();
        presenter = new PatientRegisterActivityPresenter(activity, mock(Activity.class));
    }

    @Test
    public void testSaveFormShouldSaveForm() throws JSONException {
        presenter.saveForm(JSON_FORM);
    }
}
