package xyz.b515.schedule.entity

import com.j256.ormlite.dao.ForeignCollection
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.field.ForeignCollectionField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(tableName = "courses")
data class Course(
        @DatabaseField(generatedId = true) var id: Int = 0,
        @DatabaseField var name: String = "",
        @DatabaseField var teacher: String = "",
        @DatabaseField var color: Int = 0,
        @ForeignCollectionField(eager = true) var spacetimes: ForeignCollection<Spacetime>? = null
)
