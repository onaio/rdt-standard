package io.ona.rdt.robolectric.shadow;

import com.vijay.jsonwizard.utils.AllSharedPreferences;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;

import io.ona.rdt.BuildConfig;

@Implements(AllSharedPreferences.class)
public class AllSharedPreferencesShadow extends Shadow {

    @Implementation
    public String fetchLanguagePreference(){
        return BuildConfig.LOCALE;
    }
}
