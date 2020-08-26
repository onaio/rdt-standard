package io.ona.rdt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;

import org.smartregister.util.LangUtils;
import org.smartregister.view.fragment.BaseRegisterFragment;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.ona.rdt.R;
import io.ona.rdt.fragment.CovidPatientRegisterFragment;
import io.ona.rdt.presenter.CovidPatientRegisterActivityPresenter;
import io.ona.rdt.presenter.PatientRegisterActivityPresenter;
import io.ona.rdt.util.CovidRDTJsonFormUtils;
import io.ona.rdt.util.RDTJsonFormUtils;

import static io.ona.rdt.util.CovidConstants.Form.SAMPLE_DELIVERY_DETAILS_FORM;

/**
 * Created by Vincent Karuri on 16/06/2020
 */
public class CovidPatientRegisterActivity extends PatientRegisterActivity {

    private static final String LOCALE_IN = "in";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerLanguageSwitcher();
    }

    @Override
    public BaseRegisterFragment getRegisterFragment() {
        return new CovidPatientRegisterFragment();
    }

    @Override
    protected RDTJsonFormUtils initializeFormUtils() {
        return new CovidRDTJsonFormUtils();
    }

    @Override
    public boolean selectDrawerItem(MenuItem menuItem) {
        if (super.selectDrawerItem(menuItem)) {
            return true;
        }
        switch(menuItem.getItemId()) {
            case R.id.menu_item_create_shipment:
                getPresenter().launchForm(this, SAMPLE_DELIVERY_DETAILS_FORM, null);
                return true;

            case R.id.menu_item_switch_language:
                languageSwitcherDialog();
                return true;
        }
        return false;
    }

    @Override
    protected Class getLoginPage() {
        return CovidLoginActivity.class;
    }

    protected PatientRegisterActivityPresenter createPatientRegisterActivityPresenter() {
        return new CovidPatientRegisterActivityPresenter(this);
    }

    private Map<String, Locale> locales = new HashMap<>();
    private String[] languages;
    private int currentLanguageIndex = 0;

    private void languageSwitcherDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.drawer_menu_item_change_language));
        builder.setSingleChoiceItems(languages, currentLanguageIndex, (dialog, position) -> {
            String selectedLanguage = languages[position];
            saveLanguage(selectedLanguage);
            reloadClass();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void saveLanguage(String selectedLanguage) {
        Locale selectedLanguageLocale = locales.get(selectedLanguage);
        if (selectedLanguageLocale != null) {
            LangUtils.saveLanguage(getApplication(), selectedLanguageLocale.getLanguage());
        }
    }

    private void registerLanguageSwitcher() {
        addLanguages();

        languages = new String[locales.size()];
        Locale current = getResources().getConfiguration().locale;
        int x = 0;
        for (Map.Entry<String, Locale> language : locales.entrySet()) {
            languages[x] = language.getKey(); //Update the languages strings array with the languages to be displayed on the alert dialog

            if (current.getLanguage().equals(language.getValue().getLanguage())) {
                currentLanguageIndex = x;
            }
            x++;
        }
    }

    private void addLanguages() {
        locales.put(getString(R.string.lang_english), Locale.ENGLISH);
        locales.put(getString(R.string.lang_indonesia), new Locale(LOCALE_IN));
    }

    private void reloadClass() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
