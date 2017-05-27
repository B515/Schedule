package xyz.b515.schedule.entity;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.widget.TextView;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Yun on 2017.4.26.
 */

@DatabaseTable(tableName = "spacetimes")
public class Spacetime {
    @DatabaseField(generatedId = true) int id;
    @DatabaseField int weekday;
    @DatabaseField int startTime;
    @DatabaseField int endTime;
    @DatabaseField int startWeek;
    @DatabaseField int endWeek;
    @DatabaseField String location;
    @DatabaseField boolean oddWeek = false;
    @DatabaseField boolean evenWeek = false;
    @DatabaseField(foreign = true, foreignAutoRefresh = true) Course course;

    public Spacetime() {
    }

    public Spacetime(int weekday, int startTime, int endTime, int startWeek, int endWeek, String location, boolean oddWeek, boolean evenWeek) {
        this.weekday = weekday;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startWeek = startWeek;
        this.endWeek = endWeek;
        this.location = location;
        this.oddWeek = oddWeek;
        this.evenWeek = evenWeek;
    }

    @BindingAdapter("android:text")
    public static void setText(TextView view, int value) {
        view.setText(String.valueOf(value));
    }

    @InverseBindingAdapter(attribute = "android:text")
    public static int getText(TextView view) {
        String text = view.getText().toString();
        return text.isEmpty() ? 0 : Integer.parseInt(text);
    }

    public int getWeekday() {
        return weekday;
    }

    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(int startWeek) {
        this.startWeek = startWeek;
    }

    public int getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(int endWeek) {
        this.endWeek = endWeek;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isOddWeek() {
        return oddWeek;
    }

    public void setOddWeek(boolean oddWeek) {
        this.oddWeek = oddWeek;
    }

    public boolean isEvenWeek() {
        return evenWeek;
    }

    public void setEvenWeek(boolean evenWeek) {
        this.evenWeek = evenWeek;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
