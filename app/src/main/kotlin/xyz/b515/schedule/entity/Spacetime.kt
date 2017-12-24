package xyz.b515.schedule.entity

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

/**
 * Created by Yun on 2017.4.26.
 */

@DatabaseTable(tableName = "spacetimes")
data class Spacetime(
        @DatabaseField(generatedId = true) var id: Int = 0,
        @DatabaseField var weekday: Int = 0,
        @DatabaseField var startTime: Int = 0,
        @DatabaseField var endTime: Int = 0,
        @DatabaseField var startWeek: Int = 0,
        @DatabaseField var endWeek: Int = 0,
        @DatabaseField var location: String = "",
        @DatabaseField var oddWeek: Boolean = false,
        @DatabaseField var evenWeek: Boolean = false,
        @DatabaseField(foreign = true, foreignAutoRefresh = true) var course: Course = Course())
