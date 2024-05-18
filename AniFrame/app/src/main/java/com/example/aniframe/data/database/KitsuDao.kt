package com.example.aniframe.data.database

import androidx.room.*

@Dao
interface KitsuDao {
    @Query("SELECT * FROM kitsudb")
      fun getAll(): List<KitsuDB>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(kitsuList: List<KitsuDB>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(kitsu: KitsuDB)
    @Update
    fun updateKitsu(kitsu: KitsuDB)
    @Delete
    fun delete(kitsu: KitsuDB)
    @Query("SELECT * FROM kitsudb WHERE tag = :tag")
    fun getAllByTag(tag: String): List<KitsuDB>

}