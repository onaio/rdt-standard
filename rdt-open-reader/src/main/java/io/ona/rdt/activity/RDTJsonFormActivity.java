package io.ona.rdt.activity;

import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.WindowManager;

import com.vijay.jsonwizard.activities.JsonFormActivity;
import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.utils.Utils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentManager;
import io.ona.rdt.R;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.contract.RDTJsonFormActivityContract;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.presenter.RDTJsonFormActivityPresenter;
import io.ona.rdt.util.Constants;
import io.ona.rdt.util.RDTJsonFormUtils;

import static com.vijay.jsonwizard.utils.PermissionUtils.PHONE_STATE_PERMISSION;
import static io.ona.rdt.util.Constants.RDTType.ONA_RDT;
import static io.ona.rdt.util.Utils.updateLocale;

public class RDTJsonFormActivity extends JsonFormActivity implements RDTJsonFormActivityContract.View {

    protected RDTJsonFormUtils formUtils;
    private String rdtType = ONA_RDT;
    private RDTJsonFormActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.hideProgressDialog();
        updateLocale(this);
        super.onCreate(savedInstanceState);
        formUtils = getFormUtils();
        presenter = new RDTJsonFormActivityPresenter(this);
        modifyActionBarAppearance();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private RDTJsonFormUtils getFormUtils() {
        if (formUtils == null) {
            formUtils = initializeFormUtils();
        }
        return formUtils;
    }

    protected RDTJsonFormUtils initializeFormUtils() {
        return new RDTJsonFormUtils();
    }

    private void modifyActionBarAppearance() {
        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.form_fragment_toolbar_color));
        actionBar.setBackgroundDrawable(colorDrawable);
    }

    @Override
    public void initializeFormFragment() {
        getSupportFragmentManager().beginTransaction().add(com.vijay.jsonwizard.R.id.container, getFirstStep()).commitAllowingStateLoss();
    }

    protected JsonFormFragment getFirstStep() {
        return RDTJsonFormFragment.getFormFragment(JsonFormConstants.FIRST_STEP_NAME);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       if (requestCode == PHONE_STATE_PERMISSION && grantResults.length > 0
               && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            setResult(Constants.RESULT_CODE.PERMISSIONS_DENIED, null);
            formUtils.showToast(this, getString(R.string.phone_state_permissions_required));
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
        setPreviousPressed(true);
        getSupportFragmentManager().popBackStack();
    }

    public String getRdtType() {
        return rdtType;
    }

    public void setRdtType(String rdtType) {
        this.rdtType = rdtType;
    }

    @Override
    public void onStop() {
        RDTApplication.getInstance().getStepStateConfiguration().destroyInstance();
        super.onStop();
    }
}
