package xyz.b515.schedule.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_course_summary.view.*
import org.jetbrains.anko.startActivity
import xyz.b515.schedule.Constant
import xyz.b515.schedule.R
import xyz.b515.schedule.entity.Course
import xyz.b515.schedule.ui.view.CourseDetailActivity
import java.text.DateFormatSymbols

/**
 * Created by Yun on 2017.4.24.
 */

class CourseAdapter(var items: ArrayList<Course>) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CourseViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_course_summary, viewGroup, false)
        return CourseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.v.tv_name.text = items[position].name
        val locations = items[position].spacetimes!!
                .map { it.location }
                .distinct()
                .joinToString(", ")
        holder.v.tv_location.text = locations
        holder.v.tv_teacher.text = items[position].teacher

        val weeks = DateFormatSymbols.getInstance().shortWeekdays
        val times = items[position].spacetimes!!
                .joinToString(", ") { (_, weekday, startTime, endTime) -> "${weeks[weekday]} $startTime-$endTime" }
        holder.v.tv_time.text = times

        holder.itemView.setOnClickListener { v ->
            v.context.startActivity<CourseDetailActivity>(Constant.TOOLBAR_TITLE to false, Constant.COURSE_ID to items[position].id)
        }
    }

    override fun getItemCount() = items.size

    class CourseViewHolder(val v: View) : RecyclerView.ViewHolder(v)
}
