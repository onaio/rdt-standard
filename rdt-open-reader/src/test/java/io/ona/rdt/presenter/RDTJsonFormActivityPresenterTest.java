package io.ona.rdt.presenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import io.ona.rdt.PowerMockTest;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.contract.RDTJsonFormActivityContract;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.util.StepStateConfig;

import static io.ona.rdt.util.Constants.Encounter.RDT_TEST;
import static io.ona.rdt.util.Constants.FormFields.ENCOUNTER_TYPE;
import static io.ona.rdt.util.Constants.Step.DISABLED_BACK_PRESS_PAGES;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by Vincent Karuri on 14/11/2019
 */
@PrepareForTest({RDTJsonFormFragment.class, RDTApplication.class})
public class RDTJsonFormActivityPresenterTest extends PowerMockTest {

    @Mock
    private RDTJsonFormActivityContract.View activity;

    @Mock
    private RDTApplication rdtApplication;

    @Mock
    private StepStateConfig stepStateConfig;

    private RDTJsonFormActivityPresenter presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = new RDTJsonFormActivityPresenter(activity);
    }

    @Test
    public void testOnBackPress() throws JSONException {
        mockStaticClasses();
        mockStaticMethods();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ENCOUNTER_TYPE, RDT_TEST);
        doReturn(jsonObject).when(activity).getmJSONObject();

        // verify back-press can be disabled
        when(RDTJsonFormFragment.getCurrentStep()).thenReturn("step6");
        presenter.onBackPress();
        verify(activity, never()).onBackPress();

        when(RDTJsonFormFragment.getCurrentStep()).thenReturn("step13");
        presenter.onBackPress();
        verify(activity, never()).onBackPress();

        when(RDTJsonFormFragment.getCurrentStep()).thenReturn("step14");
        presenter.onBackPress();
        verify(activity, never()).onBackPress();

        // verify back-press is allowed for all steps that are not flagged
        when(RDTJsonFormFragment.getCurrentStep()).thenReturn("step3");
        presenter.onBackPress();
        verify(activity).onBackPress();
    }

    private void mockStaticClasses() {
        mockStatic(RDTJsonFormFragment.class);
    }

    private void mockStaticMethods() throws JSONException {
        // mock RDTApplication and Drishti context
        mockStatic(RDTApplication.class);
        PowerMockito.when(RDTApplication.getInstance()).thenReturn(rdtApplication);
        PowerMockito.when(rdtApplication.getStepStateConfiguration()).thenReturn(stepStateConfig);


        JSONObject jsonObject = mock(JSONObject.class);
        doReturn(new JSONArray("[\n" +
                "    \"step6\",\n" +
                "    \"step13\",\n" +
                "    \"step14\"\n" +
                "  ]")).when(jsonObject).optJSONArray(eq(DISABLED_BACK_PRESS_PAGES));

        doReturn(jsonObject).when(stepStateConfig).getStepStateObj();
    }
}
