package io.ona.rdt.widget;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
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
import org.junit.runner.RunWith;
import org.mockito.AdditionalMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.ArrayList;
import java.util.List;

import io.ona.rdt.activity.RDTJsonFormActivity;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.callback.OnLabelClickedListener;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.presenter.RDTJsonFormFragmentPresenter;
import io.ona.rdt.util.Constants;
import io.ona.rdt.util.StepStateConfig;

import static com.vijay.jsonwizard.constants.JsonFormConstants.KEY;
import static com.vijay.jsonwizard.constants.JsonFormConstants.NEXT;
import static io.ona.rdt.util.Constants.Step.COVID_AFFIX_RESPIRATORY_SAMPLE_ID_PAGE;
import static io.ona.rdt.util.Constants.Step.COVID_COLLECT_RESPIRATORY_SPECIMEN_PAGE;
import static io.ona.rdt.util.Constants.Step.COVID_CONDUCT_RDT_PAGE;
import static io.ona.rdt.util.Constants.Step.COVID_MANUAL_RDT_ENTRY_PAGE;
import static io.ona.rdt.util.Constants.Step.COVID_ONE_SCAN_WIDGET_SPECIMEN_PAGE;
import static io.ona.rdt.util.Constants.Step.COVID_RESPIRATORY_SPECIMEN_COLLECTION_OPT_IN_PAGE;
import static io.ona.rdt.util.Constants.Step.COVID_SCAN_BARCODE_PAGE;
import static io.ona.rdt.util.Constants.Step.COVID_TEST_COMPLETE_PAGE;
import static io.ona.rdt.util.Constants.Step.SCAN_CARESTART_PAGE;
import static io.ona.rdt.util.Constants.Step.SCAN_QR_PAGE;
import static io.ona.rdt.widget.RDTLabelFactory.CENTER_LABEL;
import static io.ona.rdt.widget.RDTLabelFactory.HAS_DRAWABLE_END;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created by Vincent Karuri on 20/11/2019
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({RDTApplication.class})
public class RDTLabelFactoryTest {

    private RDTLabelFactory rdtLabelFactory;

    private JSONObject jsonObject;

    @Mock
    private RDTJsonFormFragment jsonFormFragment;

    @Mock
    private RDTApplication rdtApplication;

    @Mock
    private StepStateConfig stepStateConfig;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        rdtLabelFactory = new RDTLabelFactory();
        jsonObject = new JSONObject();
    }

    @Test
    public void testEnhanceLabels() throws Exception {
        List<View> views = new ArrayList<>();
        ConstraintLayout view = mock(ConstraintLayout.class);

        CustomTextView labelText = mock(CustomTextView.class);
        doReturn(labelText).when(view).findViewById(eq(com.vijay.jsonwizard.R.id.label_text));
        views.add(view);

        jsonObject.put(CENTER_LABEL, true);
        jsonObject.put(HAS_DRAWABLE_END, true);
        Whitebox.invokeMethod(rdtLabelFactory, "enhanceLabels", views, jsonObject);

        verify(labelText).setGravity(eq(Gravity.CENTER));
        verify(labelText).setOnClickListener(any(View.OnClickListener.class));
    }

    @Test
    public void testOnClickShouldPerformCorrectAction() throws Exception {
        mockStaticMethods();

        WidgetArgs widgetArgs = new WidgetArgs();
        jsonObject.put(KEY, Constants.FormFields.LBL_CARE_START);
        jsonObject.put(NEXT, "step1");
        RDTJsonFormActivity rdtJsonFormActivity = mock(RDTJsonFormActivity.class);
        doReturn(rdtJsonFormActivity).when(jsonFormFragment).getRdtActivity();

        RDTJsonFormFragmentPresenter presenter = mock(RDTJsonFormFragmentPresenter.class);
        doReturn(presenter).when(jsonFormFragment).getPresenter();

        widgetArgs.withFormFragment(jsonFormFragment)
                .withJsonObject(jsonObject);

        OnLabelClickedListener onLabelClickedListener = new OnLabelClickedListener(widgetArgs);
        onLabelClickedListener.onClick(mock(View.class));
        verify(jsonFormFragment.getRdtActivity()).setRdtType(eq(Constants.RDTType.CARESTART_RDT));
        verify(presenter).moveToNextStep(eq(SCAN_CARESTART_PAGE));

        jsonObject.put(KEY, Constants.FormFields.LBL_SCAN_QR_CODE);
        onLabelClickedListener.onClick(mock(View.class));
        verify(jsonFormFragment.getRdtActivity()).setRdtType(eq(Constants.RDTType.ONA_RDT));
        verify(presenter).moveToNextStep(eq(SCAN_QR_PAGE));

        jsonObject.put(KEY, Constants.FormFields.LBL_SCAN_BARCODE);
        onLabelClickedListener.onClick(mock(View.class));
        verify(presenter).moveToNextStep(eq(COVID_SCAN_BARCODE_PAGE));

        jsonObject.put(KEY, Constants.FormFields.LBL_ENTER_RDT_MANUALLY);
        onLabelClickedListener.onClick(mock(View.class));
        verify(presenter).moveToNextStep(eq(COVID_MANUAL_RDT_ENTRY_PAGE));

        jsonObject.put(KEY, Constants.FormFields.LBL_CONDUCT_RDT);
        onLabelClickedListener.onClick(mock(View.class));
        verify(presenter).moveToNextStep(eq(COVID_CONDUCT_RDT_PAGE));

        jsonObject.put(KEY, Constants.FormFields.LBL_SKIP_RDT_TEST);
        onLabelClickedListener.onClick(mock(View.class));
        verify(presenter).moveToNextStep(eq(COVID_RESPIRATORY_SPECIMEN_COLLECTION_OPT_IN_PAGE));

        jsonObject.put(KEY, Constants.FormFields.LBL_COLLECT_RESPIRATORY_SAMPLE);
        onLabelClickedListener.onClick(mock(View.class));
        verify(presenter).moveToNextStep(eq(COVID_COLLECT_RESPIRATORY_SPECIMEN_PAGE));

        jsonObject.put(KEY, Constants.FormFields.LBL_SKIP_RESPIRATORY_SAMPLE_COLLECTION);
        onLabelClickedListener.onClick(mock(View.class));
        verify(presenter).moveToNextStep(eq(COVID_TEST_COMPLETE_PAGE));

        jsonObject.put(KEY, Constants.FormFields.LBL_SCAN_RESPIRATORY_SPECIMEN_BARCODE);
        onLabelClickedListener.onClick(mock(View.class));
        verify(presenter).moveToNextStep(eq(COVID_ONE_SCAN_WIDGET_SPECIMEN_PAGE));

        jsonObject.put(KEY, Constants.FormFields.LBL_AFFIX_RESPIRATORY_SPECIMEN_LABEL);
        onLabelClickedListener.onClick(mock(View.class));
        verify(presenter).moveToNextStep(eq(COVID_AFFIX_RESPIRATORY_SAMPLE_ID_PAGE));
    }

    @Test
    public void testGetViewsFromJson() throws Exception {
        rdtLabelFactory.getViewsFromJson("step", mock(Context.class), mock(JsonFormFragment.class), mock(JSONObject.class), mock(CommonListener.class), false);
    }

    private void mockStaticMethods() throws JSONException {
        // mock RDTApplication and Drishti context
        mockStatic(RDTApplication.class);
        PowerMockito.when(RDTApplication.getInstance()).thenReturn(rdtApplication);
        PowerMockito.when(rdtApplication.getStepStateConfiguration()).thenReturn(stepStateConfig);

        JSONObject jsonObject = mock(JSONObject.class);
        doReturn(SCAN_CARESTART_PAGE).when(jsonObject).optString(eq(SCAN_CARESTART_PAGE));
        doReturn(SCAN_QR_PAGE).when(jsonObject).optString(eq(SCAN_QR_PAGE));
        doReturn(COVID_SCAN_BARCODE_PAGE).when(jsonObject).optString(eq(COVID_SCAN_BARCODE_PAGE));
        doReturn(COVID_MANUAL_RDT_ENTRY_PAGE).when(jsonObject).optString(eq(COVID_MANUAL_RDT_ENTRY_PAGE));
        doReturn(COVID_CONDUCT_RDT_PAGE).when(jsonObject).optString(eq(COVID_CONDUCT_RDT_PAGE));
        doReturn(COVID_RESPIRATORY_SPECIMEN_COLLECTION_OPT_IN_PAGE).when(jsonObject).optString(eq(COVID_RESPIRATORY_SPECIMEN_COLLECTION_OPT_IN_PAGE));
        doReturn(COVID_COLLECT_RESPIRATORY_SPECIMEN_PAGE).when(jsonObject).optString(eq(COVID_COLLECT_RESPIRATORY_SPECIMEN_PAGE));
        doReturn(COVID_TEST_COMPLETE_PAGE).when(jsonObject).optString(eq(COVID_TEST_COMPLETE_PAGE));
        doReturn(COVID_ONE_SCAN_WIDGET_SPECIMEN_PAGE).when(jsonObject).optString(eq(COVID_ONE_SCAN_WIDGET_SPECIMEN_PAGE));
        doReturn(COVID_AFFIX_RESPIRATORY_SAMPLE_ID_PAGE).when(jsonObject).optString(eq(COVID_AFFIX_RESPIRATORY_SAMPLE_ID_PAGE));
        doReturn(jsonObject).when(stepStateConfig).getStepStateObj();
    }
}
