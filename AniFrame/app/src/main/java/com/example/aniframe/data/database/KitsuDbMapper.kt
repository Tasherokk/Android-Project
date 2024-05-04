package com.example.aniframe.data.database

import com.example.aniframe.data.models.AgeRating
import com.example.aniframe.data.models.Attributes
import com.example.aniframe.data.models.Kitsu
import com.example.aniframe.data.models.PosterImage

val KitsuDbMapper: (KitsuDB) -> Kitsu = {
    Kitsu(
        id = it.id,
        type = it.type,
        attributes = Attributes(
                createdAt = it.attributes.createdAt,
                canonicalTitle = it.attributes.canonicalTitle,
                averageRating = it.attributes.averageRating,
                ageRating = AgeRating.valueOf(it.attributes.ageRating), // Assuming ageRating is an enum
                posterImage = PosterImage(
                        tiny = it.attributes.posterImage.tiny,
                        small = it.attributes.posterImage.small,
                        large = it.attributes.posterImage.large,
                        original = it.attributes.posterImage.original
                )
        )
)
}