package io.ona.rdt.contract;

/**
 * Created by Vincent Karuri on 28/08/2020
 */
public interface CovidPatientVisitFragmentContract {

    interface View {
        String translateString(int resourceId);
    }

    interface Presenter {
        String translateString(int resourceId);
    }
}
