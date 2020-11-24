package io.ona.rdt.repository;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.smartregister.repository.BaseRepository;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import io.ona.rdt.domain.RDTTestDetails;
import timber.log.Timber;

import static io.ona.rdt.util.Constants.Table.RDT_TESTS;
import static io.ona.rdt.util.Utils.convertJsonArrToListOfStrings;
import static io.ona.rdt.util.Utils.tableExists;

/**
 * Created by Vincent Karuri on 23/01/2020
 */
public class RDTTestsRepository extends BaseRepository {

    private static final String RDT_ID = "rdt_id";
    private static final String RDT_TYPE = "rdt_type";
    public static final String TEST_DATE = "time_form_closed";
    public static final String PARASITE_TYPE = "parasite_type";
    public static final String BASE_ENTITY_ID = "patient_id";
    public static final String CHW_RESULT = "chw_result";

    public static final String COLUMNS = String.format("%s, %s, %s, %s, %s, %s", BASE_ENTITY_ID, RDT_ID, RDT_TYPE, CHW_RESULT, PARASITE_TYPE, TEST_DATE);
    public static final String RDT_TESTS_TABLES = "rdt_tests";

    public static void createIndexes(SQLiteDatabase db) {
        if (tableExists(db, RDT_TESTS)) {
            db.execSQL("CREATE INDEX IF NOT EXISTS base_entity_id_index ON " + RDT_TESTS + "(" + BASE_ENTITY_ID + ")");
        }
    }

    @Nullable
    public RDTTestDetails getRDTTestDetailsByRDTId(String rdtId) {

        Cursor cursor = getReadableDatabase().rawQuery("SELECT " + COLUMNS + " FROM " + RDT_TESTS_TABLES +
                " WHERE " + RDT_ID + " =?", new String[]{rdtId});

        List<RDTTestDetails> rdtTestDetails = readCursor(cursor);
        if (cursor != null) {
            cursor.close();
        }

        return rdtTestDetails.isEmpty() ?  null : rdtTestDetails.get(0);
    }

    @Nullable
    public List<RDTTestDetails> getRDTTestDetailsByBaseEntityId(String baseEntityId) {

        Cursor cursor = getReadableDatabase().rawQuery("SELECT " + COLUMNS + " FROM " + RDT_TESTS_TABLES +
                " WHERE " + BASE_ENTITY_ID + " =?", new String[]{baseEntityId});

        List<RDTTestDetails> rdtTestDetails = readCursor(cursor);
        if (cursor != null) {
            cursor.close();
        }
        return rdtTestDetails;
    }

    private List<RDTTestDetails> readCursor(Cursor cursor) {
        List<RDTTestDetails> rdtTestDetails = new ArrayList<>();
        while (cursor.moveToNext()) {
            RDTTestDetails rdtTestDetail = new RDTTestDetails();
            rdtTestDetail.setRdtId(cursor.getString(cursor.getColumnIndex(RDT_ID)));
            rdtTestDetail.setDate(cursor.getString(cursor.getColumnIndex(TEST_DATE)));
            setRDTTestResults(cursor, rdtTestDetail);
            rdtTestDetail.setRdtType(cursor.getString(cursor.getColumnIndex(RDT_TYPE)));
            rdtTestDetails.add(rdtTestDetail);
        }
        return rdtTestDetails;
    }

    private void setRDTTestResults(Cursor cursor, RDTTestDetails rdtTestDetails) {
        try {
            rdtTestDetails.setTestResult(cursor.getString(cursor.getColumnIndex(CHW_RESULT)));
            String parasites = cursor.getString(cursor.getColumnIndex(PARASITE_TYPE));
            List<String> parasiteTypes = parasites == null ? null
                    : convertJsonArrToListOfStrings(new JSONArray(parasites));
            rdtTestDetails.setParasiteTypes(parasiteTypes);
        } catch (JSONException e) {
            Timber.e(e);
        }
    }
}
