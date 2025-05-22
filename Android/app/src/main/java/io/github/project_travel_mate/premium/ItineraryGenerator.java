package io.github.project_travel_mate.premium;

import objects.Trip;

public class ItineraryGenerator {

    public static String generateItinerary(Trip trip) {
        // Placeholder for future itinerary generation logic
        if (trip == null) {
            return "No trip information";
        }
        return "Sample itinerary for " + trip.getTname();
    }
}
