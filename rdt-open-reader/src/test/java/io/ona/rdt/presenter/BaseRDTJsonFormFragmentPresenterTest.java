package io.ona.rdt.presenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.mockito.AdditionalMatchers;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;
import org.smartregister.repository.EventClientRepository;

import io.ona.rdt.PowerMockTest;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.contract.RDTJsonFormFragmentContract;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.interactor.RDTJsonFormFragmentInteractor;
import io.ona.rdt.interactor.RDTJsonFormInteractor;
import io.ona.rdt.util.StepStateConfig;

import static io.ona.rdt.util.Constants.Step.BLOT_PAPER_TASK_PAGE;
import static io.ona.rdt.util.Constants.Step.EXPIRATION_DATE_READER_ADDRESS;
import static io.ona.rdt.util.Constants.Step.MANUAL_EXPIRATION_DATE_ENTRY_PAGE;
import static io.ona.rdt.util.Constants.Step.PRODUCT_EXPIRED_PAGE;
import static io.ona.rdt.util.Constants.Step.RDT_ID_KEY;
import static io.ona.rdt.util.Constants.Step.RDT_ID_LBL_ADDRESSES;
import static io.ona.rdt.util.Constants.Step.SCAN_CARESTART_PAGE;
import static io.ona.rdt.util.Constants.Step.SCAN_QR_PAGE;
import static io.ona.rdt.util.Constants.Step.TAKE_IMAGE_OF_RDT_PAGE;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by Vincent Karuri on 16/07/2020
 */

@PrepareForTest({RDTJsonFormFragment.class, RDTApplication.class})
public abstract class BaseRDTJsonFormFragmentPresenterTest extends PowerMockTest {

    protected RDTJsonFormFragmentPresenter presenter;
    protected RDTJsonFormFragmentContract.View rdtFormFragmentView;
    protected RDTJsonFormFragment formFragment;
    protected String BLOT_PAPER_TASK_PAGE_NO = "step9";

    @Mock
    protected RDTJsonFormFragmentInteractor interactor;

    @Mock
    private RDTApplication rdtApplication;

    @Mock
    private StepStateConfig stepStateConfig;

    @Mock
    private org.smartregister.Context drishtiContext;

    @Mock
    protected EventClientRepository eventClientRepository;

    @Before
    public void setUp() throws JSONException {
        mockStaticMethods();
        rdtFormFragmentView = spy(new PatientRegisterFragmentStub());
        presenter = new RDTJsonFormFragmentPresenter(rdtFormFragmentView, mock(RDTJsonFormInteractor.class));
        Whitebox.setInternalState(presenter, "rdtJsonFormFragmentInteractor", interactor);
    }


    protected void mockStaticClasses() {
        mockStatic(RDTJsonFormFragment.class);
        formFragment = mock(RDTJsonFormFragment.class);
        when(RDTJsonFormFragment.getFormFragment(anyString())).thenReturn(formFragment);
    }

    protected void mockStaticMethods() throws JSONException {
        // mock RDTApplication and Drishti context
        mockStatic(RDTApplication.class);
        PowerMockito.when(RDTApplication.getInstance()).thenReturn(rdtApplication);
        PowerMockito.when(rdtApplication.getStepStateConfiguration()).thenReturn(stepStateConfig);
        PowerMockito.when(rdtApplication.getContext()).thenReturn(drishtiContext);

        // mock repositories
        PowerMockito.when(drishtiContext.getEventClientRepository()).thenReturn(eventClientRepository);

        JSONObject jsonObject = mock(JSONObject.class);
        doReturn("step1").when(jsonObject).optString(AdditionalMatchers.or(eq(SCAN_CARESTART_PAGE), eq(SCAN_QR_PAGE)));
        doReturn("step1").when(jsonObject).optString(eq(PRODUCT_EXPIRED_PAGE), anyString());
        doReturn("step6").when(jsonObject).optString(eq(PRODUCT_EXPIRED_PAGE));
        doReturn(BLOT_PAPER_TASK_PAGE_NO).when(jsonObject).optString(eq(BLOT_PAPER_TASK_PAGE));
        doReturn("step1:expiration_date_reader").when(jsonObject).optString(eq(EXPIRATION_DATE_READER_ADDRESS), anyString());
        doReturn("step1").when(jsonObject).optString(eq(TAKE_IMAGE_OF_RDT_PAGE));
        doReturn("rdt_id").when(jsonObject).optString(eq(RDT_ID_KEY));
        doReturn(new JSONArray("[\n" +
                "    \"step7:lbl_rdt_id\",\n" +
                "    \"step8:lbl_rdt_id\",\n" +
                "    \"step9:lbl_rdt_id\",\n" +
                "    \"step18:lbl_rdt_id\",\n" +
                "    \"step19:lbl_rdt_id\"\n" +
                "  ]")).when(jsonObject).optJSONArray(eq(RDT_ID_LBL_ADDRESSES));
        doReturn("step20").when(jsonObject).optString(eq(MANUAL_EXPIRATION_DATE_ENTRY_PAGE));
        doReturn(jsonObject).when(stepStateConfig).getStepStateObj();
    }
}
