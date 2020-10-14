package io.ona.rdt.robolectric.shadow;

import net.sqlcipher.database.SQLiteDatabase;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;

import io.ona.rdt.repository.RDTRepository;

@Implements(RDTRepository.class)
public class RDTRepositoryShadow extends Shadow {

    @Implementation
    public synchronized SQLiteDatabase getWritableDatabase() {
        return new SQLiteDatabase("", null, null, -1);
    }
}
