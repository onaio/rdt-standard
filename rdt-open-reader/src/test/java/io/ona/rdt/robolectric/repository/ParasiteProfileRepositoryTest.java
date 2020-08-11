package io.ona.rdt.robolectric.repository;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import io.ona.rdt.domain.ParasiteProfileResult;
import io.ona.rdt.repository.ParasiteProfileRepository;
import io.ona.rdt.robolectric.RobolectricTest;

import static io.ona.rdt.repository.ParasiteProfileRepository.EXPERIMENT_TYPE;
import static io.ona.rdt.util.Constants.FormFields.RDT_ID;
import static io.ona.rdt.util.Constants.Table.MICROSCOPY_RESULTS;
import static io.ona.rdt.util.Constants.Table.PCR_RESULTS;
import static io.ona.rdt.util.Constants.Test.BLOODSPOT_Q_PCR;
import static io.ona.rdt.util.Constants.Test.MICROSCOPY;
import static io.ona.rdt.util.Constants.Test.RDT_Q_PCR;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by Vincent Karuri on 29/07/2020
 */
public class ParasiteProfileRepositoryTest extends RobolectricTest {

    private ParasiteProfileRepository parasiteProfileRepository;

    @Before
    public void setUp() {
        parasiteProfileRepository = new ParasiteProfileRepository();
    }

    @Test
    public void testCreateIndexesShouldCreateIndices() {
        Cursor cursor = mock(Cursor.class);
        doReturn(1).when(cursor).getCount();
        SQLiteDatabase db = mock(SQLiteDatabase.class);
        doReturn(cursor).when(db).rawQuery(anyString(), any());
        parasiteProfileRepository.createIndexes(db);
        verify(db).execSQL(eq("CREATE INDEX IF NOT EXISTS pcr_results_rdt_id_and_experiment_type_index ON " + PCR_RESULTS + "(" + RDT_ID + "," + EXPERIMENT_TYPE + ")"));
        verify(db).execSQL(eq("CREATE INDEX IF NOT EXISTS microscopy_results_rdt_id_index ON " + MICROSCOPY_RESULTS + "(" + RDT_ID + ")"));
    }

    @Test
    public void testGetParasiteProfilesShouldFetchParasiteProfilesByRDTIDAndExperimentType() {
        verifyParasiteProfileResultsAreCorrect(parasiteProfileRepository.getParasiteProfiles("rdt_id", MICROSCOPY_RESULTS, MICROSCOPY), MICROSCOPY);
        verifyParasiteProfileResultsAreCorrect(parasiteProfileRepository.getParasiteProfiles("rdt_id", PCR_RESULTS, RDT_Q_PCR), RDT_Q_PCR);
        verifyParasiteProfileResultsAreCorrect(parasiteProfileRepository.getParasiteProfiles("rdt_id", PCR_RESULTS, BLOODSPOT_Q_PCR), BLOODSPOT_Q_PCR);
    }

    private void verifyParasiteProfileResultsAreCorrect(List<ParasiteProfileResult> parasiteProfileResults, String experimentType) {
        ParasiteProfileResult actualParasiteProfileResult = parasiteProfileResults.get(0);
        ParasiteProfileResult expectedParasiteProfileResult = getParasiteProfileResult();
        assertEquals(expectedParasiteProfileResult.getRdtId(), actualParasiteProfileResult.getRdtId());
        assertEquals(experimentType, actualParasiteProfileResult.getExperimentType());
        assertEquals(expectedParasiteProfileResult.getExperimentDate(), actualParasiteProfileResult.getExperimentDate());
        assertEquals(expectedParasiteProfileResult.getpFalciparum(), actualParasiteProfileResult.getpFalciparum());
        assertEquals(expectedParasiteProfileResult.getpMalariae(), actualParasiteProfileResult.getpMalariae());
        assertEquals(actualParasiteProfileResult.getpOvale(), actualParasiteProfileResult.getpOvale());
        assertEquals(expectedParasiteProfileResult.getpVivax(), actualParasiteProfileResult.getpVivax());
        assertEquals(expectedParasiteProfileResult.getPfGameto(), actualParasiteProfileResult.getPfGameto());
    }

    public static ParasiteProfileResult getParasiteProfileResult() {
        ParasiteProfileResult parasiteProfileResult = new ParasiteProfileResult();
        parasiteProfileResult.setRdtId("rdt_id");
        parasiteProfileResult.setExperimentDate("2020-12-03T21:00:00.000Z");
        parasiteProfileResult.setpFalciparum("negative");
        parasiteProfileResult.setpMalariae("positive");
        parasiteProfileResult.setpOvale("negative");
        parasiteProfileResult.setpVivax("positive");
        parasiteProfileResult.setPfGameto("negative");
        return parasiteProfileResult;
    }
}
