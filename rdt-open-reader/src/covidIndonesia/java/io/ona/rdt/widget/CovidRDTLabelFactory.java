package io.ona.rdt.widget;

import io.ona.rdt.callback.CovidOnLabelClickedListener;
import io.ona.rdt.callback.OnLabelClickedListener;

/**
 * Created by Vincent Karuri on 13/07/2020
 */
public class CovidRDTLabelFactory extends RDTLabelFactory {

    @Override
    protected OnLabelClickedListener getOnLabelClickedListener() {
        return new CovidOnLabelClickedListener(widgetArgs);
    }
}
