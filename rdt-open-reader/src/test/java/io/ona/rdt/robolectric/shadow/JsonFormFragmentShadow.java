package io.ona.rdt.robolectric.shadow;

import android.os.Bundle;
import android.view.View;

import com.vijay.jsonwizard.fragments.JsonFormFragment;

import org.json.JSONObject;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;

import androidx.annotation.Nullable;

/**
 * Created by Vincent Karuri on 24/07/2020
 */

@Implements(JsonFormFragment.class)
public class JsonFormFragmentShadow extends Shadow {

    @Implementation
    public boolean displayScrollBars() {
        return false;
    }

    @Implementation
    public JSONObject getStep(String stepName) {
        return new JSONObject();
    }

    @Implementation
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    }
}
