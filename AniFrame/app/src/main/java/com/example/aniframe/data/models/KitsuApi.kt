import com.example.aniframe.data.models.AgeRating
import com.example.aniframe.data.models.Attributes
import com.example.aniframe.data.models.Kitsu
import com.example.aniframe.data.models.PosterImage


data class KitsuApi(
        val id: String,
        val type: String,
        val attributes: AttributesApi
) {

    companion object {
        fun toKitsu(kitsuApi: KitsuApi) = Kitsu(
                id = kitsuApi.id,
                type = kitsuApi.type,
                attributes = Attributes(
                        startDate = kitsuApi.attributes.startDate,
                        canonicalTitle = kitsuApi.attributes.canonicalTitle,
                        averageRating = kitsuApi.attributes.averageRating,
                        ageRating = kitsuApi.attributes.ageRating,
                        posterImage = kitsuApi.attributes.posterImage
                )
        )
    }
}

data class AttributesApi(
    val startDate: String,
    val canonicalTitle: String,
    val averageRating: String,
    val ageRating: AgeRating,
    val posterImage: PosterImage
)