package xyz.b515.schedule.util

import com.jrummyapps.android.colorpicker.ColorPickerDialog
import org.jsoup.Jsoup
import xyz.b515.schedule.db.CourseManager
import xyz.b515.schedule.entity.Course
import xyz.b515.schedule.entity.Spacetime
import java.util.*

object CourseParser {
    private val weekdayTimePattern = "周(.)第(.+)节\\{第(\\d+)-(\\d+)周\\}".toRegex()

    fun parse(text: String, manager: CourseManager) {
        val names = ArrayList<String>()

        val document = Jsoup.parse(text)
        val courses = document.getElementById("Table1").getElementsByTag("td")
        for (td in courses) {
            if ("周" !in td.text())
                continue

            for (nodes in td.textNodes().map { it.text() }.chunk(4)) {
                val name = nodes[0]
                val course: Course
                if (name !in names) {
                    names.add(name)

                    course = Course()
                    course.name = name
                    course.teacher = nodes[2]

                    val color = ColorPickerDialog.MATERIAL_COLORS[Random().nextInt(18)]
                    course.color = ColorHelper.shadeColor(color, 0.33)

                    manager.insertCourse(course)
                } else {
                    course = manager.getCourse(name)
                }

                val spacetime = Spacetime()
                spacetime.course = course

                val m = weekdayTimePattern.find(nodes[1])
                m?.let {
                    val values = m.groupValues
                    val weeks = values[2].split(",")
                    with(spacetime) {
                        weekday = translateWeekday(values[1])
                        startTime = weeks.first().toInt()
                        endTime = weeks.last().toInt()
                        startWeek = values[3].toInt()
                        endWeek = values[4].toInt()
                        location = nodes[3]
                        oddWeek = "单周" in nodes[1]
                        evenWeek = "双周" in nodes[1]
                    }
                }

                manager.insertSpacetime(spacetime)
            }
        }
    }

    private fun translateWeekday(s: String) = when (s) {
        "一" -> Calendar.MONDAY
        "二" -> Calendar.TUESDAY
        "三" -> Calendar.WEDNESDAY
        "四" -> Calendar.THURSDAY
        "五" -> Calendar.FRIDAY
        "六" -> Calendar.SATURDAY
        "日" -> Calendar.SUNDAY
        else -> 0
    }

    fun <T> List<T>.chunk(size: Int): List<List<T>> {
        tailrec fun <T> List<T>._chunk(size: Int, initial: List<List<T>> = emptyList()): List<List<T>> {
            val result = initial + listOf(this.take(size))
            return if (this.size > size) this.drop(size)._chunk(size, result) else result
        }
        return this._chunk(size)
    }
}
