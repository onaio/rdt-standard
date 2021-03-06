package io.ona.rdt.repository;

import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.AllConstants;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.repository.LocationRepository;
import org.smartregister.repository.PlanDefinitionRepository;
import org.smartregister.repository.PlanDefinitionSearchRepository;
import org.smartregister.repository.Repository;
import org.smartregister.repository.SettingsRepository;
import org.smartregister.repository.StructureRepository;
import org.smartregister.repository.TaskRepository;
import org.smartregister.repository.UniqueIdRepository;

import timber.log.Timber;

/**
 * Created by Vincent Karuri on 07/06/2019
 */
public class RDTRepository extends Repository {

    protected SQLiteDatabase readableDatabase;
    protected SQLiteDatabase writableDatabase;


    public RDTRepository(Context context, org.smartregister.Context openSRPContext) {
        super(context, AllConstants.DATABASE_NAME, 1, openSRPContext.session(), openSRPContext.commonFtsObject(), openSRPContext.sharedRepositoriesArray());
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        super.onCreate(database);
        EventClientRepository.createTable(database, EventClientRepository.Table.client, EventClientRepository.client_column.values());
        EventClientRepository.createTable(database, EventClientRepository.Table.event, EventClientRepository.event_column.values());
        UniqueIdRepository.createTable(database);
        SettingsRepository.onUpgrade(database);
        RDTTestsRepository.createIndexes(database);
        ParasiteProfileRepository.createIndexes(database);
        LocationRepository.createTable(database);
        TaskRepository.createTable(database);
        StructureRepository.createTable(database);
        PlanDefinitionRepository.createTable(database);
        PlanDefinitionSearchRepository.createTable(database);
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
        if (readableDatabase == null || !readableDatabase.isOpen()) {
            readableDatabase = super.getReadableDatabase();
        }
        return readableDatabase;
    }

    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
        if (writableDatabase == null || !writableDatabase.isOpen()) {
            writableDatabase = super.getWritableDatabase();
        }
        return writableDatabase;
    }
}
