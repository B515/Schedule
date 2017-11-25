package xyz.b515.schedule.ui.view

import android.app.Fragment
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.widget.DefaultItemAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_today.view.*
import xyz.b515.schedule.Constant
import xyz.b515.schedule.R
import xyz.b515.schedule.db.CourseManager
import xyz.b515.schedule.ui.adapter.CourseAdapter
import java.util.*

/**
 * Created by Yun on 2017.4.24.
 */

class TodayCoursesFragment : Fragment() {

    lateinit var adapter: CourseAdapter
    private val manager: CourseManager by lazy { CourseManager(context) }
    private val preferences: SharedPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(context) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_today, container, false)

        adapter = CourseAdapter(arrayListOf())
        view.recycler.adapter = adapter
        view.recycler.itemAnimator = DefaultItemAnimator()

        val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        val currentWeek = preferences.getInt(Constant.CURRENT_WEEK, -1) + 1
        adapter.items.clear()
        manager.getAllCourse()
                .flatMap { it.spacetimes!! }
                .filter { it.weekday == today }
                .filter { currentWeek in it.startWeek..it.endWeek }
                .sortedBy { it.startTime }
                .forEach { adapter.items.add(it.course) }
        adapter.notifyDataSetChanged()

        return view
    }
}
