package xyz.b515.schedule.db;

import android.content.Context;
import android.util.Log;

import java.util.List;

import xyz.b515.schedule.entity.Course;

public class CourseManager {
    private static final String TAG = "CourseManager";

    private DBHelper dbHelper;

    public CourseManager(Context context) {
        this.dbHelper = new DBHelper(context);
    }

    public boolean insertCourse(Course course) {
        try {
            dbHelper.getCourseDao().create(course);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "insertCourse", e);
        }
        return false;
    }

    public List<Course> getAllCourse() {
        try {
            return dbHelper.getCourseDao().queryForAll();
        } catch (Exception e) {
            Log.e(TAG, "getAllCourse", e);
        }
        return null;
    }

    public boolean clearCourse() {
        try {
            dbHelper.getCourseDao().deleteBuilder().delete();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "clearCourse", e);
        }
        return false;
    }
}
