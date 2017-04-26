package xyz.b515.schedule.util;

import android.graphics.Color;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xyz.b515.schedule.entity.Course;

public class CourseParser {
    private static final Pattern weekdayTimePattern = Pattern.compile("周(.)第(.+)节\\{第(\\d+)-(\\d+)周\\}");

    public static ArrayList<Course> parse(String text) {
        ArrayList<Course> list = new ArrayList<>();
        Document document = Jsoup.parse(text);
        Elements courses = document.getElementById("Table1").getElementsByTag("td");
        for (Element td : courses) {
            if (!td.text().contains("周"))
                continue;
            List<TextNode> nodes =td.textNodes();
            Course course = new Course();
            course.setName(nodes.get(0).text());

            Matcher m = weekdayTimePattern.matcher(nodes.get(1).text());
            if (m.find()) {
                //course.setWeekday(translateWeekday(m.group(1)));
                String[] weeks = m.group(2).split(",");
                //course.setStartTime(Integer.parseInt(weeks[0]));
                //course.setEndTime(Integer.parseInt(weeks[weeks.length - 1]));
                //course.setStartWeek(Integer.parseInt(m.group(3)));
                //course.setEndWeek(Integer.parseInt(m.group(4)));
            }
            //TODO 数据通信原理 {第1-16周|2节/周}

            course.setTeacher(nodes.get(2).text());
            //course.setLocation(nodes.get(3).text());
            if (text.contains("单周")) {
                //course.setOddWeek(true);
            } else if (text.contains("双周")) {
                //course.setEvenWeek(true);
            }

            Random rnd = new Random();
            course.setColor(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));

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
