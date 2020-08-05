package io.ona.rdt.robolectric.repository;

import net.sqlcipher.database.SQLiteDatabase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;
import org.smartregister.Context;
import org.smartregister.repository.DrishtiRepository;
import org.smartregister.repository.EventClientRepository;

import java.util.List;

import io.ona.rdt.repository.RDTRepository;
import io.ona.rdt.robolectric.RobolectricTest;
import io.ona.rdt.robolectric.shadow.EventClientRepositoryShadow;
import io.ona.rdt.robolectric.shadow.ParasiteProfileRepositoryShadow;
import io.ona.rdt.robolectric.shadow.RDTTestsRepositoryShadow;
import io.ona.rdt.robolectric.shadow.SettingsRepositoryShadow;
import io.ona.rdt.robolectric.shadow.UniqueIdRepositoryShadow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;

/**
 * Created by Vincent Karuri on 04/08/2020
 */

@Config(shadows = {EventClientRepositoryShadow.class, UniqueIdRepositoryShadow.class,
        SettingsRepositoryShadow.class, RDTTestsRepositoryShadow.class,
        ParasiteProfileRepositoryShadow.class})
public class RDTRepositoryTest extends RobolectricTest {

    private RDTRepository rdtRepository;

    @Mock
    private SQLiteDatabase db;
    @Mock
    private android.content.Context androidContext;
    @Mock
    private Context openSRPContext;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        doReturn(new DrishtiRepository[0]).when(openSRPContext).sharedRepositoriesArray();
        rdtRepository = new RDTRepository(androidContext, openSRPContext);
    }

    @Test
    public void testOnCreateShouldInitializeAllRepositories() {
        rdtRepository.onCreate(db);
        // set up client table
        List<Object> eventClientRepositoryCreateTableArgs = EventClientRepositoryShadow.getArgs();
        assertEquals(db, eventClientRepositoryCreateTableArgs.get(0));
        assertEquals(EventClientRepository.Table.client, eventClientRepositoryCreateTableArgs.get(1));
        assertTrue(eventClientRepositoryCreateTableArgs.get(2) instanceof EventClientRepository.client_column[]);
        // set up event table
        assertEquals(db, eventClientRepositoryCreateTableArgs.get(3));
        assertEquals(EventClientRepository.Table.event, eventClientRepositoryCreateTableArgs.get(4));
        assertTrue(eventClientRepositoryCreateTableArgs.get(5) instanceof EventClientRepository.event_column[]);
        // set up unique id table
        assertEquals(db, UniqueIdRepositoryShadow.getArgs().get(0));
        // set up settings table
        assertEquals(db, SettingsRepositoryShadow.getArgs().get(0));
        // set up rdt tests repository
        assertEquals(db, RDTTestsRepositoryShadow.getArgs().get(0));
        // set up parasite profile repository
        assertEquals(db, ParasiteProfileRepositoryShadow.getArgs().get(0));
    }

    @Test
    public void testOnUpgradeShouldPerformDBUpgrade() {
        rdtRepository.onUpgrade(db, 0, 1);
    }
}
