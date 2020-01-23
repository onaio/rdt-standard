package io.ona.rdt.repository;

import android.support.annotation.Nullable;

import net.sqlcipher.Cursor;

import org.smartregister.repository.BaseRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.ona.rdt.domain.RDTTestDetails;
import timber.log.Timber;

/**
 * Created by Vincent Karuri on 23/01/2020
 */
public class RDTTestsRepository extends BaseRepository {

    private static final String RDT_ID = "rdt_id";
    private static final String RDT_TYPE = "rdt_type";
    private static final String TEST_DATE = "time_form_closed";
    private static final String TEST_RESULTS = "parasite_type";
    private static final String BASE_ENTITY_ID = "patient_id";


    protected static final String COLUMNS = String.format("%s, %s, %s, %s, %s", BASE_ENTITY_ID, RDT_ID, RDT_TYPE, TEST_RESULTS, TEST_DATE);
    protected static final String RDT_TESTS_TABLES = "rdt_tests";

    @Nullable
    public RDTTestDetails getRDTTestDetailsByRDTId(String rdtId) {
        Cursor cursor = null;
        RDTTestDetails rdtTestDetail = null;
        try {
            cursor = getReadableDatabase().rawQuery("SELECT " + COLUMNS + " FROM " + RDT_TESTS_TABLES +
                    " WHERE " + RDT_ID + " =?", new String[]{rdtId});
            List<RDTTestDetails> rdtTestDetails = readCursor(cursor);
            rdtTestDetail = rdtTestDetails.isEmpty() ?  rdtTestDetail : rdtTestDetails.get(0);
        } catch (Exception e) {
            Timber.e(e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return rdtTestDetail;
    }

    @Nullable
    public List<RDTTestDetails> getRDTTestDetailsByBaseEntityId(String baseEntityId) {
        Cursor cursor = null;
        List<RDTTestDetails> rdtTestDetails = new ArrayList<>();
        try {
            cursor = getReadableDatabase().rawQuery("SELECT " + COLUMNS + " FROM " + RDT_TESTS_TABLES +
                    " WHERE " + BASE_ENTITY_ID + " =?", new String[]{baseEntityId});

            rdtTestDetails = readCursor(cursor);
        } catch (Exception e) {
            Timber.e(e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return rdtTestDetails;
    }

    private List<RDTTestDetails> readCursor(Cursor cursor) {
        List<RDTTestDetails> rdtTestDetails = new ArrayList<>();
        while (cursor.moveToNext()) {
            RDTTestDetails rdtTestDetail = new RDTTestDetails();
            rdtTestDetail.setRdtId(cursor.getString(cursor.getColumnIndex(RDT_ID)));
            rdtTestDetail.setDate(cursor.getString(cursor.getColumnIndex(TEST_DATE)));
            rdtTestDetail.setTestResult(Arrays.asList(cursor.getString(cursor.getColumnIndex(TEST_RESULTS))));
            rdtTestDetail.setRdtType(cursor.getString(cursor.getColumnIndex(RDT_TYPE)));
            rdtTestDetails.add(rdtTestDetail);
        }
        return rdtTestDetails;
    }
}
