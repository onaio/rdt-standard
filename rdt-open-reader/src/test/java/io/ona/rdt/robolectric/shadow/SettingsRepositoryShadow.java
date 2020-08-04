package io.ona.rdt.robolectric.shadow;

import net.sqlcipher.database.SQLiteDatabase;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;
import org.smartregister.repository.SettingsRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent Karuri on 04/08/2020
 */

@Implements(SettingsRepository.class)
public class SettingsRepositoryShadow extends Shadow {

    private static List<Object> args = new ArrayList<>();

    @Implementation
    public static void onUpgrade(SQLiteDatabase db) {
        args.add(db);
    }

    public static List<Object> getArgs() {
        return args;
    }
}
