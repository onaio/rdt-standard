package io.ona.rdt.contract;

import org.json.JSONObject;

/**
 * Created by Vincent Karuri on 16/08/2019
 */
public interface RDTJsonFormActivityContract {

    interface View {
        void onBackPress();
        JSONObject getmJSONObject();
    }

    interface Presenter {
        void onBackPress();
    }
}
