package io.ona.rdt.robolectric.shadow;

import android.content.Context;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;

/**
 * Created by Vincent Karuri on 28/07/2020
 */

@Implements(SQLiteDatabase.class)
public class SQLiteDatabaseShadow extends Shadow {

    @Implementation
    public static synchronized void loadLibs (Context context) {

    }

    @Implementation
    protected Cursor rawQueryWithFactory(SQLiteDatabase.CursorFactory cursorFactory, String sql, String[] selectionArgs, String editTable) {
        return null;
    }
}

