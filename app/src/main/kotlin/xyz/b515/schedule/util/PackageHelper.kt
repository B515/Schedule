package xyz.b515.schedule.util

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager

object PackageHelper {
    fun changeMain(context: Context, splash: Boolean) {
        val splashActivity = ComponentName(context, "xyz.b515.schedule.ui.view.SplashActivity")
        val mainActivity = ComponentName(context, "xyz.b515.schedule.ui.view.MainActivityAlias")

        val manager = context.packageManager
        if (splash) {
            manager.setComponentEnabledSetting(mainActivity,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP)
            manager.setComponentEnabledSetting(splashActivity,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP)
        } else {
            manager.setComponentEnabledSetting(splashActivity,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP)
            manager.setComponentEnabledSetting(mainActivity,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP)
        }
    }
}
