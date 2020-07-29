package io.ona.rdt.robolectric.shadow;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import org.json.JSONArray;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;

import static io.ona.rdt.repository.ParasiteProfileRepository.EXPERIMENT_DATE;
import static io.ona.rdt.repository.ParasiteProfileRepository.EXPERIMENT_TYPE;
import static io.ona.rdt.repository.ParasiteProfileRepository.PF_GAMETO;
import static io.ona.rdt.repository.ParasiteProfileRepository.P_FALCIPARUM;
import static io.ona.rdt.repository.ParasiteProfileRepository.P_MALARIAE;
import static io.ona.rdt.repository.ParasiteProfileRepository.P_OVALE;
import static io.ona.rdt.repository.ParasiteProfileRepository.P_VIVAX;
import static io.ona.rdt.repository.RDTTestsRepository.BASE_ENTITY_ID;
import static io.ona.rdt.repository.RDTTestsRepository.CHW_RESULT;
import static io.ona.rdt.repository.RDTTestsRepository.COLUMNS;
import static io.ona.rdt.repository.RDTTestsRepository.PARASITE_TYPE;
import static io.ona.rdt.repository.RDTTestsRepository.RDT_TESTS_TABLES;
import static io.ona.rdt.repository.RDTTestsRepository.TEST_DATE;
import static io.ona.rdt.util.Constants.FormFields.RDT_ID;
import static io.ona.rdt.util.Constants.RDTType.RDT_TYPE;
import static io.ona.rdt.util.Constants.Table.MICROSCOPY_RESULTS;
import static io.ona.rdt.util.Constants.Table.PCR_RESULTS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Vincent Karuri on 28/07/2020
 */

@Implements(SQLiteOpenHelper.class)
public class SQLiteOpenHelperShadow extends Shadow {

    @Implementation
    public synchronized SQLiteDatabase getReadableDatabase(String password) {
       return getDb();
    }

    @Implementation
    public synchronized SQLiteDatabase getWritableDatabase(String password) {
        return getDb();
    }

    private SQLiteDatabase getDb() {
        SQLiteDatabase db =  mock(SQLiteDatabase.class);

        doReturn(getRDTTestsCursor()).when(db).rawQuery(eq("SELECT " + COLUMNS + " FROM " + RDT_TESTS_TABLES +
                " WHERE " + BASE_ENTITY_ID + " =?"), any(String[].class));

        doReturn(getRDTTestsCursor()).when(db).rawQuery(eq("SELECT " + COLUMNS + " FROM " + RDT_TESTS_TABLES +
                " WHERE " + RDT_ID + " =?"), any(String[].class));

        doReturn(getParasiteProfileCursor()).when(db).rawQuery(eq("SELECT *" + " FROM " + MICROSCOPY_RESULTS +
                " WHERE " + RDT_ID + " =?" + " ORDER BY experiment_date"), any(String[].class));

        doReturn(getParasiteProfileCursor()).when(db).rawQuery(eq("SELECT *" + " FROM " + PCR_RESULTS +
                " WHERE " + RDT_ID + "=?" + " AND " + EXPERIMENT_TYPE +  "=?" + " ORDER BY experiment_date"),
                any(String[].class));

        return db;
    }

    private Cursor getRDTTestsCursor() {
        Cursor cursor = mock(Cursor.class);
        doReturn(0).when(cursor).getColumnIndex(eq(RDT_ID));
        doReturn(1).when(cursor).getColumnIndex(eq(TEST_DATE));
        doReturn(2).when(cursor).getColumnIndex(eq(RDT_TYPE));
        doReturn(3).when(cursor).getColumnIndex(eq(CHW_RESULT));
        doReturn(4).when(cursor).getColumnIndex(eq(PARASITE_TYPE));
        doReturn("rdt_id").when(cursor).getString(eq(0));
        doReturn("test_date").when(cursor).getString(eq(1));
        doReturn("rdt_type").when(cursor).getString(eq(2));
        doReturn("chw_result").when(cursor).getString(eq(3));


        doReturn(getParasiteTypes().toString()).when(cursor).getString(eq(4));

        when(cursor.moveToNext()).thenReturn(true).thenReturn(false);

        return cursor;
    }

    private Cursor getParasiteProfileCursor() {
        Cursor cursor = mock(Cursor.class);
        doReturn(0).when(cursor).getColumnIndex(eq(RDT_ID));
        doReturn(1).when(cursor).getColumnIndex(eq(EXPERIMENT_DATE));
        doReturn(2).when(cursor).getColumnIndex(eq(P_FALCIPARUM));
        doReturn(3).when(cursor).getColumnIndex(eq(P_MALARIAE));
        doReturn(4).when(cursor).getColumnIndex(eq(P_OVALE));
        doReturn(5).when(cursor).getColumnIndex(eq(P_VIVAX));
        doReturn(6).when(cursor).getColumnIndex(eq(PF_GAMETO));

        doReturn("rdt_id").when(cursor).getString(eq(0));
        doReturn("2019-07-09T00:00:00.000+03:00").when(cursor).getString(eq(1));
        doReturn("negative").when(cursor).getString(eq(2));
        doReturn("positive").when(cursor).getString(eq(3));
        doReturn("negative").when(cursor).getString(eq(4));
        doReturn("positive").when(cursor).getString(eq(5));
        doReturn("negative").when(cursor).getString(eq(6));

        when(cursor.moveToNext()).thenReturn(true).thenReturn(false);
        return cursor;
    }

    public static JSONArray getParasiteTypes() {
        JSONArray parasiteTypes = new JSONArray();
        parasiteTypes.put("ovale");
        parasiteTypes.put("gameto");
        return parasiteTypes;
    }
}
