package io.ona.rdt_app.util;

import java.util.ArrayList;

/**
 * Created by Vincent Karuri on 16/07/2019
 */
public class Utils {
    public static final ArrayList<String> ALLOWED_LEVELS;
    public static final String DEFAULT_LOCATION_LEVEL = Constants.Tags.HEALTH_CENTER;

    static {
        ALLOWED_LEVELS = new ArrayList<>();
        ALLOWED_LEVELS.add(DEFAULT_LOCATION_LEVEL);
        ALLOWED_LEVELS.add(Constants.Tags.COUNTRY);
        ALLOWED_LEVELS.add(Constants.Tags.PROVINCE);
        ALLOWED_LEVELS.add(Constants.Tags.DISTRICT);
        ALLOWED_LEVELS.add(Constants.Tags.VILLAGE);
    }
}
