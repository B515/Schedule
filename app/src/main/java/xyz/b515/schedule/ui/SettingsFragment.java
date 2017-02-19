package xyz.b515.schedule.ui;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import xyz.b515.schedule.BuildConfig;
import xyz.b515.schedule.R;

/**
 * Created by ZeroGo on 2017.2.19.
 */

public class SettingsFragment extends PreferenceFragment {
    EditTextPreference userNamePreference;
    EditTextPreference psdNamePreference;
    Preference versionPreference;
    private static Preference.OnPreferenceChangeListener onPreferenceChangeListener = (preference, newValue) -> {
        String value = newValue.toString();
        preference.setSummary(value);
        if (preference.getKey().equals("password")) {
            preference.setSummary("********");
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

        versionPreference.setSummary(BuildConfig.VERSION_NAME);

        bindPreferenceSummaryToValue(userNamePreference);
        bindPreferenceSummaryToValue(psdNamePreference);
    }

    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(onPreferenceChangeListener);
        onPreferenceChangeListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }
}