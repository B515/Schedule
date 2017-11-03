package xyz.b515.schedule.db

import android.content.Context
import android.util.Log
import com.j256.ormlite.dao.Dao

import xyz.b515.schedule.entity.Course
import xyz.b515.schedule.entity.Spacetime

class CourseManager(context: Context) {
    private val TAG = "CourseManager"
    private val dbHelper = DBHelper(context)

    private fun courseTransaction(body: (Dao<Course, Int>) -> Int): Int {
        try {
            return body(dbHelper.courseDao)
        } catch (e: Exception) {
            Log.e(TAG, body.toString(), e)
        }
        return 0
    }

    private fun <T> courseTransaction(default: T, body: (Dao<Course, Int>) -> T?): T {
        try {
            return body(dbHelper.courseDao) ?: default
        } catch (e: Exception) {
            Log.e(TAG, body.toString(), e)
        }
        return default
    }

    private fun <T> courseTransaction(body: (Dao<Course, Int>) -> MutableList<T>): MutableList<T> {
        try {
            return body(dbHelper.courseDao)
        } catch (e: Exception) {
            Log.e(TAG, body.toString(), e)
        }
        return mutableListOf()
    }

    private fun spacetimeTransaction(body: (Dao<Spacetime, Int>) -> Int): Int {
        try {
            return body(dbHelper.spacetimeDao)
        } catch (e: Exception) {
            Log.e(TAG, body.toString(), e)
        }
        return 0
    }

    private fun courseSpacetimeTransaction(body: (Dao<Course, Int>, Dao<Spacetime, Int>) -> Int): Int {
        try {
            return body(dbHelper.courseDao, dbHelper.spacetimeDao)
        } catch (e: Exception) {
            Log.e(TAG, body.toString(), e)
        }
        return 0
    }

    fun getAllCourse() = courseTransaction<Course> { it.queryForAll() }

    fun insertCourse(courses: List<Course>) = courseTransaction { it.create(courses) }

    fun insertCourse(course: Course) = courseTransaction { it.create(course) }

    fun getCourse(name: String) = courseTransaction<Course>(Course()) {
        it.queryBuilder().where().eq("name", name).queryForFirst()
    }

    fun getCourse(id: Int) = courseTransaction<Course>(Course()) { it.queryForId(id) }

    fun updateCourse(course: Course) = courseTransaction { it.update(course) }

    fun clearCourse() = courseSpacetimeTransaction { cd, sd ->
        cd.deleteBuilder().delete() + sd.deleteBuilder().delete()
    }

    fun insertSpacetime(spacetime: Spacetime) = spacetimeTransaction { it.create(spacetime) }

    fun updateSpacetime(spacetime: Spacetime) = spacetimeTransaction { it.update(spacetime) }

    fun deleteCourse(course: Course) = courseSpacetimeTransaction { cd, sd ->
        cd.delete(course) + sd.delete(course.spacetimes)
    }

    fun deleteSpacetime(spacetime: Spacetime) = spacetimeTransaction { it.delete(spacetime) }

}
