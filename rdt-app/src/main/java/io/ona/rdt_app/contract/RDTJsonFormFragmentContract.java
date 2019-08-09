package io.ona.rdt_app.contract;

import org.json.JSONException;

/**
 * Created by Vincent Karuri on 05/08/2019
 */
public interface RDTJsonFormFragmentContract {

    interface View {

    }

    interface Presenter{
        void saveForm() throws JSONException;
    }
}
