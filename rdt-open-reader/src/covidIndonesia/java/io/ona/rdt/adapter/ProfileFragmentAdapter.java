package io.ona.rdt.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import io.ona.rdt.activity.CovidPatientProfileActivity;
import io.ona.rdt.fragment.CovidPatientProfileFragment;
import io.ona.rdt.fragment.CovidPatientVisitFragment;

/**
 * Created by Vincent Karuri on 25/08/2020
 */
public class ProfileFragmentAdapter extends FragmentStateAdapter {

    private FragmentActivity fragmentActivity;

    public ProfileFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        this.fragmentActivity = fragmentActivity;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = getCovidPatientProfileActivity().createPatientProfileFragment();
                break;
            case 1:
                fragment = new CovidPatientVisitFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    private CovidPatientProfileActivity getCovidPatientProfileActivity() {
        return (CovidPatientProfileActivity) fragmentActivity;
    }
}
