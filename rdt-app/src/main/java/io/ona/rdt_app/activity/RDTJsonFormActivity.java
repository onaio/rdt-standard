package io.ona.rdt_app.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;

import com.vijay.jsonwizard.activities.JsonFormActivity;
import com.vijay.jsonwizard.constants.JsonFormConstants;

import io.ona.rdt_app.R;
import io.ona.rdt_app.contract.RDTJsonFormActivityContract;
import io.ona.rdt_app.fragment.RDTJsonFormFragment;
import io.ona.rdt_app.presenter.RDTJsonFormActivityPresenter;
import io.ona.rdt_app.presenter.RDTJsonFormFragmentPresenter;
import io.ona.rdt_app.util.RDTJsonFormUtils;

import static com.vijay.jsonwizard.utils.PermissionUtils.PHONE_STATE_PERMISSION;
import static io.ona.rdt_app.util.Constants.ONA_RDT;

public class RDTJsonFormActivity extends JsonFormActivity implements RDTJsonFormActivityContract.View {

    private RDTJsonFormUtils formUtils;
    private String rdtType = ONA_RDT;
    private RDTJsonFormActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        formUtils = new RDTJsonFormUtils();
        presenter = new RDTJsonFormActivityPresenter(this);
        modifyActionBarAppearance();
    }

    private void modifyActionBarAppearance() {
        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.form_fragment_toolbar_color));
        actionBar.setBackgroundDrawable(colorDrawable);
    }

    @Override
    public void initializeFormFragment() {
        RDTJsonFormFragment jsonFormFragment = (RDTJsonFormFragment) RDTJsonFormFragment.getFormFragment(JsonFormConstants.FIRST_STEP_NAME);
        getSupportFragmentManager().beginTransaction().add(com.vijay.jsonwizard.R.id.container, jsonFormFragment).commitAllowingStateLoss();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
       if (requestCode == PHONE_STATE_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            Intent resultIntent = new Intent();
            setResult(RESULT_CANCELED, resultIntent);
            formUtils.showToast(this, getApplicationContext().getString(R.string.phone_state_permissions_required));
            finish();
       }
       super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void showPermissionDeniedDialog() {
        // do nothing
    }

    @Override
    public void onBackPressed() {
        presenter.onBackPress();
    }

    @Override
    public void onBackPress() {
        getSupportFragmentManager().popBackStack();
    }

    public String getRdtType() {
        return rdtType;
    }

    public void setRdtType(String rdtType) {
        this.rdtType = rdtType;
    }
}
