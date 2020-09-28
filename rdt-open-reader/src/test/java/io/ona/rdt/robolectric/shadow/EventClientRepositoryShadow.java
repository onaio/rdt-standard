package io.ona.rdt.robolectric.shadow;

import net.sqlcipher.database.SQLiteDatabase;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;
import org.smartregister.domain.db.Column;
import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.EventClientRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent Karuri on 04/08/2020
 */
@Implements(EventClientRepository.class)
public class EventClientRepositoryShadow extends Shadow {

    private static List<Object> args = new ArrayList<>();

    @Implementation
    public static void createTable(SQLiteDatabase db, BaseRepository.BaseTable table, Column[] columns) {
        args.add(db);
        args.add(table);
        args.add(columns);
    }

    public static List<Object> getArgs() {
        return args;
    }
}
