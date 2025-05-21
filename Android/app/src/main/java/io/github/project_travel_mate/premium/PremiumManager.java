package io.github.project_travel_mate.premium;

import objects.SubscriptionTier;
import objects.User;

public class PremiumManager {

    public static boolean hasItineraryAccess(User user) {
        return user.getSubscriptionTier() != SubscriptionTier.BASIC;
    }

    public static boolean hasConciergeAccess(User user) {
        return user.getSubscriptionTier() == SubscriptionTier.CONCIERGE;
    }
}
