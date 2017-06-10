package xyz.b515.schedule.util;

import com.jrummyapps.android.colorpicker.ColorPickerDialog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xyz.b515.schedule.db.CourseManager;
import xyz.b515.schedule.entity.Course;
import xyz.b515.schedule.entity.Spacetime;

public class CourseParser {
    private static final Pattern weekdayTimePattern = Pattern.compile("周(.)第(.+)节\\{第(\\d+)-(\\d+)周\\}");

    public static void parse(String text, CourseManager manager) {
        ArrayList<String> names = new ArrayList<>();

        Document document = Jsoup.parse(text);
        Elements courses = document.getElementById("Table1").getElementsByTag("td");
        for (Element td : courses) {
            if (!td.text().contains("周"))
                continue;
            List<TextNode> nodes = td.textNodes();

            String name = nodes.get(0).text();
            Course course;
            if (!names.contains(name)) {
                names.add(name);

                course = new Course();
                course.setName(name);
                course.setTeacher(nodes.get(2).text());

                int color = ColorPickerDialog.MATERIAL_COLORS[new Random().nextInt(18)];
                course.setColor(ColorHelper.shadeColor(color, 0.33));

                manager.insertCourse(course);
            } else {
                course = manager.getCourse(name);
            }

            Spacetime spacetime = new Spacetime();
            spacetime.setCourse(course);

            Matcher m = weekdayTimePattern.matcher(nodes.get(1).text());
            if (m.find()) {
                spacetime.setWeekday(translateWeekday(m.group(1)));
                String[] weeks = m.group(2).split(",");
                spacetime.setStartTime(Integer.parseInt(weeks[0]));
                spacetime.setEndTime(Integer.parseInt(weeks[weeks.length - 1]));
                spacetime.setStartWeek(Integer.parseInt(m.group(3)));
                spacetime.setEndWeek(Integer.parseInt(m.group(4)));
            }
            //TODO 数据通信原理 {第1-16周|2节/周}

            spacetime.setLocation(nodes.get(3).text());
            if (text.contains("单周")) {
                spacetime.setOddWeek(true);
            } else if (text.contains("双周")) {
                spacetime.setEvenWeek(true);
            }

            manager.insertSpacetime(spacetime);
        }
    }

    private static int translateWeekday(String s) {
        switch (s) {
            case "一":
                return Calendar.MONDAY;
            case "二":
                return Calendar.TUESDAY;
            case "三":
                return Calendar.WEDNESDAY;
            case "四":
                return Calendar.THURSDAY;
            case "五":
                return Calendar.FRIDAY;
            case "六":
                return Calendar.SATURDAY;
            case "日":
                return Calendar.SUNDAY;
        }
        return 0;
    }
}
