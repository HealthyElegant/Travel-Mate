package utils

import objects.ItineraryItem

object ItineraryGenerator {
    private val defaultActivities = listOf(
        "Breakfast",
        "Morning sightseeing",
        "Lunch",
        "Afternoon tour",
        "Dinner"
    )

    fun generate(days: Int, itineraryId: Int = 0): List<ItineraryItem> {
        val items = mutableListOf<ItineraryItem>()
        for (day in 1..days) {
            var position = 0
            for (activity in defaultActivities) {
                items.add(ItineraryItem(0, itineraryId, day, activity, position++))
            }
        }
        return items
    }
}
