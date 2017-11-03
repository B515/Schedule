package xyz.b515.schedule.ui.view

import android.app.Fragment
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_week.view.*
import xyz.b515.schedule.Constant
import xyz.b515.schedule.R
import xyz.b515.schedule.db.CourseManager
import xyz.b515.schedule.entity.Spacetime

/**
 * Created by Yun on 2017.4.24.
 */

class WeekCoursesFragment : Fragment() {
    lateinit var manager: CourseManager
    lateinit var preferences: SharedPreferences
    private var baseHeight: Int = 0
    private var baseWidth: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_week, container, false)
        manager = CourseManager(context!!)
        preferences = PreferenceManager.getDefaultSharedPreferences(context)

        showHeaders(view)
        showCourses(view)
        return view
    }

    private fun showHeaders(view: View) {
        val column = 7
        val row = 12

        val metrics = resources.displayMetrics
        baseHeight = Math.round(metrics.heightPixels * (1.0 / 15.0)).toInt()
        baseWidth = Math.round(metrics.widthPixels * (2.0 / 15.0)).toInt()

        for (i in 0 until column) {
            val tv = TextView(context)
            tv.text = resources.getStringArray(R.array.weekdays)[i]
            tv.gravity = Gravity.CENTER
            tv.width = baseWidth
            view.header_weeks.addView(tv)
        }
        for (i in 1..row) {
            val tv = TextView(context)
            tv.text = i.toString()
            tv.gravity = Gravity.CENTER
            tv.height = baseHeight
            view.header_time.addView(tv)
        }
    }

    private fun showCourses(view: View) {
        val currentWeek = preferences.getInt(Constant.CURRENT_WEEK, -1) + 1
        val list = manager.getAllCourse()
        list.flatMap { it.spacetimes!! }
                .filter { currentWeek in it.startWeek..it.endWeek }
                .forEach { this.addCourseToView(it, view) }
    }

    private fun addCourseToView(spacetime: Spacetime, view: View) {
        val course = spacetime.course

        val v = LayoutInflater.from(context).inflate(R.layout.item_course_view, view.courses_layout, false) as LinearLayout
        val textView = v.findViewById<TextView>(R.id.course_name)
        textView.text = "${course.name}\n${spacetime.location}"
        v.setBackgroundColor(course.color)
        val weekday = if (spacetime.weekday == 1) 7 else spacetime.weekday - 1
        v.x = ((weekday - 1) * baseWidth).toFloat()
        v.y = ((spacetime.startTime - 1) * baseHeight).toFloat()
        view.courses_layout.addView(v)
        v.layoutParams.height = (spacetime.endTime - spacetime.startTime + 1) * baseHeight
        v.layoutParams.width = baseWidth
        v.setOnClickListener {
            val intent = Intent(context, CourseDetailActivity::class.java)
            intent.putExtra(Constant.TOOLBAR_TITLE, false)
            intent.putExtra(Constant.COURSE_ID, course.id)
            context!!.startActivity(intent)
        }
    }

}
