package objects

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "itineraries")
data class Itinerary(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "itinerary_id")
    var id: Int = 0,
    @ColumnInfo(name = "user_id")
    var userId: String,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "days")
    var days: Int
) : Serializable
