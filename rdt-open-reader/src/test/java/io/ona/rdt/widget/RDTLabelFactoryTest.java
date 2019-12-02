package io.ona.rdt.widget;

import android.support.constraint.ConstraintLayout;
import android.view.Gravity;
import android.view.View;

import com.vijay.jsonwizard.domain.WidgetArgs;
import com.vijay.jsonwizard.views.CustomTextView;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;

import java.util.ArrayList;
import java.util.List;

import io.ona.rdt.activity.RDTJsonFormActivity;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.presenter.RDTJsonFormFragmentPresenter;
import io.ona.rdt.util.Constants;

import static com.vijay.jsonwizard.constants.JsonFormConstants.KEY;
import static com.vijay.jsonwizard.constants.JsonFormConstants.NEXT;
import static io.ona.rdt.widget.RDTLabelFactory.CENTER_LABEL;
import static io.ona.rdt.widget.RDTLabelFactory.HAS_DRAWABLE_END;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Created by Vincent Karuri on 20/11/2019
 */
public class RDTLabelFactoryTest {

    private RDTLabelFactory rdtLabelFactory;

    @Mock
    private RDTJsonFormFragment jsonFormFragment;

    private JSONObject jsonObject;

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
        Whitebox.invokeMethod(rdtLabelFactory, "enhanceLabels", views, jsonObject, jsonFormFragment);

        verify(labelText).setGravity(eq(Gravity.CENTER));
        verify(labelText).setOnClickListener(any(View.OnClickListener.class));
    }

    @Test
    public void testOnClickShouldPerformCorrectAction() throws Exception {
        WidgetArgs widgetArgs = new WidgetArgs();
        jsonObject.put(KEY, Constants.LBL_CARE_START);
        jsonObject.put(NEXT, "step1");
        RDTJsonFormActivity rdtJsonFormActivity = mock(RDTJsonFormActivity.class);
        doReturn(rdtJsonFormActivity).when(jsonFormFragment).getRdtActivity();

        RDTJsonFormFragmentPresenter presenter = mock(RDTJsonFormFragmentPresenter.class);
        doReturn(presenter).when(jsonFormFragment).getPresenter();

        widgetArgs.withFormFragment(jsonFormFragment)
                .withJsonObject(jsonObject);
        Whitebox.setInternalState(rdtLabelFactory, "widgetArgs", widgetArgs);

        rdtLabelFactory.onClick(mock(View.class));
        verify(jsonFormFragment.getRdtActivity()).setRdtType(eq(Constants.CARESTART_RDT));

        jsonObject.remove(KEY);
        rdtLabelFactory.onClick(mock(View.class));
        verify(jsonFormFragment.getRdtActivity()).setRdtType(eq(Constants.ONA_RDT));
        verify(presenter, times(2)).moveToNextStep(eq(jsonObject.optString(NEXT)));
    }
}
