package io.ona.rdt.robolectric.fragment;

import android.os.Bundle;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.util.ReflectionHelpers;

import java.util.ArrayList;

import androidx.fragment.app.testing.FragmentScenario;
import io.ona.rdt.R;
import io.ona.rdt.domain.FormattedRDTTestDetails;
import io.ona.rdt.domain.ParasiteProfileResult;
import io.ona.rdt.fragment.TestsProfileFragment;
import io.ona.rdt.presenter.TestsProfileFragmentPresenter;
import io.ona.rdt.robolectric.RobolectricTest;

import static io.ona.rdt.util.Constants.Test.RDT_TEST_DETAILS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

/**
 * Created by Vincent Karuri on 27/07/2020
 */
public class TestsProfileFragmentTest extends RobolectricTest {

    @Mock
    private TestsProfileFragmentPresenter presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFragmentCreationShouldInitializeFragmentState() {
        doReturn(new ArrayList<ParasiteProfileResult>()).when(presenter)
                .getParasiteProfiles(anyString(), anyString(), anyString());

        FormattedRDTTestDetails expectedformattedRDTTestDetails = new FormattedRDTTestDetails();
        expectedformattedRDTTestDetails.setFormattedRDTId("rdt_id");
        expectedformattedRDTTestDetails.setFormattedRDTTestDate("11/20/2202");
        expectedformattedRDTTestDetails.setFormattedRDTType("malaria");
        expectedformattedRDTTestDetails.setFormattedTestResults("test_results");
        expectedformattedRDTTestDetails.setTestResult("test_result");

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
        });
    }
}
