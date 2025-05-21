package objects

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Represents a single entry in a restaurant menu.
 */
data class MenuItem(
    @SerializedName("name") var name: String,
    @SerializedName("price") var price: Float
) : Serializable
