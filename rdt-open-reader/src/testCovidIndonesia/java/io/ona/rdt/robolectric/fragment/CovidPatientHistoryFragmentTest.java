package io.ona.rdt.robolectric.fragment;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.recyclerview.widget.RecyclerView;
import io.ona.rdt.R;
import io.ona.rdt.fragment.CovidPatientHistoryFragment;
import io.ona.rdt.viewholder.CovidPatientHistoryViewHolder;

/**
 * Created by Vincent Karuri on 18/08/2020
 */
public class CovidPatientHistoryFragmentTest extends FragmentRobolectricTest {

    private CovidPatientHistoryFragment covidPatientHistoryFragment;
    private FragmentScenario<CovidPatientHistoryFragment> fragmentScenario;

    @Before
    public void setUp() {
        fragmentScenario = FragmentScenario.launchInContainer(CovidPatientHistoryFragment.class, null,
                R.style.AppTheme, null);
        fragmentScenario.onFragment(
                fragment -> {
                    covidPatientHistoryFragment = fragment;
                }
        );
    }

    @Test
    public void testOnCreateViewShouldCorrectlyPopulatePatientHistorySections() {
        // rdt history section
        assertListValuesAreCorrect(1, R.id.patient_rdt_history_section);
        assertListValuesAreCorrect(2, R.id.patient_rdt_history_section);
        assertListValuesAreCorrect(3, R.id.patient_rdt_history_section);

        // symptoms history section
        assertListValuesAreCorrect(1, R.id.patient_symptoms_history_section);
        assertListValuesAreCorrect(2, R.id.patient_symptoms_history_section);
        assertListValuesAreCorrect(3, R.id.patient_symptoms_history_section);

        // supporting investigations history section
        assertListValuesAreCorrect(1, R.id.patient_supporting_investigations_history_section);
        assertListValuesAreCorrect(2, R.id.patient_supporting_investigations_history_section);
        assertListValuesAreCorrect(3, R.id.patient_supporting_investigations_history_section);

        // samples history section
        assertListValuesAreCorrect(1, R.id.patient_samples_history_section);
        assertListValuesAreCorrect(2, R.id.patient_samples_history_section);
        assertListValuesAreCorrect(3, R.id.patient_samples_history_section);
    }

    private void assertListValuesAreCorrect(int position, int layoutId) {
        RecyclerView recyclerView = covidPatientHistoryFragment.getView()
                .findViewById(layoutId)
                .findViewById(R.id.patient_history_entries);
        CovidPatientHistoryViewHolder viewHolder = (CovidPatientHistoryViewHolder) recyclerView
                .findViewHolderForAdapterPosition(position - 1);
        Assert.assertEquals(String.format("value%d", position), viewHolder.getTvHistoryValue().getText());
        Assert.assertEquals(String.format("key%d", position), viewHolder.getTvHistoryKey().getText());
    }

    @Override
    public FragmentScenario getFragmentScenario() {
        return fragmentScenario;
    }
}
