package io.ona.rdt.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import com.vijay.jsonwizard.domain.WidgetArgs;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.views.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;

import java.util.ArrayList;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import io.ona.rdt.PowerMockTest;
import io.ona.rdt.activity.RDTJsonFormActivity;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.callback.OnLabelClickedListener;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.presenter.RDTJsonFormFragmentPresenter;
import io.ona.rdt.util.Constants;
import io.ona.rdt.util.StepStateConfig;

import static com.vijay.jsonwizard.constants.JsonFormConstants.KEY;
import static com.vijay.jsonwizard.constants.JsonFormConstants.NEXT;
import static io.ona.rdt.util.Constants.Step.MANUAL_EXPIRATION_DATE_ENTRY_PAGE;
import static io.ona.rdt.util.Constants.Step.SCAN_CARESTART_PAGE;
import static io.ona.rdt.util.Constants.Step.SCAN_QR_PAGE;
import static io.ona.rdt.widget.RDTLabelFactory.CENTER_LABEL;
import static io.ona.rdt.widget.RDTLabelFactory.HAS_DRAWABLE_END;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created by Vincent Karuri on 20/11/2019
 */
@PrepareForTest({RDTApplication.class})
public class RDTLabelFactoryTest extends PowerMockTest {

    private RDTLabelFactory rdtLabelFactory;

    protected JSONObject jsonObject;

    @Mock
    private RDTJsonFormFragment jsonFormFragment;

    @Mock
    private RDTApplication rdtApplication;

    @Mock
    private StepStateConfig stepStateConfig;

    @Mock
    protected RDTJsonFormFragmentPresenter presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        rdtLabelFactory = new RDTLabelFactory();
        jsonObject = new JSONObject();
    }

    @Test
    public void testEnhanceLabels() throws Exception {
        mockStaticMethods();
        List<View> views = new ArrayList<>();
        ConstraintLayout view = mock(ConstraintLayout.class);

        CustomTextView labelText = mock(CustomTextView.class);
        doReturn(labelText).when(view).findViewById(eq(com.vijay.jsonwizard.R.id.label_text));
        views.add(view);

        jsonObject.put(CENTER_LABEL, true);
        jsonObject.put(HAS_DRAWABLE_END, true);
        Whitebox.setInternalState(rdtLabelFactory, "widgetArgs", getWidgetArgs("key"));
        Whitebox.invokeMethod(rdtLabelFactory, "enhanceLabels", views, jsonObject);

        verify(labelText).setGravity(eq(Gravity.CENTER));
        verify(labelText).setOnClickListener(any(View.OnClickListener.class));
    }

    @Test
    public void testOnClickShouldPerformCorrectAction() throws Exception {
        mockStaticMethods();

        OnLabelClickedListener onLabelClickedListener = new OnLabelClickedListener(getWidgetArgs(Constants.FormFields.LBL_CARE_START));
        onLabelClickedListener.onClick(mock(View.class));
        verify(jsonFormFragment.getRdtActivity()).setRdtType(eq(Constants.RDTType.CARESTART_RDT));
        verify(presenter).moveToNextStep(eq(SCAN_CARESTART_PAGE));

        onLabelClickedListener = new OnLabelClickedListener(getWidgetArgs(Constants.FormFields.LBL_SCAN_QR_CODE));
        onLabelClickedListener.onClick(mock(View.class));
        verify(jsonFormFragment.getRdtActivity()).setRdtType(eq(Constants.RDTType.ONA_RDT));
        verify(presenter).moveToNextStep(eq(SCAN_QR_PAGE));
    }

    @Test
    public void testGetViewsFromJson() throws Exception {
        rdtLabelFactory.getViewsFromJson("step", mock(Context.class), mock(JsonFormFragment.class), mock(JSONObject.class), mock(CommonListener.class), false);
    }

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
        doReturn(rdtJsonFormActivity).when(jsonFormFragment).getRdtActivity();

        doReturn(presenter).when(jsonFormFragment).getPresenter();

        widgetArgs.withFormFragment(jsonFormFragment)
                .withJsonObject(jsonObject);
        return widgetArgs;
    }
}
