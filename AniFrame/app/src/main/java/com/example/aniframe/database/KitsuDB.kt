package com.example.aniframe.database

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kitsudb")
data class KitsuDB(
        @PrimaryKey val id: String,
        val type: String,
        @Embedded val attributes: AttributesDB,
        val tag: String? = null
)

data class AttributesDB(
        @ColumnInfo(name = "created_at") val createdAt: String,
        @ColumnInfo(name = "canonical_title") val canonicalTitle: String,
        @ColumnInfo(name = "average_rating") val averageRating: String,
        @ColumnInfo(name = "age_rating") val ageRating: String,
        @Embedded val posterImage: PosterImageDB
)

data class PosterImageDB (
        val tiny: String,
        val small: String,
        val large: String,
        val original: String
)