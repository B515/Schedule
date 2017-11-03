package xyz.b515.schedule.util

import com.jrummyapps.android.colorpicker.ColorPickerDialog
import org.jsoup.Jsoup
import xyz.b515.schedule.db.CourseManager
import xyz.b515.schedule.entity.Course
import xyz.b515.schedule.entity.Spacetime
import java.util.*
import java.util.regex.Pattern

object CourseParser {
    private val weekdayTimePattern = Pattern.compile("周(.)第(.+)节\\{第(\\d+)-(\\d+)周\\}")

    fun parse(text: String, manager: CourseManager) {
        val names = ArrayList<String>()

        val document = Jsoup.parse(text)
        val courses = document.getElementById("Table1").getElementsByTag("td")
        for (td in courses) {
            if (!td.text().contains("周"))
                continue
            val nodes = td.textNodes()

            val name = nodes[0].text()
            val course: Course?
            if (!names.contains(name)) {
                names.add(name)

                course = Course()
                course.name = name
                course.teacher = nodes[2].text()

                val color = ColorPickerDialog.MATERIAL_COLORS[Random().nextInt(18)]
                course.color = ColorHelper.shadeColor(color, 0.33)

                manager.insertCourse(course)
            } else {
                course = manager.getCourse(name)
            }

            val spacetime = Spacetime()
            spacetime.course = course

            val m = weekdayTimePattern.matcher(nodes[1].text())
            if (m.find()) {
                spacetime.weekday = translateWeekday(m.group(1))
                val weeks = m.group(2).split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                spacetime.startTime = Integer.parseInt(weeks[0])
                spacetime.endTime = Integer.parseInt(weeks[weeks.size - 1])
                spacetime.startWeek = Integer.parseInt(m.group(3))
                spacetime.endWeek = Integer.parseInt(m.group(4))
            }
            //TODO 数据通信原理 {第1-16周|2节/周}

            spacetime.location = nodes[3].text()
            if (text.contains("单周")) {
                spacetime.oddWeek = true
            } else if (text.contains("双周")) {
                spacetime.evenWeek = true
            }

            manager.insertSpacetime(spacetime)
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
}
