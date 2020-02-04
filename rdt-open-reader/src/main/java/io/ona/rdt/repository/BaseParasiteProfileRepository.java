package io.ona.rdt.repository;

import android.support.annotation.Nullable;

import net.sqlcipher.Cursor;

import org.smartregister.repository.BaseRepository;

import io.ona.rdt.domain.ParasiteProfileResults;
import timber.log.Timber;

/**
 * Created by Vincent Karuri on 04/02/2020
 */
public class BaseParasiteProfileRepository extends BaseRepository {

    protected String tableName = "pcr_results";

    private static final String P_FALCIPARUM = "p_falciparum";
    private static final String P_VIVAX = "p_vivax";
    private static final String P_MALARIAE = "p_malariae";
    private static final String P_OVALE = "p_ovale";
    private static final String PF_GAMETO = "pf_gameto";
    private static final String EXPERIMENT_DATE = "experiment_date";
    private static final String RDT_ID = "rdt_id";

    private static final String COLUMNS = String.format("%s, %s, %s, %s, %s, %s, %s", RDT_ID, P_FALCIPARUM, P_MALARIAE, P_OVALE, P_VIVAX, PF_GAMETO, EXPERIMENT_DATE);

    @Nullable
    public ParasiteProfileResults getParasiteProfile(String rdtId) {
        Cursor cursor = null;
        ParasiteProfileResults parasiteProfileResults = null;
        try {
            cursor = getReadableDatabase().rawQuery("SELECT " + COLUMNS + " FROM " + tableName +
                    " WHERE " + RDT_ID + " =?", new String[]{rdtId});

            parasiteProfileResults = readCursor(cursor);
        } catch (Exception e) {
            Timber.e(e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return parasiteProfileResults;
    }

    private ParasiteProfileResults readCursor(Cursor cursor) {
        ParasiteProfileResults parasiteProfileResults = new ParasiteProfileResults();
        while (cursor.moveToNext()) {
            parasiteProfileResults.setRdtId(cursor.getString(cursor.getColumnIndex(RDT_ID)));
            parasiteProfileResults.setExperimentDate(cursor.getString(cursor.getColumnIndex(EXPERIMENT_DATE)));
            parasiteProfileResults.setpFalciparum(cursor.getString(cursor.getColumnIndex(P_FALCIPARUM)));
            parasiteProfileResults.setpMalariae(cursor.getString(cursor.getColumnIndex(P_MALARIAE)));
            parasiteProfileResults.setpOvale(cursor.getString(cursor.getColumnIndex(P_OVALE)));
            parasiteProfileResults.setpVivax(cursor.getString(cursor.getColumnIndex(P_VIVAX)));
            parasiteProfileResults.setPfGameto(cursor.getString(cursor.getColumnIndex(PF_GAMETO)));
        }
        return parasiteProfileResults;
    }
}
