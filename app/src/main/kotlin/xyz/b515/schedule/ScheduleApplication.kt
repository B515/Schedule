package xyz.b515.schedule

import android.app.Application
import android.preference.PreferenceManager

import xyz.b515.schedule.util.ThemeHelper

class ScheduleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ThemeHelper.init(this, PreferenceManager.getDefaultSharedPreferences(this).getInt("theme", 0))
    }

}
