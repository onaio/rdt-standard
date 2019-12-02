package io.ona.rdt.presenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.contract.RDTJsonFormActivityContract;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.util.StepStateConfig;

import static io.ona.rdt.util.Constants.Step.DISABLED_BACK_PRESS_PAGES;
import static io.ona.rdt.util.Constants.Step.EXPIRATION_DATE_READER_ADDRESS;
import static io.ona.rdt.util.Constants.Step.RDT_EXPIRED_PAGE;
import static io.ona.rdt.util.Constants.Step.RDT_ID_KEY;
import static io.ona.rdt.util.Constants.Step.RDT_ID_LBL_ADDRESSES;
import static io.ona.rdt.util.Constants.Step.SCAN_CARESTART_PAGE;
import static io.ona.rdt.util.Constants.Step.SCAN_QR_PAGE;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by Vincent Karuri on 14/11/2019
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({RDTJsonFormFragment.class, RDTApplication.class})
public class RDTJsonFormActivityPresenterTest {

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

        // test back-press disabled for expiration date and rdt-capture screens
        when(RDTJsonFormFragment.getCurrentStep()).thenReturn(6);
        presenter.onBackPress();
        verifyZeroInteractions(activity);

        when(RDTJsonFormFragment.getCurrentStep()).thenReturn(13);
        presenter.onBackPress();
        verifyZeroInteractions(activity);

        when(RDTJsonFormFragment.getCurrentStep()).thenReturn(14);
        presenter.onBackPress();
        verifyZeroInteractions(activity);

        // allow back-press for other screens
        when(RDTJsonFormFragment.getCurrentStep()).thenReturn(3);
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
