package io.ona.rdt.presenter;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.smartregister.repository.EventClientRepository;

import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.callback.OnFormSavedCallback;
import io.ona.rdt.contract.PatientProfileActivityContract;
import io.ona.rdt.interactor.PatientProfileActivityInteractor;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created by Vincent Karuri on 27/07/2020
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({RDTApplication.class})
public class PatientProfileActivityPresenterTest {

    @Mock
    private PatientProfileActivityInteractor interactor;
    @Mock
    PatientProfileActivityContract.View activity;
    @Mock
    private RDTApplication rdtApplication;
    @Mock
    private org.smartregister.Context drishtiContext;
    @Mock
    protected EventClientRepository eventClientRepository;

    @Captor
    private ArgumentCaptor<JSONObject> jsonObjectCaptor;

    private PatientProfileActivityPresenter presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockStaticMethods();
        presenter = new PatientProfileActivityPresenter(activity);
    }

    @Test
    public void testSaveFormShouldSaveForm() throws JSONException {
        OnFormSavedCallback callback = mock(OnFormSavedCallback.class);
        JSONObject jsonForm = new JSONObject();
        jsonForm.put("key", "value");
        Whitebox.setInternalState(presenter, "interactor", interactor);
        presenter.saveForm(jsonForm.toString(), callback);
        verify(interactor).saveForm(jsonObjectCaptor.capture(), eq(callback));
        assertEquals(jsonForm.toString(), jsonObjectCaptor.getValue().toString());
    }

    private void mockStaticMethods() {
        // mock RDTApplication and Drishti context
        mockStatic(RDTApplication.class);
        PowerMockito.when(RDTApplication.getInstance()).thenReturn(rdtApplication);
        PowerMockito.when(rdtApplication.getContext()).thenReturn(drishtiContext);

        // mock repositories
        PowerMockito.when(drishtiContext.getEventClientRepository()).thenReturn(eventClientRepository);
    }
}
