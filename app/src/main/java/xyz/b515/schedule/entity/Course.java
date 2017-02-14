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
}
