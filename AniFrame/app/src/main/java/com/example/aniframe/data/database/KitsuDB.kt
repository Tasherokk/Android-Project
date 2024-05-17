package com.example.aniframe.data.database

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.aniframe.data.models.AgeRating
import com.example.aniframe.data.models.Tag

@Entity(tableName = "kitsudb")
data class KitsuDB(
        @PrimaryKey val id: String,
        val type: String,
        @Embedded val attributes: AttributesDB,
        var tag: String? = null
)

data class AttributesDB(
        @ColumnInfo(name = "created_at") val startDate: String?,
        @ColumnInfo(name = "canonical_title") val canonicalTitle: String?,
        @ColumnInfo(name = "average_rating") val averageRating: String?,
        @ColumnInfo(name = "age_rating") val ageRating: AgeRating?,
        @Embedded val posterImage: PosterImageDB
)

data class PosterImageDB(
        val tiny: String?,
        val small: String?,
        val large: String?,
        val original: String?
)
