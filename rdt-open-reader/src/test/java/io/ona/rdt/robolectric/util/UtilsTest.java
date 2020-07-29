package io.ona.rdt.robolectric.util;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.junit.Test;

import io.ona.rdt.robolectric.RobolectricTest;
import io.ona.rdt.util.Utils;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by Vincent Karuri on 29/07/2020
 */
public class UtilsTest extends RobolectricTest {

    @Test
    public void testRecordExceptionInCrashlyticsShouldRecordException() {
        Throwable throwable = mock(Throwable.class);
        Utils.recordExceptionInCrashlytics(throwable);
        verify(FirebaseCrashlytics.getInstance()).recordException(eq(throwable));
    }

    @Test
    public void testLogEventToCrashlyticsShouldLogEvent() {
        String message = "message";
        Utils.logEventToCrashlytics(message);
        verify(FirebaseCrashlytics.getInstance()).log(eq(message));
    }
}
