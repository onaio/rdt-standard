package io.ona.rdt.robolectric.shadow;

import net.sqlcipher.database.SQLiteDatabase;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;

import java.util.ArrayList;
import java.util.List;

import io.ona.rdt.repository.ParasiteProfileRepository;

/**
 * Created by Vincent Karuri on 04/08/2020
 */

@Implements(ParasiteProfileRepository.class)
public class ParasiteProfileRepositoryShadow extends Shadow {

    private static List<Object> args = new ArrayList<>();

    @Implementation
    public static void createIndexes(SQLiteDatabase db) {
        args.add(db);
    }

    public static List<Object> getArgs() {
        return args;
    }
}
