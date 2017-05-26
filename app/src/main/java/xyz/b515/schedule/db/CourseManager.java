package xyz.b515.schedule.db;

import android.content.Context;
import android.util.Log;

import java.util.List;

import xyz.b515.schedule.entity.Course;
import xyz.b515.schedule.entity.Spacetime;

public class CourseManager {
    private static final String TAG = "CourseManager";

    private DBHelper dbHelper;

    public CourseManager(Context context) {
        this.dbHelper = new DBHelper(context);
    }

    public boolean insertCourse(List<Course> courses) {
        try {
            dbHelper.getCourseDao().create(courses);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "insertCourse", e);
        }
        return false;
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

    public Course getCourse(String name) {
        try {
            return dbHelper.getCourseDao().queryBuilder().where().eq("name", name).queryForFirst();
        } catch (Exception e) {
            Log.e(TAG, "getCourse", e);
        }
        return null;
    }

    public int updateCourse(Course course) {
        try {
            return dbHelper.getCourseDao().update(course);
        } catch (Exception e) {
            Log.e(TAG, "updateCourse", e);
        }
        return 0;
    }

    public boolean clearCourse() {
        try {
            dbHelper.getCourseDao().deleteBuilder().delete();
            dbHelper.getSpacetimeDao().deleteBuilder().delete();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "clearCourse", e);
        }
        return false;
    }

    public boolean insertSpacetime(Spacetime spacetime) {
        try {
            dbHelper.getSpacetimeDao().create(spacetime);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "insertSpacetime", e);
        }
        return false;
    }

    public int updateSpacetime(Spacetime spacetime) {
        try {
            return dbHelper.getSpacetimeDao().update(spacetime);
        } catch (Exception e) {
            Log.e(TAG, "updateSpacetime", e);
        }
        return 0;
    }

    public boolean deleteCourse(Course course){
        try {
            dbHelper.getCourseDao().delete(course);
            dbHelper.getSpacetimeDao().delete(course.getSpacetimes());
            return true;
        } catch (Exception e) {
            Log.e(TAG, "deleteCourse", e);
        }
        return false;
    }

    public boolean deleteSpacetime(Spacetime spacetime){
        try {
            dbHelper.getSpacetimeDao().delete(spacetime);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "deleteSpacetime", e);
        }
        return false;
    }
}
