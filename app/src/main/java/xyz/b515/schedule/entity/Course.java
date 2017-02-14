package xyz.b515.schedule.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "courses")
public class Course {
    @DatabaseField(generatedId = true) int id;
    @DatabaseField int week;
    @DatabaseField int startTime;
    @DatabaseField int endTime;
    @DatabaseField int startWeek;
    @DatabaseField int endWeek;
    @DatabaseField String name;
    @DatabaseField String teacher;
    @DatabaseField String location;
    @DatabaseField boolean oddWeek;
    @DatabaseField boolean evenWeek;

    public Course() {
    }

    public Course(int id, int week, int startTime, int endTime, int startWeek, int endWeek, String name, String teacher, String location, boolean oddWeek, boolean evenWeek) {
        this.id = id;
        this.week = week;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startWeek = startWeek;
        this.endWeek = endWeek;
        this.name = name;
        this.teacher = teacher;
        this.location = location;
        this.oddWeek = oddWeek;
        this.evenWeek = evenWeek;
    }

    public int getId() {
        return id;
    }

    public int getWeek() {
        return week;
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

    public void setWeek(int week) {
        this.week = week;
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
}
