package xyz.b515.schedule.entity;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "courses")
public class Course {
    @DatabaseField(generatedId = true) int id;

    @DatabaseField String name;
    @DatabaseField String teacher;
    @DatabaseField int color;
    @ForeignCollectionField() ForeignCollection<Spacetime> spacetimes;

    public Course() {
    }

    public Course(int id, String name, String teacher, int color, ForeignCollection<Spacetime> spacetimes) {
        this.id = id;
        this.name = name;
        this.teacher = teacher;
        this.color = color;
        this.spacetimes = spacetimes;
    }

    public int getColor() {
        return color;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public ForeignCollection<Spacetime> getSpacetimes() {
        return spacetimes;
    }

    public void setSpacetimes(ForeignCollection<Spacetime> spacetimes) {
        this.spacetimes = spacetimes;
    }
}
