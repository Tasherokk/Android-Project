package com.example.aniframe.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.aniframe.models.AgeRating
import com.example.aniframe.models.Kitsu

@Entity(tableName = "kitsudb")
data class KitsuDB(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "canonical_title") val canonicalTitle: String,
    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "average_rating") val averageRating: String,
    @ColumnInfo(name = "age_rating") val ageRating: AgeRating,
    @ColumnInfo(name = "poster_image_url") val posterImageUrl: String
) {
    constructor(kitsu: Kitsu) : this(
        kitsu.id,
        kitsu.attributes.canonicalTitle,
        kitsu.attributes.createdAt,
        kitsu.attributes.averageRating,
        kitsu.attributes.ageRating,
        kitsu.attributes.posterImage.original
    )
}
