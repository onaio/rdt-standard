package io.ona.rdt.util;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import timber.log.Timber;

import static io.ona.rdt.util.Utils.logEventToCrashlytics;
import static io.ona.rdt.util.Utils.recordExceptionInCrashlytics;

/**
 * Created by Vincent Karuri on 29/05/2020
 */
public class ReleaseTree extends Timber.Tree {

    @Override
    protected void log(int priority, @Nullable String tag, @NotNull String message, @Nullable Throwable t) {
        Log.println(priority, tag, message);

        if (priority < Log.INFO) { return; }

        logEventToCrashlytics(message);

        if (t != null) { recordExceptionInCrashlytics(t); }
    }
}
