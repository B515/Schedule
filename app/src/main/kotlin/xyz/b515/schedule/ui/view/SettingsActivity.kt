package xyz.b515.schedule.ui.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.*
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_pref.*
import xyz.b515.schedule.BuildConfig
import xyz.b515.schedule.R
import xyz.b515.schedule.util.PackageHelper

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pref)
        fragmentManager.beginTransaction().replace(R.id.content, SettingsFragment()).commit()
        setSupportActionBar(settings_toolbar)
        settings_toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    class SettingsFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.pref_settings)

            val userNamePreference = findPreference("user") as EditTextPreference
            val psdNamePreference = findPreference("password") as EditTextPreference
            val versionPreference = findPreference("version")
            val splashScreenPreference = findPreference("splash_screen") as SwitchPreference
            val themePreference = findPreference("theme")
            val sourcePreference = findPreference("source")

            versionPreference.summary = BuildConfig.VERSION_NAME

            bindPreferenceSummaryToValue(userNamePreference)
            bindPreferenceSummaryToValue(psdNamePreference)
            splashScreenPreference.setOnPreferenceChangeListener { _, boo ->
                PackageHelper.changeMain(context, boo as Boolean)
                true
            }
            themePreference.setOnPreferenceClickListener {
                context.startActivity(Intent(context, ThemeActivity::class.java))
                true
            }
            sourcePreference.setOnPreferenceClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(resources.getString(R.string.source_code_url))))
                true
            }
        }

        companion object {
            private val onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
                val value = newValue.toString()
                when {
                    value.isEmpty() -> preference.setSummary(R.string.settings_empty)
                    preference.key == "password" -> preference.setSummary(R.string.settings_password_mask)
                    else -> preference.summary = value
                }
                true
            }

            private fun bindPreferenceSummaryToValue(preference: Preference) {
                preference.onPreferenceChangeListener = onPreferenceChangeListener
                onPreferenceChangeListener.onPreferenceChange(preference,
                        PreferenceManager
                                .getDefaultSharedPreferences(preference.context)
                                .getString(preference.key, ""))
            }
        }
    }
}