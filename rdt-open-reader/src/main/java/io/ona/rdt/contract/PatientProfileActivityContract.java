package io.ona.rdt.contract;

import androidx.fragment.app.Fragment;

import io.ona.rdt.callback.OnFormSavedCallback;

/**
 * Created by Vincent Karuri on 29/01/2020
 */
public interface PatientProfileActivityContract {

    interface View {
        void replaceFragment(Fragment fragment, boolean addToBackStack);
    }

    interface Presenter {
        void saveForm(String jsonForm, OnFormSavedCallback callback);
    }
}
