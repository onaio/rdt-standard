package io.ona.rdt.widget;

import com.vijay.jsonwizard.domain.WidgetArgs;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import io.ona.rdt.PowerMockTest;
import io.ona.rdt.activity.RDTJsonFormActivity;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.presenter.RDTJsonFormFragmentPresenter;
import io.ona.rdt.util.StepStateConfig;

import static com.vijay.jsonwizard.constants.JsonFormConstants.KEY;
import static com.vijay.jsonwizard.constants.JsonFormConstants.NEXT;
import static io.ona.rdt.util.Constants.Step.MANUAL_EXPIRATION_DATE_ENTRY_PAGE;
import static io.ona.rdt.util.Constants.Step.SCAN_CARESTART_PAGE;
import static io.ona.rdt.util.Constants.Step.SCAN_QR_PAGE;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created by Vincent Karuri on 16/07/2020
 */

@PrepareForTest({RDTApplication.class})
public abstract class BaseRDTLabelFactoryTest extends PowerMockTest {

    @Mock
    protected RDTJsonFormFragmentPresenter presenter;
    @Mock
    private RDTApplication rdtApplication;
    @Mock
    private StepStateConfig stepStateConfig;
    @Mock
    protected RDTJsonFormFragment jsonFormFragment;

    protected JSONObject jsonObject;
    protected RDTLabelFactory rdtLabelFactory;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        rdtLabelFactory = new RDTLabelFactory();
        jsonObject = new JSONObject();
    }

    protected abstract RDTLabelFactory getRdtLabelFactory();

    protected void mockStaticMethods() throws JSONException {
        // mock RDTApplication and Drishti context
        mockStatic(RDTApplication.class);
        PowerMockito.when(RDTApplication.getInstance()).thenReturn(rdtApplication);
        PowerMockito.when(rdtApplication.getStepStateConfiguration()).thenReturn(stepStateConfig);

        jsonObject.put(SCAN_CARESTART_PAGE, SCAN_CARESTART_PAGE);
        jsonObject.put(SCAN_QR_PAGE, SCAN_QR_PAGE);
        jsonObject.put(MANUAL_EXPIRATION_DATE_ENTRY_PAGE, MANUAL_EXPIRATION_DATE_ENTRY_PAGE);
        doReturn(jsonObject).when(stepStateConfig).getStepStateObj();
    }

    protected WidgetArgs getWidgetArgs(String widgetKey) throws JSONException {
        WidgetArgs widgetArgs = new WidgetArgs();
        jsonObject.put(KEY, widgetKey);
        jsonObject.put(NEXT, "step1");
        RDTJsonFormActivity rdtJsonFormActivity = mock(RDTJsonFormActivity.class);
        Mockito.doReturn(rdtJsonFormActivity).when(jsonFormFragment).getRdtActivity();

        Mockito.doReturn(presenter).when(jsonFormFragment).getPresenter();

        widgetArgs.withFormFragment(jsonFormFragment)
                .withJsonObject(jsonObject);
        return widgetArgs;
    }
}
