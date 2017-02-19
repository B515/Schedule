package xyz.b515.schedule.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "courses")
public class Course {
    @DatabaseField(generatedId = true) int id;
    @DatabaseField int weekday;
    @DatabaseField int startTime;
    @DatabaseField int endTime;
    @DatabaseField int startWeek;
    @DatabaseField int endWeek;
    @DatabaseField String name;
    @DatabaseField String teacher;
    @DatabaseField String location;
    @DatabaseField boolean oddWeek = false;
    @DatabaseField boolean evenWeek = false;
    @DatabaseField int color;

    public Course() {
    }

    public Course(int id, int weekday, int startTime, int endTime, int startWeek, int endWeek, String name, String teacher, String location, boolean oddWeek, boolean evenWeek, int color) {
        this.id = id;
        this.weekday = weekday;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startWeek = startWeek;
        this.endWeek = endWeek;
        this.name = name;
        this.teacher = teacher;
        this.location = location;
        this.oddWeek = oddWeek;
        this.evenWeek = evenWeek;
        this.color = color;
    }

    public int getColor() {
        return color;
    }


    public int getId() {
        return id;
    }

    public int getWeekday() {
        return weekday;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public int getStartWeek() {
        return startWeek;
    }

    public int getEndWeek() {
        return endWeek;
    }

    public String getName() {
        return name;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getLocation() {
        return location;
    }

    public boolean isOddWeek() {
        return oddWeek;
    }

    public boolean isEvenWeek() {
        return evenWeek;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public void setStartWeek(int startWeek) {
        this.startWeek = startWeek;
    }

    public void setEndWeek(int endWeek) {
        this.endWeek = endWeek;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setOddWeek(boolean oddWeek) {
        this.oddWeek = oddWeek;
    }

    public void setEvenWeek(boolean evenWeek) {
        this.evenWeek = evenWeek;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", weekday=" + weekday +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", startWeek=" + startWeek +
                ", endWeek=" + endWeek +
                ", name='" + name + '\'' +
                ", teacher='" + teacher + '\'' +
                ", location='" + location + '\'' +
                ", oddWeek=" + oddWeek +
                ", evenWeek=" + evenWeek +
                ", color=" + color +
                '}';
    }
}
