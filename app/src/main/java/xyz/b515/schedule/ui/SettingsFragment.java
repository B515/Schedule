package xyz.b515.schedule.ui;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_settings);

        userNamePreference = (EditTextPreference) findPreference("settings_user");
        psdNamePreference = (EditTextPreference) findPreference("settings_password");
        versionPreference = findPreference("settings_version");

        versionPreference.setSummary(BuildConfig.VERSION_NAME);


    }

}