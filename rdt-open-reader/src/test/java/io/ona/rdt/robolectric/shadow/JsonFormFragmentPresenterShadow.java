package io.ona.rdt.robolectric.shadow;

import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interactors.JsonFormInteractor;
import com.vijay.jsonwizard.presenters.JsonFormFragmentPresenter;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;

/**
 * Created by Vincent Karuri on 24/07/2020
 */

@Implements(JsonFormFragmentPresenter.class)
public class JsonFormFragmentPresenterShadow extends Shadow {

    @Implementation
    public void __constructor__(JsonFormFragment formFragment, JsonFormInteractor jsonFormInteractor) {
    }

    @Implementation
    public void __constructor__(JsonFormFragment formFragment) {
    }
}
