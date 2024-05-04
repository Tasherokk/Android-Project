import com.example.aniframe.models.AgeRating
import com.example.aniframe.models.Attributes
import com.example.aniframe.models.Kitsu
import com.example.aniframe.models.PosterImage


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
                        createdAt = kitsuApi.attributes.createdAt,
                        canonicalTitle = kitsuApi.attributes.canonicalTitle,
                        averageRating = kitsuApi.attributes.averageRating,
                        ageRating = kitsuApi.attributes.ageRating,
                        posterImage = kitsuApi.attributes.posterImage
                )
        )
    }
}

data class AttributesApi(
        val createdAt: String,
        val canonicalTitle: String,
        val averageRating: String,
        val ageRating: AgeRating,
        val posterImage: PosterImage
)