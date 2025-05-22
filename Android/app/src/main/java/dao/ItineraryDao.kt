package dao

import android.arch.persistence.room.*
import objects.Itinerary
import objects.ItineraryItem

@Dao
interface ItineraryDao {
    @Insert
    fun insertItinerary(itinerary: Itinerary): Long

    @Insert
    fun insertItems(items: List<ItineraryItem>)

    @Query("SELECT * FROM itineraries WHERE user_id = :userId")
    fun getItineraries(userId: String): List<Itinerary>

    @Query("SELECT * FROM itinerary_items WHERE itinerary_id = :itineraryId ORDER BY day, position")
    fun getItems(itineraryId: Int): List<ItineraryItem>

    @Update
    fun updateItem(item: ItineraryItem)

    @Delete
    fun deleteItem(item: ItineraryItem)
}
