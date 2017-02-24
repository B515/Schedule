package xyz.b515.schedule;

import android.app.Application;
import android.preference.PreferenceManager;

import xyz.b515.schedule.util.ThemeHelper;

public class ScheduleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ThemeHelper.getInstance().init(this, PreferenceManager.getDefaultSharedPreferences(this).getInt("theme", 0));
    }

}
