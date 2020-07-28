package io.ona.rdt.robolectric.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.util.ReflectionHelpers;

import androidx.fragment.app.testing.FragmentScenario;
import io.ona.rdt.R;
import io.ona.rdt.domain.FormattedRDTTestDetails;
import io.ona.rdt.fragment.TestsProfileFragment;
import io.ona.rdt.robolectric.RobolectricTest;

import static io.ona.rdt.util.Constants.Test.INVALID;
import static io.ona.rdt.util.Constants.Test.NEGATIVE;
import static io.ona.rdt.util.Constants.Test.POSITIVE;
import static io.ona.rdt.util.Constants.Test.RDT_TEST_DETAILS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Vincent Karuri on 27/07/2020
 */
public class TestsProfileFragmentTest extends RobolectricTest {

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFragmentCreationShouldInitializeFragmentState() {

        FormattedRDTTestDetails expectedformattedRDTTestDetails = getFormattedRDTTestDetails(NEGATIVE, "");

        Bundle bundle = new Bundle();
        bundle.putParcelable(RDT_TEST_DETAILS, expectedformattedRDTTestDetails);

        FragmentScenario<TestsProfileFragment> fragmentScenario =
                FragmentScenario.launchInContainer(TestsProfileFragment.class,
                        bundle, R.style.AppTheme, null);

        fragmentScenario.onFragment(fragment -> {
            assertNotNull(ReflectionHelpers.getField(fragment, "presenter"));
            FormattedRDTTestDetails actualFormattedRDTTestDetails = ReflectionHelpers.getField(fragment,
                    "formattedRDTTestDetails");
            assertEquals(expectedformattedRDTTestDetails, actualFormattedRDTTestDetails);

            View rootLayout = ReflectionHelpers.getField(fragment, "rootLayout");
            TextView pvResult = rootLayout.findViewById(R.id.tv_rdt_pv_result);
            TextView pfResult = rootLayout.findViewById(R.id.tv_rdt_pf_result);
            assertEquals(fragment.getContext().getString(R.string.pv_negative_result), pvResult.getText());
            assertEquals(fragment.getContext().getString(R.string.pf_negative_result), pfResult.getText());
        });
    }

    @Test
    public void testPopulateRDTTestResultsShouldSetInvalidTestResults() {
        FormattedRDTTestDetails expectedformattedRDTTestDetails = getFormattedRDTTestDetails(INVALID, "");

        Bundle bundle = new Bundle();
        bundle.putParcelable(RDT_TEST_DETAILS, expectedformattedRDTTestDetails);

        FragmentScenario<TestsProfileFragment> fragmentScenario =
                FragmentScenario.launchInContainer(TestsProfileFragment.class,
                        bundle, R.style.AppTheme, null);

        fragmentScenario.onFragment(fragment -> {
            View rootLayout = ReflectionHelpers.getField(fragment, "rootLayout");
            assertEquals(View.GONE, rootLayout.findViewById(R.id.tv_rdt_pv_result).getVisibility());
            TextView pfResult = rootLayout.findViewById(R.id.tv_rdt_pf_result);
            assertEquals(fragment.getContext().getString(R.string.invalid_result), pfResult.getText());
        });
    }


    @Test
    public void testPopulateRDTTestResultsShouldSetPositiveTestResults() {
        FormattedRDTTestDetails expectedformattedRDTTestDetails = getFormattedRDTTestDetails(POSITIVE, "positive, positive");

        Bundle bundle = new Bundle();
        bundle.putParcelable(RDT_TEST_DETAILS, expectedformattedRDTTestDetails);

        FragmentScenario<TestsProfileFragment> fragmentScenario =
                FragmentScenario.launchInContainer(TestsProfileFragment.class,
                        bundle, R.style.AppTheme, null);

        // both pv and pf have results
        fragmentScenario.onFragment(fragment -> {
            View rootLayout = ReflectionHelpers.getField(fragment, "rootLayout");
            TextView pvResult = rootLayout.findViewById(R.id.tv_rdt_pv_result);
            TextView pfResult = rootLayout.findViewById(R.id.tv_rdt_pf_result);
            assertEquals("positive", pfResult.getText());
            assertEquals("positive", pvResult.getText());
        });

        // pf positive result
        expectedformattedRDTTestDetails = getFormattedRDTTestDetails(POSITIVE,
                RuntimeEnvironment.application.getString(R.string.pf_positive_result));

        bundle = new Bundle();
        bundle.putParcelable(RDT_TEST_DETAILS, expectedformattedRDTTestDetails);

        fragmentScenario =
                FragmentScenario.launchInContainer(TestsProfileFragment.class,
                        bundle, R.style.AppTheme, null);

        fragmentScenario.onFragment(fragment -> {
            View rootLayout = ReflectionHelpers.getField(fragment, "rootLayout");
            TextView pvResult = rootLayout.findViewById(R.id.tv_rdt_pv_result);
            TextView pfResult = rootLayout.findViewById(R.id.tv_rdt_pf_result);
            assertEquals(RuntimeEnvironment.application.getString(R.string.pf_positive_result), pfResult.getText());
            assertEquals(RuntimeEnvironment.application.getString(R.string.pv_negative_result), pvResult.getText());
        });

        // pv positive result
        expectedformattedRDTTestDetails = getFormattedRDTTestDetails(POSITIVE,
                RuntimeEnvironment.application.getString(R.string.pv_positive_result));

        bundle = new Bundle();
        bundle.putParcelable(RDT_TEST_DETAILS, expectedformattedRDTTestDetails);

        fragmentScenario =
                FragmentScenario.launchInContainer(TestsProfileFragment.class,
                        bundle, R.style.AppTheme, null);

        fragmentScenario.onFragment(fragment -> {
            View rootLayout = ReflectionHelpers.getField(fragment, "rootLayout");
            TextView pvResult = rootLayout.findViewById(R.id.tv_rdt_pv_result);
            TextView pfResult = rootLayout.findViewById(R.id.tv_rdt_pf_result);
            assertEquals(RuntimeEnvironment.application.getString(R.string.pv_positive_result), pfResult.getText());
            assertEquals(RuntimeEnvironment.application.getString(R.string.pf_negative_result), pvResult.getText());
        });
    }

    private FormattedRDTTestDetails getFormattedRDTTestDetails(String testResult, String formattedTestResults) {
        FormattedRDTTestDetails expectedformattedRDTTestDetails = new FormattedRDTTestDetails();
        expectedformattedRDTTestDetails.setFormattedRDTId("rdt_id");
        expectedformattedRDTTestDetails.setFormattedRDTTestDate("11/20/2202");
        expectedformattedRDTTestDetails.setFormattedRDTType("malaria");
        expectedformattedRDTTestDetails.setFormattedTestResults(formattedTestResults);
        expectedformattedRDTTestDetails.setTestResult(testResult);
        return expectedformattedRDTTestDetails;
    }
}
