package io.ona.rdt.util;

import android.app.Activity;

import org.json.JSONException;
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
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.repository.EventClientRepository;

import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.domain.Patient;

import static io.ona.rdt.util.BaseFormSaverTest.expectedPatient;
import static io.ona.rdt.util.Constants.Form.RDT_TEST_FORM;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created by Vincent Karuri on 27/07/2020
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({ RDTApplication.class})
public class FormLauncherAndSaverTest {

    @Mock
    private FormLauncher formLauncher;
    @Mock
    private Activity activity;
    @Mock
    private RDTApplication rdtApplication;
    @Mock
    private org.smartregister.Context drishtiContext;
    @Mock
    protected EventClientRepository eventClientRepository;

    @Captor
    private ArgumentCaptor<Patient> patientArgumentCaptor;

    private FormLauncherAndSaver formLauncherAndSaver;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockStaticMethods();
        formLauncherAndSaver = new FormLauncherAndSaver();
    }

    @Test
    public void testLaunchFormShouldLaunchFormWithCorrectParameters() throws JSONException {
        ReflectionHelpers.setField(formLauncherAndSaver, "formLauncher", formLauncher);
        formLauncherAndSaver.launchForm(activity, "form_name", BaseFormSaverTest.expectedPatient);
        verify(formLauncher).launchForm(eq(activity), eq("form_name"), patientArgumentCaptor.capture());

        Patient rdtPatient = patientArgumentCaptor.getValue();
        assertNotNull(rdtPatient);
        assertEquals(rdtPatient.getPatientName(), expectedPatient.getPatientName());
        assertEquals(rdtPatient.getPatientSex(), expectedPatient.getPatientSex());
        assertEquals(rdtPatient.getBaseEntityId(), expectedPatient.getBaseEntityId());
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
