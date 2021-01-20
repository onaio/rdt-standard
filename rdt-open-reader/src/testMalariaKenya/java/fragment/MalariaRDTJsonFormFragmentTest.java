package fragment;

import android.os.Bundle;

import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.robolectric.util.ReflectionHelpers;

import androidx.fragment.app.testing.FragmentScenario;
import io.ona.rdt.R;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.robolectric.RobolectricTest;
import io.ona.rdt.robolectric.fragment.RDTJsonFormFragmentTest;

import static io.ona.rdt.util.Constants.Step.TWENTY_MIN_COUNTDOWN_TIMER_PAGE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Vincent Karuri on 24/07/2020
 */
public class MalariaRDTJsonFormFragmentTest extends RobolectricTest {

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testIs20minTimerPageShouldReturnCorrectStatus() {
        Bundle bundle = new Bundle();
        bundle.putString(JsonFormConstants.STEPNAME, JsonFormConstants.STEP1);

        FragmentScenario<RDTJsonFormFragment> fragmentScenario =
                FragmentScenario.launchInContainer(RDTJsonFormFragment.class,
                        bundle, R.style.AppTheme, new RDTJsonFormFragmentTest.RDTJsonFormFragmentFactory());

        fragmentScenario.onFragment(fragment -> {
            String step = RDTApplication.getInstance().getStepStateConfiguration()
                    .getStepStateObj()
                    .optString(TWENTY_MIN_COUNTDOWN_TIMER_PAGE);
            assertTrue(ReflectionHelpers.callInstanceMethod(fragment, "is20minTimerPage",
                    ReflectionHelpers.ClassParameter.from(String.class, step)));
            assertFalse(ReflectionHelpers.callInstanceMethod(fragment, "is20minTimerPage",
                    ReflectionHelpers.ClassParameter.from(String.class, getStep(step, 1))));
        });
    }

    private String getStep(String step, int offset) {
        int stepNum = Integer.parseInt(step.substring(4)) + 1;
        return "step" + stepNum;
    }
}
