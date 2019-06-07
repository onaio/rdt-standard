package io.ona.rdt_app.repository;

import android.content.Context;
import net.sqlcipher.database.SQLiteDatabase;
import android.util.Log;

import org.smartregister.AllConstants;
import org.smartregister.BuildConfig;
import org.smartregister.repository.Repository;

/**
 * Created by Vincent Karuri on 07/06/2019
 */
public class RDTRepository extends Repository {

    private static final String TAG = RDTRepository.class.getCanonicalName();
    protected SQLiteDatabase readableDatabase;
    protected SQLiteDatabase writableDatabase;


    public RDTRepository(Context context, org.smartregister.Context openSRPContext) {
        super(context, AllConstants.DATABASE_NAME, 1, openSRPContext.session(), null, openSRPContext.sharedRepositoriesArray());
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

    }

    @Override
    public synchronized SQLiteDatabase getReadableDatabase() {
        try {
            if (readableDatabase == null || !readableDatabase.isOpen()) {
                readableDatabase = super.getReadableDatabase();
            }
            return readableDatabase;
        } catch (Exception e) {
            Log.e(TAG, "Database Error. " + e.getMessage());
            return null;
        }
    }

    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
        if (writableDatabase == null || !writableDatabase.isOpen()) {
            writableDatabase = super.getWritableDatabase();
        }
        return writableDatabase;
    }
}
