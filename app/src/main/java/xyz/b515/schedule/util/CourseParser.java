package xyz.b515.schedule.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xyz.b515.schedule.entity.Course;

public class CourseParser {
    private static final Pattern weekdayTimePattern = Pattern.compile("周(.)第(.+)节\\{第(\\d+)-(\\d+)周\\}");

    public static ArrayList<Course> parse(String text) {
        ArrayList<Course> list = new ArrayList<>();
        Document document = Jsoup.parse(text);
        Elements courses = document.getElementById("Table1").getElementsContainingText("周");
        for (Element td : courses) {
            String[] data = td.text().split("<br>");
            Course course = new Course();
            course.setName(data[0]);

            Matcher m = weekdayTimePattern.matcher(data[1]);
            course.setWeekday(translateWeekday(m.group(0)));
            String[] weeks = m.group(1).split(",");
            course.setStartTime(Integer.parseInt(weeks[0]));
            course.setEndTime(Integer.parseInt(weeks[weeks.length - 1]));
            course.setStartWeek(Integer.parseInt(m.group(2)));
            course.setEndWeek(Integer.parseInt(m.group(3)));

            course.setTeacher(data[2]);
            course.setLocation(data[3]);
            if (text.contains("单周")) {
                course.setOddWeek(true);
            } else if (text.contains("双周")) {
                course.setEvenWeek(true);
            }

            list.add(course);
        }
        return list;
    }

    private static int translateWeekday(String s) {
        switch (s) {
            case "一":
                return 1;
            case "二":
                return 2;
            case "三":
                return 3;
            case "四":
                return 4;
            case "五":
                return 5;
            case "六":
                return 6;
            case "日":
                return 7;
        }
        return 0;
    }
}
