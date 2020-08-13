package io.ona.rdt.robolectric.shadow;

import net.sqlcipher.database.SQLiteDatabase;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;
import org.smartregister.repository.UniqueIdRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Vincent Karuri on 04/08/2020
 */

@Implements(UniqueIdRepository.class)
public class UniqueIdRepositoryShadow extends Shadow {

    private static List<Object> args = new ArrayList<>();
    public static Set<String> openmrsIds;

    @Implementation
    public static void createTable(SQLiteDatabase db) {
        args.add(db);
    }

    @Implementation
    public int close(String openmrsId) {
        openmrsIds.add(openmrsId);
        return 0;
    }

    public static void setOpenmrsIds(Set<String> openmrsIds) {
        UniqueIdRepositoryShadow.openmrsIds = openmrsIds;
    }

    public static List<Object> getArgs() {
        return args;
    }
}
