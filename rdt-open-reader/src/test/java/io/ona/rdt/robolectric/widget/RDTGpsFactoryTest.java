package io.ona.rdt.robolectric.widget;

import android.view.View;
import android.widget.ScrollView;

import com.vijay.jsonwizard.domain.WidgetArgs;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.interfaces.JsonApi;
import com.vijay.jsonwizard.utils.AppExecutors;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.annotation.Config;
import org.robolectric.util.ReflectionHelpers;

import java.util.List;

import io.ona.rdt.R;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.robolectric.shadow.MockCounter;
import io.ona.rdt.robolectric.shadow.RDTJsonFormUtilsShadow;
import io.ona.rdt.util.RDTGpsDialog;
import io.ona.rdt.util.Utils;
import io.ona.rdt.widget.RDTGpsFactory;

/**
 * Created by Vincent Karuri on 08/11/2019
 */

public class RDTGpsFactoryTest extends WidgetFactoryRobolectricTest {

    @Mock
    private RDTJsonFormFragment formFragment;
    @Mock
    private CommonListener commonListener;
    @Mock
    private JSONObject jsonObject;
    @Mock
    private View formFragmentRootLayout;
    @Mock
    private ScrollView scrollView;
    @Mock
    private View mainLayout;

    private RDTGpsFactory gpsFactory;
    private final String STEP1 = "step1";

    @Before
    public void setUp() throws Exception {
        super.setUp();
        mockMethods();
        gpsFactory = new RDTGpsFactory();
    }

    @Test
    public void testGetViewsFromJsonShouldCorrectlyInitializeWidget() throws Exception {

        List<View> views = gpsFactory.getViewsFromJson(STEP1, jsonFormActivity,
                formFragment, jsonObject, commonListener, false);

        Assert.assertFalse(org.smartregister.util.Utils.isEmptyCollection(views));
        WidgetArgs widgetArgs = ReflectionHelpers.getField(gpsFactory, "widgetArgs");
        Assert.assertEquals(formFragment, widgetArgs.getFormFragment());
        Assert.assertEquals(jsonFormActivity, widgetArgs.getContext());
        Assert.assertEquals(jsonObject, widgetArgs.getJsonObject());
        Assert.assertEquals(commonListener, widgetArgs.getListener());
        Assert.assertFalse(widgetArgs.isPopup());

        View rootLayout = views.get(0);
        Assert.assertTrue(rootLayout.findViewById(R.id.record_button).isEnabled());
        Assert.assertEquals(View.GONE, rootLayout.findViewById(R.id.altitude).getVisibility());
        Assert.assertEquals(View.GONE, rootLayout.findViewById(R.id.accuracy).getVisibility());

        int margin = Utils.convertDpToPixels(jsonFormActivity, jsonFormActivity.getResources()
                        .getDimension(com.vijay.jsonwizard.R.dimen.bottom_navigation_margin));
        Mockito.verify(scrollView).setFillViewport(ArgumentMatchers.eq(true));
        Mockito.verify(scrollView).setPadding(ArgumentMatchers.eq(0), ArgumentMatchers.eq(0), ArgumentMatchers.eq(0), ArgumentMatchers.eq(margin));
        Mockito.verify(mainLayout).setPadding(ArgumentMatchers.eq(margin), ArgumentMatchers.eq(0), ArgumentMatchers.eq(margin), ArgumentMatchers.eq(0));

        RDTGpsDialog gpsDialog = ReflectionHelpers.getField(gpsFactory, "gpsDialog");
        Assert.assertNotNull(gpsDialog);
        Assert.assertEquals(formFragment, gpsDialog.getFormFragment());
    }

    @Config(shadows = {RDTJsonFormUtilsShadow.class})
    @Test
    public void testShowLocationServicesDialogShouldShowDialogForDisabledLocationServices() throws Exception {
        RDTJsonFormUtilsShadow.setMockCounter(new MockCounter());
        Assert.assertEquals(0, RDTJsonFormUtilsShadow.getMockCounter().getCount());
        RDTJsonFormUtilsShadow.setIsLocationServiceDisabled(true);

        List<View> views = gpsFactory.getViewsFromJson(STEP1, jsonFormActivity,
                formFragment, jsonObject, commonListener, false);

        View rootLayout = views.get(0);
        rootLayout.findViewById(R.id.record_button).performClick();

        Assert.assertEquals(1, RDTJsonFormUtilsShadow.getMockCounter().getCount());

        RDTJsonFormUtilsShadow.setMockCounter(null);
        RDTJsonFormUtilsShadow.setIsLocationServiceDisabled(false);
    }

    private void mockMethods() {
        formFragmentRootLayout = Mockito.mock(View.class);
        Mockito.doReturn(formFragmentRootLayout).when(formFragment).getRootLayout();

        JsonApi jsonApi = Mockito.mock(JsonApi.class);
        Mockito.doReturn(new AppExecutors()).when(jsonApi).getAppExecutors();
        Mockito.doReturn(jsonApi).when(formFragment).getJsonApi();

        scrollView = Mockito.mock(ScrollView.class);
        Mockito.doReturn(scrollView).when(formFragmentRootLayout).findViewById(ArgumentMatchers.eq(com.vijay.jsonwizard.R.id.scroll_view));

        mainLayout = Mockito.mock(View.class);
        Mockito.doReturn(mainLayout).when(scrollView).findViewById(ArgumentMatchers.eq(com.vijay.jsonwizard.R.id.main_layout));
    }
}
