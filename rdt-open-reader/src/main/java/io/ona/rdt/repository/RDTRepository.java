package io.ona.rdt.repository;

import android.content.Context;
import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.AllConstants;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.repository.Repository;
import org.smartregister.repository.SettingsRepository;
import org.smartregister.repository.UniqueIdRepository;

import io.ona.rdt.application.RDTApplication;
import timber.log.Timber;

import static io.ona.rdt.util.Utils.isMalariaApp;

/**
 * Created by Vincent Karuri on 07/06/2019
 */
public class RDTRepository extends Repository {

    private static final String TAG = RDTRepository.class.getCanonicalName();
    protected SQLiteDatabase readableDatabase;
    protected SQLiteDatabase writableDatabase;


    public RDTRepository(Context context, org.smartregister.Context openSRPContext) {
        super(context, AllConstants.DATABASE_NAME, 1, openSRPContext.session(), RDTApplication.getInstance().getPresenter().createCommonFtsObject(), openSRPContext.sharedRepositoriesArray());
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        super.onCreate(database);
        EventClientRepository.createTable(database, EventClientRepository.Table.client, EventClientRepository.client_column.values());
        EventClientRepository.createTable(database, EventClientRepository.Table.event, EventClientRepository.event_column.values());
        UniqueIdRepository.createTable(database);
        SettingsRepository.onUpgrade(database);
        if (isMalariaApp()) {
            RDTTestsRepository.createIndexes(database);
            ParasiteProfileRepository.createIndexes(database);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Timber.w("Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");

        int upgradeTo = oldVersion + 1;
        while (upgradeTo <= newVersion) {
            switch (upgradeTo) {
                default:
                    break;
            }
            upgradeTo++;
        }
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
