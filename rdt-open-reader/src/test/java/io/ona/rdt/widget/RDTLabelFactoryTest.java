package io.ona.rdt.widget;

import android.support.constraint.ConstraintLayout;
import android.view.Gravity;
import android.view.View;

import com.vijay.jsonwizard.domain.WidgetArgs;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.views.CustomTextView;
import com.vijay.jsonwizard.widgets.CountDownTimerFactory;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.ArrayList;
import java.util.List;

import io.ona.rdt.activity.RDTJsonFormActivity;

import static io.ona.rdt.widget.RDTLabelFactory.CENTER_LABEL;
import static io.ona.rdt.widget.RDTLabelFactory.HAS_DRAWABLE_END;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.support.membermodification.MemberMatcher.methods;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;

/**
 * Created by Vincent Karuri on 20/11/2019
 */
public class RDTLabelFactoryTest {

    private RDTLabelFactory rdtLabelFactory;

    @Before
    public void setUp() {
        rdtLabelFactory = new RDTLabelFactory();
    }

    @Test
    public void testEnhanceLabels() throws Exception {
        List<View> views = new ArrayList<>();
        ConstraintLayout view = mock(ConstraintLayout.class);

        CustomTextView labelText = mock(CustomTextView.class);
        doReturn(labelText).when(view).findViewById(eq(com.vijay.jsonwizard.R.id.label_text));
        views.add(view);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CENTER_LABEL, true);
        jsonObject.put(HAS_DRAWABLE_END, true);
        Whitebox.invokeMethod(rdtLabelFactory, "enhanceLabels", views, jsonObject, mock(JsonFormFragment.class));

        verify(labelText).setGravity(eq(Gravity.CENTER));
        verify(labelText).setOnClickListener(any(View.OnClickListener.class));
    }
}
