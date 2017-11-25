package xyz.b515.schedule.ui.view

import android.app.FragmentManager
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v13.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import xyz.b515.schedule.Constant
import xyz.b515.schedule.R
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title_text.setText(R.string.app_name)
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        checkCurrentWeek()

        setSupportActionBar(toolbar)

        view_pager.adapter = CourseFragmentAdapter(fragmentManager)
        tab_layout.setupWithViewPager(view_pager)

        val weeks = (1..20).map { getString(R.string.week_num).format(it) }
        val adapter = ArrayAdapter(this, R.layout.spinner_item, weeks)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setSelection(preferences.getInt(Constant.CURRENT_WEEK, 0))
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                preferences.edit().putInt(Constant.CURRENT_WEEK, position).apply()
                reload()
            }

            override fun onNothingSelected(parent: AdapterView<*>) = Unit
        }
    }

    override fun onResume() {
        super.onResume()
        reload()
    }

    private fun reload() {
        val page = view_pager.currentItem
        view_pager.adapter = CourseFragmentAdapter(fragmentManager)
        view_pager.currentItem = page
    }

    private fun checkCurrentWeek() {
        val newWeekOfYear = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)
        val oldWeek = preferences.getInt(Constant.CURRENT_WEEK, -1)
        val oldWeekOfYear = preferences.getInt(Constant.CURRENT_WEEK_OF_YEAR, newWeekOfYear - 1)
        if (newWeekOfYear > oldWeekOfYear) {
            val newWeek = oldWeek + newWeekOfYear - oldWeekOfYear
            preferences.edit().putInt(Constant.CURRENT_WEEK, if (newWeek < 20) newWeek else 0)
                    .putInt(Constant.CURRENT_WEEK_OF_YEAR, newWeekOfYear)
                    .apply()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_manage -> {
                val intent = Intent(this, CourseManageActivity::class.java)
                startActivity(intent)
            }
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.action_capture -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private inner class CourseFragmentAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
        override fun getItem(position: Int) = when (position) {
            0 -> TodayCoursesFragment()
            else -> WeekCoursesFragment()
        }

        override fun getCount() = 2

        override fun getPageTitle(position: Int): String = when (position) {
            0 -> getString(R.string.main_today_courses)
            else -> getString(R.string.main_week_courses)
        }
    }
}
