package objects

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "itinerary_items",
        foreignKeys = [ForeignKey(entity = Itinerary::class,
                parentColumns = ["itinerary_id"],
                childColumns = ["itinerary_id"],
                onDelete = ForeignKey.CASCADE)])
data class ItineraryItem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id")
    var id: Int = 0,
    @ColumnInfo(name = "itinerary_id", index = true)
    var itineraryId: Int,
    @ColumnInfo(name = "day")
    var day: Int,
    @ColumnInfo(name = "activity")
    var activity: String,
    @ColumnInfo(name = "position")
    var position: Int
) : Serializable
