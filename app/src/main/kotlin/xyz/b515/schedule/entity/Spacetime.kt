package xyz.b515.schedule.entity

import android.databinding.BindingAdapter
import android.databinding.InverseBindingAdapter
import android.widget.TextView

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

/**
 * Created by Yun on 2017.4.26.
 */

@DatabaseTable(tableName = "spacetimes")
data class Spacetime(
        @DatabaseField(generatedId = true) var id: Int = 0,
        @DatabaseField var weekday: Int = 0,
        @DatabaseField var startTime: Int = 0,
        @DatabaseField var endTime: Int = 0,
        @DatabaseField var startWeek: Int = 0,
        @DatabaseField var endWeek: Int = 0,
        @DatabaseField var location: String = "",
        @DatabaseField var oddWeek: Boolean = false,
        @DatabaseField var evenWeek: Boolean = false,
        @DatabaseField(foreign = true, foreignAutoRefresh = true) var course: Course = Course()) {

    companion object {
        @JvmStatic
        @BindingAdapter("android:text")
        fun setText(view: TextView, value: Int) {
            view.text = value.toString()
        }

        @JvmStatic
        @InverseBindingAdapter(attribute = "android:text")
        fun getText(view: TextView): Int {
            val text = view.text.toString()
            return if (text.isEmpty()) 0 else Integer.parseInt(text)
        }
    }
}