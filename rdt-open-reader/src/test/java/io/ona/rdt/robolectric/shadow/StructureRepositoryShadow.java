package io.ona.rdt.robolectric.shadow;

import net.sqlcipher.database.SQLiteDatabase;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;
import org.smartregister.repository.LocationRepository;
import org.smartregister.repository.StructureRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent Karuri on 04/08/2020
 */

@Implements(StructureRepository.class)
public class StructureRepositoryShadow extends Shadow {

    private static List<Object> args = new ArrayList<>();

    @Implementation
    public static void createTable(SQLiteDatabase db) {
        args.add(db);
    }

    public static List<Object> getArgs() {
        return args;
    }
}
