package io.ona.rdt.robolectric.repository;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import io.ona.rdt.domain.RDTTestDetails;
import io.ona.rdt.repository.RDTTestsRepository;
import io.ona.rdt.robolectric.RobolectricTest;

import static io.ona.rdt.robolectric.shadow.SQLiteOpenHelperShadow.getParasiteTypes;
import static io.ona.rdt.util.Constants.Table.RDT_TESTS;
import static io.ona.rdt.util.Utils.convertJsonArrToListOfStrings;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by Vincent Karuri on 28/07/2020
 */
public class RDTTestsRepositoryTest extends RobolectricTest {

    private RDTTestsRepository rdtTestsRepository;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        rdtTestsRepository = new RDTTestsRepository();
    }

    @Test
    public void testCreateIndexesShouldCreateIndices() {
        Cursor cursor = mock(Cursor.class);
        doReturn(1).when(cursor).getCount();
        SQLiteDatabase db = mock(SQLiteDatabase.class);
        doReturn(cursor).when(db).rawQuery(anyString(), any());
        rdtTestsRepository.createIndexes(db);
        verify(db).execSQL(eq("CREATE INDEX IF NOT EXISTS base_entity_id_index ON " + RDT_TESTS + "(" + RDTTestsRepository.BASE_ENTITY_ID + ")"));
    }

    @Test
    public void testGetRDTTestDetailsByBaseEntityIdShouldFetchTestDetailsByEntityID() throws JSONException {
        assertCorrectTestDetailsAreFetched(rdtTestsRepository.getRDTTestDetailsByBaseEntityId("base-entity-id"));
    }

    @Test
    public void testGetRDTTestDetailsByRDTIdShouldFetchTestDetailsByRDTId() throws JSONException {
        List<RDTTestDetails> rdtTestDetails = new ArrayList<>();
        rdtTestDetails.add(rdtTestsRepository.getRDTTestDetailsByRDTId("rdt-id"));
        assertCorrectTestDetailsAreFetched(rdtTestDetails);
    }

    private void assertCorrectTestDetailsAreFetched(List<RDTTestDetails> actualRdtTestDetails) throws JSONException {
        RDTTestDetails actualRDTTestDetails = actualRdtTestDetails.get(0);
        RDTTestDetails expectedRdtTestDetail = getExpectedRDTTestDetails();
        assertEquals(expectedRdtTestDetail.getRdtId(), actualRDTTestDetails.getRdtId());
        assertEquals(expectedRdtTestDetail.getDate(), actualRDTTestDetails.getDate());
        assertEquals(expectedRdtTestDetail.getRdtType(), actualRDTTestDetails.getRdtType());
        assertEquals(expectedRdtTestDetail.getTestResult(), actualRDTTestDetails.getTestResult());
        assertEquals(StringUtils.join(expectedRdtTestDetail.getParasiteTypes(), ","),
                StringUtils.join(actualRDTTestDetails.getParasiteTypes(), ","));
    }

    private RDTTestDetails getExpectedRDTTestDetails() throws JSONException {
        RDTTestDetails expectedRdtTestDetail = new RDTTestDetails();
        expectedRdtTestDetail.setRdtId("rdt_id");
        expectedRdtTestDetail.setDate("test_date");
        expectedRdtTestDetail.setParasiteTypes(convertJsonArrToListOfStrings(getParasiteTypes()));
        expectedRdtTestDetail.setRdtType("rdt_type");
        expectedRdtTestDetail.setTestResult("chw_result");
        return expectedRdtTestDetail;
    }
}
