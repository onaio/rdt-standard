package io.ona.rdt.repository;

import androidx.annotation.Nullable;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.repository.BaseRepository;

import java.util.ArrayList;
import java.util.List;

import io.ona.rdt.domain.ParasiteProfileResult;
import timber.log.Timber;

import static io.ona.rdt.util.Constants.Table.MICROSCOPY_RESULTS;
import static io.ona.rdt.util.Constants.Table.PCR_RESULTS;

/**
 * Created by Vincent Karuri on 04/02/2020
 */
public class ParasiteProfileRepository extends BaseRepository {

    private static final String P_FALCIPARUM = "p_falciparum";
    private static final String P_VIVAX = "p_vivax";
    private static final String P_MALARIAE = "p_malariae";
    private static final String P_OVALE = "p_ovale";
    private static final String PF_GAMETO = "pf_gameto";
    private static final String EXPERIMENT_DATE = "experiment_date";
    private static final String EXPERIMENT_TYPE = "experiment_type";
    private static final String RDT_ID = "rdt_id";


    public static void createIndexes(SQLiteDatabase db) {
        db.execSQL("CREATE INDEX IF NOT EXISTS pcr_results_rdt_id_and_experiment_type_index ON " + PCR_RESULTS + "(" + RDT_ID + "," + EXPERIMENT_TYPE  +")");
        db.execSQL("CREATE INDEX IF NOT EXISTS microscopy_results_rdt_id_index ON " + MICROSCOPY_RESULTS + "(" + RDT_ID +")");
    }

    @Nullable
    public List<ParasiteProfileResult> getParasiteProfiles(String rdtId, String tableName, String experimentType) {
        Cursor cursor = null;
        List<ParasiteProfileResult> parasiteProfileResults = null;
        try {
            if (MICROSCOPY_RESULTS.equals(tableName)) {
                cursor = getReadableDatabase().rawQuery("SELECT *" + " FROM " + tableName +
                        " WHERE " + RDT_ID + " =?" + " ORDER BY experiment_date", new String[]{rdtId});
            } else {
                cursor = getReadableDatabase().rawQuery("SELECT *" + " FROM " + tableName +
                        " WHERE " + RDT_ID + "=?" + " AND " + EXPERIMENT_TYPE +  "=?" + " ORDER BY experiment_date",
                        new String[]{rdtId, experimentType});
            }
            parasiteProfileResults = readCursor(cursor, experimentType);
        } catch (Exception e) {
            Timber.e(e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return parasiteProfileResults;
    }

    protected List<ParasiteProfileResult> readCursor(Cursor cursor, String experimentType) {
        List<ParasiteProfileResult> parasiteProfileResults = new ArrayList<>();
        while (cursor.moveToNext()) {
            ParasiteProfileResult parasiteProfileResult = new ParasiteProfileResult();
            parasiteProfileResult.setRdtId(cursor.getString(cursor.getColumnIndex(RDT_ID)));
            parasiteProfileResult.setExperimentDate(cursor.getString(cursor.getColumnIndex(EXPERIMENT_DATE)));
            parasiteProfileResult.setpFalciparum(cursor.getString(cursor.getColumnIndex(P_FALCIPARUM)));
            parasiteProfileResult.setpMalariae(cursor.getString(cursor.getColumnIndex(P_MALARIAE)));
            parasiteProfileResult.setpOvale(cursor.getString(cursor.getColumnIndex(P_OVALE)));
            parasiteProfileResult.setpVivax(cursor.getString(cursor.getColumnIndex(P_VIVAX)));
            parasiteProfileResult.setPfGameto(cursor.getString(cursor.getColumnIndex(PF_GAMETO)));
            parasiteProfileResult.setExperimentType(experimentType);
            parasiteProfileResults.add(parasiteProfileResult);
        }
        return parasiteProfileResults;
    }
}
