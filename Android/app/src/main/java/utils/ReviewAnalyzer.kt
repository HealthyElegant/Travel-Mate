package utils

/**
 * Utility object which performs a simple heuristic analysis on a list of
 * reviews to determine if the restaurant is a "hidden gem" and extracts
 * tips mentioned by locals.
 */
object ReviewAnalyzer {

    /**
     * Mark restaurant as hidden gem if any review contains certain keywords.
     */
    fun isHiddenGem(reviews: List<String>): Boolean {
        return reviews.any { review ->
            val lower = review.lowercase()
            listOf("hidden gem", "locals only", "local favorite").any { key ->
                lower.contains(key)
            }
        }
    }

    /**
     * Extract local tips from reviews. Any sentence containing the word "tip:" is
     * considered a tip.
     */
    fun extractLocalTips(reviews: List<String>): List<String> {
        val tips = mutableListOf<String>()
        for (review in reviews) {
            val lower = review.lowercase()
            val index = lower.indexOf("tip:")
            if (index != -1) {
                tips.add(review.substring(index + 4).trim())
            }
        }
        return tips
    }
}
