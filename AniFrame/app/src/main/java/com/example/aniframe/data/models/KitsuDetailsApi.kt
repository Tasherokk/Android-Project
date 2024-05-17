import com.example.aniframe.data.models.AgeRating
import com.example.aniframe.data.models.Attributes
import com.example.aniframe.data.models.Kitsu
import com.example.aniframe.data.models.PosterImage


data class KitsuDetailsApi(
    val id: String,
    val type: String,
    val attributes: DetailAttributesApi
) {

    companion object {
        fun toKitsu(kitsuApi: KitsuDetailsApi) = Kitsu(
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

data class DetailAttributesApi(
    val startDate: String,
    val endDate: String,
    val status: Status,
    val canonicalTitle: String,
    val averageRating: String,
    val ageRating: AgeRating,
    val posterImage: PosterImage,
    val description: String,
    val subtype: Subtype
)

enum class Status{
    current, finished, tba, unreleased, upcoming
}
enum class Subtype{
    ONA,  OVA, TV, movie, music, string, special
}