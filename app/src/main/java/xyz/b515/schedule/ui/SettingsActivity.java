package xyz.b515.schedule.ui;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.b515.schedule.BuildConfig;
import xyz.b515.schedule.R;
import xyz.b515.schedule.util.PackageHelper;

public class SettingsActivity extends AppCompatActivity {
    @BindView(R.id.settings_toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pref);
        getFragmentManager().beginTransaction().replace(R.id.content, new SettingsFragment()).commit();
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    public static class SettingsFragment extends PreferenceFragment {
        EditTextPreference userNamePreference;
        EditTextPreference psdNamePreference;
        Preference versionPreference;
        SwitchPreference splashScreenPreference;
        private static Preference.OnPreferenceChangeListener onPreferenceChangeListener = (preference, newValue) -> {
            String value = newValue.toString();
            if (preference.getKey().equals("password")) {
                preference.setSummary("********");
            } else if (value.isEmpty()) {
                preference.setSummary("Nothing");
            } else {
                preference.setSummary(value);
            }
            return true;
        };

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_settings);

            userNamePreference = (EditTextPreference) findPreference("user");
            psdNamePreference = (EditTextPreference) findPreference("password");
            versionPreference = findPreference("version");
            splashScreenPreference = (SwitchPreference) findPreference("splash_screen");

            versionPreference.setSummary(BuildConfig.VERSION_NAME);

            bindPreferenceSummaryToValue(userNamePreference);
            bindPreferenceSummaryToValue(psdNamePreference);
            splashScreenPreference.setOnPreferenceChangeListener((preference, boo) -> {
                PackageHelper.changeMain(this.getContext(), (boolean) boo);
                return true;
            });
        }

        private static void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(onPreferenceChangeListener);
            onPreferenceChangeListener.onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getString(preference.getKey(), ""));
        }
    }
}