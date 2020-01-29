package io.ona.rdt.contract;

import android.support.v4.app.Fragment;

/**
 * Created by Vincent Karuri on 29/01/2020
 */
public interface PatientProfileActivityContract {

    interface View {
        void replaceFragment(Fragment fragment, boolean addToBackStack);
    }

    interface Presenter {}
}
