package xyz.b515.schedule.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

public class PackageHelper {
    public static void changeMain(Context context, boolean splash) {
        ComponentName splashActivity = new ComponentName(context, "xyz.b515.schedule.ui.SplashActivity");
        ComponentName mainActivity = new ComponentName(context, "xyz.b515.schedule.ui.MainActivityAlias");

        PackageManager manager = context.getPackageManager();
        if (splash) {
            manager.setComponentEnabledSetting(mainActivity,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
            manager.setComponentEnabledSetting(splashActivity,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        } else {
            manager.setComponentEnabledSetting(splashActivity,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
            manager.setComponentEnabledSetting(mainActivity,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        }
    }
}
