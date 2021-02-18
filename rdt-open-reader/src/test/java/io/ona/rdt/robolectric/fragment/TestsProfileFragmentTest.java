package io.ona.rdt.robolectric.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.testing.FragmentScenario;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.robolectric.util.ReflectionHelpers;

import io.ona.rdt.R;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.domain.FormattedRDTTestDetails;
import io.ona.rdt.domain.ParasiteProfileResult;
import io.ona.rdt.fragment.TestsProfileFragment;

import static io.ona.rdt.robolectric.repository.ParasiteProfileRepositoryTest.getParasiteProfileResult;
import static io.ona.rdt.util.Constants.Test.BLOODSPOT_Q_PCR;
import static io.ona.rdt.util.Constants.Test.INVALID;
import static io.ona.rdt.util.Constants.Test.MICROSCOPY;
import static io.ona.rdt.util.Constants.Test.NEGATIVE;
import static io.ona.rdt.util.Constants.Test.POSITIVE;
import static io.ona.rdt.util.Constants.Test.Q_PCR;
import static io.ona.rdt.util.Constants.Test.RDT_Q_PCR;
import static io.ona.rdt.util.Constants.Test.RDT_TEST_DETAILS;
import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Vincent Karuri on 27/07/2020
 */
public class TestsProfileFragmentTest extends FragmentRobolectricTest {

    private FragmentScenario<TestsProfileFragment> fragmentScenario;
    private String testDate = "03 Dec 2020";

    @Override
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFragmentCreationShouldInitializeFragmentState() {
        FormattedRDTTestDetails expectedformattedRDTTestDetails = getFormattedRDTTestDetails(NEGATIVE, "");

        Bundle bundle = new Bundle();
        bundle.putParcelable(RDT_TEST_DETAILS, expectedformattedRDTTestDetails);
        fragmentScenario =
                FragmentScenario.launchInContainer(TestsProfileFragment.class,
                        bundle, R.style.AppTheme, null);

        fragmentScenario.onFragment(fragment -> {
            assertNotNull(ReflectionHelpers.getField(fragment, "presenter"));
            FormattedRDTTestDetails actualFormattedRDTTestDetails = ReflectionHelpers.getField(fragment,
                    "formattedRDTTestDetails");
            Assert.assertEquals(expectedformattedRDTTestDetails, actualFormattedRDTTestDetails);

            // validate result labels
            View rootLayout = ReflectionHelpers.getField(fragment, "rootLayout");
            TextView pvResult = rootLayout.findViewById(R.id.tv_rdt_pv_result);
            TextView pfResult = rootLayout.findViewById(R.id.tv_rdt_pf_result);
            Assert.assertEquals(fragment.getContext().getString(R.string.pv_negative_result), pvResult.getText());
            Assert.assertEquals(fragment.getContext().getString(R.string.pf_negative_result), pfResult.getText());

            // validate date, rdt ID, rdt type labels
            Assert.assertEquals(actualFormattedRDTTestDetails.getFormattedRDTId(),
                    ((TextView) rootLayout.findViewById(R.id.tests_profile_rdt_id)).getText());

            View testNameAndDate = rootLayout.findViewById(R.id.rdt_test_name_and_date);
            Assert.assertEquals(actualFormattedRDTTestDetails.getFormattedRDTType(),
                    ((TextView) testNameAndDate.findViewById(R.id.tv_results_label)).getText());
            Assert.assertEquals(actualFormattedRDTTestDetails.getFormattedRDTTestDate(),
                    ((TextView) testNameAndDate.findViewById(R.id.tv_results_date)).getText());

            // experiment type fields
            View experimentTypeLayout = rootLayout.findViewById(R.id.rdt_microscopy_results);
            TextView tvExperimentType = experimentTypeLayout.findViewById(R.id.label_and_date).findViewById(R.id.tv_results_label);
            TextView tvExperimentDate = experimentTypeLayout.findViewById(R.id.label_and_date).findViewById(R.id.tv_results_date);
            Assert.assertEquals(testDate, tvExperimentDate.getText());
            Assert.assertEquals(RDTApplication.getInstance().getString(R.string.microscopy), tvExperimentType.getText());

            experimentTypeLayout = rootLayout.findViewById(R.id.blood_spot_qpcr_results);
            tvExperimentType = experimentTypeLayout.findViewById(R.id.label_and_date).findViewById(R.id.tv_results_label);
            tvExperimentDate = experimentTypeLayout.findViewById(R.id.label_and_date).findViewById(R.id.tv_results_date);
            Assert.assertEquals(testDate, tvExperimentDate.getText());
            Assert.assertEquals(RDTApplication.getInstance().getString(R.string.blood_spot) + Q_PCR, tvExperimentType.getText());

            experimentTypeLayout = rootLayout.findViewById(R.id.rdt_qpcr_results);
            tvExperimentType = experimentTypeLayout.findViewById(R.id.label_and_date).findViewById(R.id.tv_results_label);
            tvExperimentDate = experimentTypeLayout.findViewById(R.id.label_and_date).findViewById(R.id.tv_results_date);
            Assert.assertEquals(testDate, tvExperimentDate.getText());
            Assert.assertEquals(expectedformattedRDTTestDetails.getFormattedRDTType() + Q_PCR, tvExperimentType.getText());
        });
    }

    @Test
    public void testPopulateRDTTestResultsShouldSetInvalidTestResults() {
        FormattedRDTTestDetails expectedformattedRDTTestDetails = getFormattedRDTTestDetails(INVALID, "");

        Bundle bundle = new Bundle();
        bundle.putParcelable(RDT_TEST_DETAILS, expectedformattedRDTTestDetails);

        fragmentScenario =
                FragmentScenario.launchInContainer(TestsProfileFragment.class,
                        bundle, R.style.AppTheme, null);

        fragmentScenario.onFragment(fragment -> {
            View rootLayout = ReflectionHelpers.getField(fragment, "rootLayout");
            Assert.assertEquals(View.GONE, rootLayout.findViewById(R.id.tv_rdt_pv_result).getVisibility());
            TextView pfResult = rootLayout.findViewById(R.id.tv_rdt_pf_result);
            Assert.assertEquals(fragment.getContext().getString(R.string.invalid_result), pfResult.getText());
        });
    }

    @Test
    public void testPopulateRDTTestResultsShouldSetPositiveTestResults() {
        FormattedRDTTestDetails expectedformattedRDTTestDetails = getFormattedRDTTestDetails(POSITIVE,
                StringUtils.join(new String[]{RDTApplication.getInstance().getString(R.string.pf_positive_result) + "," +
                        RDTApplication.getInstance().getString(R.string.pv_positive_result)}, ","));

        Bundle bundle = new Bundle();
        bundle.putParcelable(RDT_TEST_DETAILS, expectedformattedRDTTestDetails);

        fragmentScenario =
                FragmentScenario.launchInContainer(TestsProfileFragment.class,
                        bundle, R.style.AppTheme, null);

        // both pv and pf have results
        fragmentScenario.onFragment(fragment -> {
            verifyRDTResultsAreCorrectlyPopulated(RDTApplication.getInstance().getString(R.string.pf_positive_result),
                    RDTApplication.getInstance().getString(R.string.pv_positive_result), fragment);
        });

        // pf positive result
        expectedformattedRDTTestDetails = getFormattedRDTTestDetails(POSITIVE,
                RDTApplication.getInstance().getString(R.string.pf_positive_result));

        bundle = new Bundle();
        bundle.putParcelable(RDT_TEST_DETAILS, expectedformattedRDTTestDetails);

        fragmentScenario = FragmentScenario.launchInContainer(TestsProfileFragment.class,
                bundle, R.style.AppTheme, null);

        fragmentScenario.onFragment(fragment -> {
            verifyRDTResultsAreCorrectlyPopulated(RDTApplication.getInstance().getString(R.string.pf_positive_result),
                    RDTApplication.getInstance().getString(R.string.pv_negative_result), fragment);
        });

        // pv positive result
        expectedformattedRDTTestDetails = getFormattedRDTTestDetails(POSITIVE,
                RDTApplication.getInstance().getString(R.string.pv_positive_result));

        bundle = new Bundle();
        bundle.putParcelable(RDT_TEST_DETAILS, expectedformattedRDTTestDetails);

        fragmentScenario = FragmentScenario.launchInContainer(TestsProfileFragment.class,
                bundle, R.style.AppTheme, null);

        fragmentScenario.onFragment(fragment -> {
            verifyRDTResultsAreCorrectlyPopulated(RDTApplication.getInstance().getString(R.string.pv_positive_result),
                    RDTApplication.getInstance().getString(R.string.pf_negative_result), fragment);
        });
    }

    @Test
    public void testOnParasiteProfileFetchedShouldCorrectlyPopulateParasiteProfiles() {
        FormattedRDTTestDetails expectedformattedRDTTestDetails = getFormattedRDTTestDetails(INVALID, "");

        Bundle bundle = new Bundle();
        bundle.putParcelable(RDT_TEST_DETAILS, expectedformattedRDTTestDetails);

        fragmentScenario =
                FragmentScenario.launchInContainer(TestsProfileFragment.class,
                        bundle, R.style.AppTheme, null);

        fragmentScenario.onFragment(fragment -> {
            // microscopy results
            verifyParasiteProfileIsCorrectlyPopulated(fragment, R.id.rdt_microscopy_results, MICROSCOPY);
            // bloodspot qpcr results
            verifyParasiteProfileIsCorrectlyPopulated(fragment, R.id.blood_spot_qpcr_results, BLOODSPOT_Q_PCR);
            // rdt qpcr results
            verifyParasiteProfileIsCorrectlyPopulated(fragment, R.id.rdt_qpcr_results, RDT_Q_PCR);
        });
    }

    private void verifyRDTResultsAreCorrectlyPopulated(String pfResultStr, String pvResultStr, TestsProfileFragment fragment) {
        View rootLayout = ReflectionHelpers.getField(fragment, "rootLayout");
        TextView pvResult = rootLayout.findViewById(R.id.tv_rdt_pv_result);
        TextView pfResult = rootLayout.findViewById(R.id.tv_rdt_pf_result);
        Assert.assertEquals(pfResultStr, pfResult.getText());
        Assert.assertEquals(pvResultStr, pvResult.getText());
    }

    private void verifyParasiteProfileIsCorrectlyPopulated(TestsProfileFragment fragment, int id, String experimentType) {
        View rootLayout = ReflectionHelpers.getField(fragment, "rootLayout");
        View parasiteProfile = rootLayout.findViewById(id);
        ParasiteProfileResult parasiteProfileResult = getParasiteProfileResult();

        TextView tvFalciparum = parasiteProfile.findViewById(R.id.tv_qpcr_falciparum);
        Assert.assertEquals(String.format(getFormatter(), RDTApplication.getInstance().getString(R.string.falciparum_prefix),
                capitalize(parasiteProfileResult.getpFalciparum())), tvFalciparum.getText());

        TextView tvVivax = parasiteProfile.findViewById(R.id.tv_qpcr_vivax);
        Assert.assertEquals(String.format(getFormatter(), RDTApplication.getInstance().getString(R.string.vivax_prefix),
                capitalize(parasiteProfileResult.getpVivax())), tvVivax.getText());

        TextView tvMalariae = parasiteProfile.findViewById(R.id.tv_qpcr_malariae);
        Assert.assertEquals(String.format(getFormatter(), RDTApplication.getInstance().getString(R.string.malariae_prefix),
                capitalize(parasiteProfileResult.getpMalariae())), tvMalariae.getText());

        TextView tvOvale = parasiteProfile.findViewById(R.id.tv_qpcr_ovale);
        Assert.assertEquals(String.format(getFormatter(), RDTApplication.getInstance().getString(R.string.ovale_prefix),
                capitalize(parasiteProfileResult.getpOvale())), tvOvale.getText());

        TextView tvGameto = parasiteProfile.findViewById(R.id.tv_qpcr_gameto);
        Assert.assertEquals(String.format(getFormatter(), RDTApplication.getInstance().getString(R.string.gameto_prefix),
                capitalize(parasiteProfileResult.getPfGameto())), tvGameto.getText());
    }

    private String getFormatter() {
        return "%s %s";
    }

    private FormattedRDTTestDetails getFormattedRDTTestDetails(String testResult, String formattedTestResults) {
        FormattedRDTTestDetails expectedformattedRDTTestDetails = new FormattedRDTTestDetails();
        expectedformattedRDTTestDetails.setFormattedRDTId("rdt_id");
        expectedformattedRDTTestDetails.setFormattedRDTTestDate(testDate);
        expectedformattedRDTTestDetails.setFormattedRDTType("Carestart");
        expectedformattedRDTTestDetails.setFormattedTestResults(formattedTestResults);
        expectedformattedRDTTestDetails.setTestResult(testResult);
        return expectedformattedRDTTestDetails;
    }

    @Override
    public FragmentScenario getFragmentScenario() {
        return fragmentScenario;
    }
}
