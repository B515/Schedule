package xyz.b515.schedule.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import xyz.b515.schedule.entity.Course
import xyz.b515.schedule.entity.Spacetime
import java.sql.SQLException

class DBHelper(context: Context) : OrmLiteSqliteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    val courseDao: Dao<Course, Int> by lazy<Dao<Course, Int>> { getDao(Course::class.java) }
    val spacetimeDao: Dao<Spacetime, Int> by lazy<Dao<Spacetime, Int>> { getDao(Spacetime::class.java) }

    override fun onCreate(db: SQLiteDatabase, connectionSource: ConnectionSource) {
        try {
            TableUtils.createTable(connectionSource, Course::class.java)
            TableUtils.createTable(connectionSource, Spacetime::class.java)
        } catch (e: SQLException) {
            Log.e(DBHelper::class.java.name, "onCreate", e)
            throw RuntimeException(e)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, connectionSource: ConnectionSource, oldVersion: Int, newVersion: Int) {
        try {
            TableUtils.dropTable<Course, Any>(connectionSource, Course::class.java, true)
            TableUtils.dropTable<Spacetime, Any>(connectionSource, Spacetime::class.java, true)
            onCreate(db, connectionSource)
        } catch (e: SQLException) {
            Log.e(DBHelper::class.java.name, "onUpgrade", e)
            throw RuntimeException(e)
        }

    }

    companion object {
        private val DATABASE_NAME = "schedule.db"
        private val DATABASE_VERSION = 2
    }
}
