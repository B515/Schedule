package xyz.b515.schedule.util

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.TypedValue
import xyz.b515.schedule.R
import java.util.*

object ThemeHelper : Application.ActivityLifecycleCallbacks {
    private val activityList = ArrayList<Activity>()
    var currentTheme = 0
        private set

    fun init(application: Application, theme: Int) {
        application.registerActivityLifecycleCallbacks(this)
        this.currentTheme = theme
    }

    fun setTheme(activity: Activity, styleRes: Int) {
        currentTheme = styleRes
        PreferenceManager.getDefaultSharedPreferences(activity).edit().putInt("theme", currentTheme).apply()
        activityList.filter { it !== activity }.forEach { it.recreate() }
        val intent = Intent(activity, activity.javaClass)
        activity.startActivity(intent)
        activity.overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out)
        activity.finish()
    }

    private fun getThemePrimaryColor(context: Context): Int {
        val value = TypedValue()
        context.theme.resolveAttribute(R.attr.colorPrimary, value, true)
        return value.data
    }

    private fun setTaskDescription(activity: Activity) {
        val taskDescription = ActivityManager.TaskDescription(activity.getString(R.string.app_name),
                BitmapFactory.decodeResource(activity.resources, R.mipmap.ic_launcher),
                getThemePrimaryColor(activity))
        activity.setTaskDescription(taskDescription)
    }

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
        activityList.add(activity)
        activity.setTheme(currentTheme)
        setTaskDescription(activity)
    }

    override fun onActivityStarted(activity: Activity) = Unit
    override fun onActivityResumed(activity: Activity) = Unit
    override fun onActivityPaused(activity: Activity) = Unit
    override fun onActivityStopped(activity: Activity) = Unit
    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle?) = Unit
    override fun onActivityDestroyed(activity: Activity) = Unit
}
